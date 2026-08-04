using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using Npgsql;

namespace JxPoint.Models;

public record PartieHistorique
(
    int PartieId,
    string Joueur1,
    string Joueur2,
    DateTime Date,
    string Statut,
    int? GagnantId
);

public class DatabaseService
{
    private readonly string _connectionString;

    public DatabaseService(string connectionString)
    {
        _connectionString = connectionString ?? throw new ArgumentNullException(nameof(connectionString));
    }

    public async Task<NpgsqlConnection> ConnecterAsync()
    {
        var connexion = new NpgsqlConnection(_connectionString);
        await connexion.OpenAsync();
        return connexion;
    }

    public async Task<int> EnregistrerJoueur(string nom)
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand("INSERT INTO joueurs(nom, date_creation) VALUES(@nom, @date_creation) RETURNING id", cn);
        cmd.Parameters.AddWithValue("@nom", nom);
        cmd.Parameters.AddWithValue("@date_creation", DateTime.UtcNow);
        var idObj = await cmd.ExecuteScalarAsync();
        if (idObj == null) throw new InvalidOperationException("Impossible de récupérer l'ID du joueur enregistré.");
        return Convert.ToInt32(idObj);
    }

    public async Task<int> SauvegarderPartie(int joueur1Id, int joueur2Id, int? gagnantId, string statut)
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand(
            "INSERT INTO parties(joueur1_id, joueur2_id, date, gagnant_id, statut) VALUES(@j1,@j2,@date,@gagnant,@statut) RETURNING id", cn);

        cmd.Parameters.AddWithValue("@j1", joueur1Id);
        cmd.Parameters.AddWithValue("@j2", joueur2Id);
        cmd.Parameters.AddWithValue("@date", DateTime.UtcNow);
        if (gagnantId.HasValue)
            cmd.Parameters.AddWithValue("@gagnant", gagnantId.Value);
        else
            cmd.Parameters.AddWithValue("@gagnant", DBNull.Value);
        cmd.Parameters.AddWithValue("@statut", statut);

        var idObj = await cmd.ExecuteScalarAsync();
        if (idObj == null) throw new InvalidOperationException("Impossible de récupérer l'ID de la partie sauvegardée.");
        return Convert.ToInt32(idObj);
    }

    // Charge un état de plateau (simplifié)
    public async Task<List<(int col, int row, int joueur)>> ChargerSommets(int partieId)
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand("SELECT col, row, joueur FROM sommets WHERE partie_id = @pid", cn);
        cmd.Parameters.AddWithValue("@pid", partieId);

        var resultats = new List<(int col, int row, int joueur)>();
        await using var reader = await cmd.ExecuteReaderAsync();
        while (await reader.ReadAsync())
        {
            resultats.Add((reader.GetInt32(0), reader.GetInt32(1), reader.GetInt32(2)));
        }

        return resultats;
    }

    public async Task ViderSommets(int partieId)
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand("DELETE FROM sommets WHERE partie_id = @pid", cn);
        cmd.Parameters.AddWithValue("@pid", partieId);
        await cmd.ExecuteNonQueryAsync();
    } 

    public async Task EnregistrerSommet(int partieId, int col, int row, int joueur)
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand("INSERT INTO sommets(partie_id, col, row, joueur) VALUES(@pid,@col,@row,@joueur)", cn);
        cmd.Parameters.AddWithValue("@pid", partieId);
        cmd.Parameters.AddWithValue("@col", col);
        cmd.Parameters.AddWithValue("@row", row);
        cmd.Parameters.AddWithValue("@joueur", joueur);
        await cmd.ExecuteNonQueryAsync();
    }

    public async Task<int?> ChargerDernierePartieId()
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand("SELECT id FROM parties ORDER BY date DESC LIMIT 1", cn);
        var idObj = await cmd.ExecuteScalarAsync();
        if (idObj == null || idObj is DBNull) return null;
        return Convert.ToInt32(idObj);
    }

    public async Task<List<PartieHistorique>> ChargerHistoriqueParties()
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand(@"
            SELECT p.id, j1.nom, j2.nom, p.date, p.statut, p.gagnant_id
            FROM parties p
            LEFT JOIN joueurs j1 ON p.joueur1_id = j1.id
            LEFT JOIN joueurs j2 ON p.joueur2_id = j2.id
            ORDER BY p.date DESC
            LIMIT 50

        ", cn);

        var result = new List<PartieHistorique>();
        await using var reader = await cmd.ExecuteReaderAsync();
        while (await reader.ReadAsync())
        {
            result.Add(new PartieHistorique(
                PartieId: reader.GetInt32(0),
                Joueur1: reader.IsDBNull(1) ? "?" : reader.GetString(1),
                Joueur2: reader.IsDBNull(2) ? "?" : reader.GetString(2),
                Date: reader.GetDateTime(3),
                Statut: reader.IsDBNull(4) ? "" : reader.GetString(4),
                GagnantId: reader.IsDBNull(5) ? null : (int?)reader.GetInt32(5)
            ));
        }

        return result;
    }

    public async Task<string?> ChargerNomJoueur(int joueurId)
    {
        await using var cn = await ConnecterAsync();
        var cmd = new NpgsqlCommand("SELECT nom FROM joueurs WHERE id = @id", cn);
        cmd.Parameters.AddWithValue("@id", joueurId);
        var obj = await cmd.ExecuteScalarAsync();
        if (obj == null || obj is DBNull) return null;
        return obj.ToString();
    }
}
