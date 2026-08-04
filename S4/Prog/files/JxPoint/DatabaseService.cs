using Npgsql;

namespace JxPoint.Models;

public class DatabaseService
{
    private readonly string _connectionString;

    public DatabaseService(string connectionString =
        "Host=localhost;Port=5432;Database=jxpoint;Username=postgres;Password=postgres")
    {
        _connectionString = connectionString;
    }

    // ─── Schéma ────────────────────────────────────────────────────────────────

    public async Task InitialiserBaseDeDonneesAsync()
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            CREATE TABLE IF NOT EXISTS joueurs (
                id SERIAL PRIMARY KEY,
                nom VARCHAR(64) NOT NULL UNIQUE,
                date_creation TIMESTAMP DEFAULT NOW()
            );

            CREATE TABLE IF NOT EXISTS parties (
                id SERIAL PRIMARY KEY,
                joueur1_id INT REFERENCES joueurs(id),
                joueur2_id INT REFERENCES joueurs(id),
                date TIMESTAMP DEFAULT NOW(),
                gagnant_id INT REFERENCES joueurs(id),
                statut VARCHAR(20) DEFAULT 'en_cours'
            );

            CREATE TABLE IF NOT EXISTS sommets (
                id SERIAL PRIMARY KEY,
                partie_id INT REFERENCES parties(id),
                col INT NOT NULL,
                row INT NOT NULL,
                joueur INT NOT NULL,
                tour INT NOT NULL
            );

            CREATE TABLE IF NOT EXISTS scores (
                id SERIAL PRIMARY KEY,
                partie_id INT REFERENCES parties(id),
                joueur_id INT REFERENCES joueurs(id),
                points INT DEFAULT 0
            );
            """;
        await cmd.ExecuteNonQueryAsync();
    }

    // ─── Joueurs ───────────────────────────────────────────────────────────────

    public async Task<int> EnregistrerJoueurAsync(string nom)
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO joueurs (nom) VALUES (@nom)
            ON CONFLICT (nom) DO UPDATE SET nom = EXCLUDED.nom
            RETURNING id;
            """;
        cmd.Parameters.AddWithValue("nom", nom);
        var result = await cmd.ExecuteScalarAsync();
        return Convert.ToInt32(result);
    }

    public async Task<List<(int id, string nom)>> ObtenirJoueursAsync()
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = "SELECT id, nom FROM joueurs ORDER BY date_creation DESC;";
        await using var reader = await cmd.ExecuteReaderAsync();
        var liste = new List<(int, string)>();
        while (await reader.ReadAsync())
            liste.Add((reader.GetInt32(0), reader.GetString(1)));
        return liste;
    }

    // ─── Parties ───────────────────────────────────────────────────────────────

    public async Task<int> CreerPartieAsync(int joueur1Id, int joueur2Id)
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO parties (joueur1_id, joueur2_id)
            VALUES (@j1, @j2) RETURNING id;
            """;
        cmd.Parameters.AddWithValue("j1", joueur1Id);
        cmd.Parameters.AddWithValue("j2", joueur2Id);
        return Convert.ToInt32(await cmd.ExecuteScalarAsync());
    }

    public async Task TerminerPartieAsync(int partieId, int? gagnantId)
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            UPDATE parties SET statut = 'terminée', gagnant_id = @gagnant
            WHERE id = @id;
            """;
        cmd.Parameters.AddWithValue("gagnant", gagnantId.HasValue ? (object)gagnantId.Value : DBNull.Value);
        cmd.Parameters.AddWithValue("id", partieId);
        await cmd.ExecuteNonQueryAsync();
    }

    // ─── Sauvegarde état plateau ───────────────────────────────────────────────

    public async Task SauvegarderEtatAsync(int partieId, PlateauModele plateau, int tour)
    {
        await using var conn = await OuvrirAsync();

        // Effacer les anciens sommets de ce tour
        await using var del = conn.CreateCommand();
        del.CommandText = "DELETE FROM sommets WHERE partie_id = @pid AND tour = @tour;";
        del.Parameters.AddWithValue("pid", partieId);
        del.Parameters.AddWithValue("tour", tour);
        await del.ExecuteNonQueryAsync();

        for (int c = 0; c <= plateau.Colonnes; c++)
        {
            for (int r = 0; r <= plateau.Lignes; r++)
            {
                int val = plateau.Etat[c, r];
                if (val == 0) continue;

                await using var ins = conn.CreateCommand();
                ins.CommandText = """
                    INSERT INTO sommets (partie_id, col, row, joueur, tour)
                    VALUES (@pid, @col, @row, @joueur, @tour);
                    """;
                ins.Parameters.AddWithValue("pid", partieId);
                ins.Parameters.AddWithValue("col", c);
                ins.Parameters.AddWithValue("row", r);
                ins.Parameters.AddWithValue("joueur", val);
                ins.Parameters.AddWithValue("tour", tour);
                await ins.ExecuteNonQueryAsync();
            }
        }
    }

    public async Task ChargerEtatAsync(int partieId, PlateauModele plateau)
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        // Charger le dernier tour
        cmd.CommandText = """
            SELECT col, row, joueur FROM sommets
            WHERE partie_id = @pid AND tour = (
                SELECT MAX(tour) FROM sommets WHERE partie_id = @pid
            );
            """;
        cmd.Parameters.AddWithValue("pid", partieId);
        await using var reader = await cmd.ExecuteReaderAsync();

        plateau.Reinitialiser();
        while (await reader.ReadAsync())
        {
            int col = reader.GetInt32(0);
            int row = reader.GetInt32(1);
            int joueur = reader.GetInt32(2);
            plateau.Etat[col, row] = joueur;
        }
    }

    // ─── Scores ────────────────────────────────────────────────────────────────

    public async Task MettreAJourScoreAsync(int partieId, int joueurId, int points)
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            INSERT INTO scores (partie_id, joueur_id, points)
            VALUES (@pid, @jid, @pts)
            ON CONFLICT (partie_id, joueur_id) DO UPDATE SET points = EXCLUDED.points;
            """;
        cmd.Parameters.AddWithValue("pid", partieId);
        cmd.Parameters.AddWithValue("jid", joueurId);
        cmd.Parameters.AddWithValue("pts", points);
        await cmd.ExecuteNonQueryAsync();
    }

    public async Task<List<(int partieId, string nom, int score)>> ObtenirScoresAsync()
    {
        await using var conn = await OuvrirAsync();
        await using var cmd = conn.CreateCommand();
        cmd.CommandText = """
            SELECT s.partie_id, j.nom, s.points
            FROM scores s
            JOIN joueurs j ON j.id = s.joueur_id
            ORDER BY s.points DESC;
            """;
        await using var reader = await cmd.ExecuteReaderAsync();
        var liste = new List<(int, string, int)>();
        while (await reader.ReadAsync())
            liste.Add((reader.GetInt32(0), reader.GetString(1), reader.GetInt32(2)));
        return liste;
    }

    // ─── Utilitaire ───────────────────────────────────────────────────────────

    private async Task<NpgsqlConnection> OuvrirAsync()
    {
        var conn = new NpgsqlConnection(_connectionString);
        await conn.OpenAsync();
        return conn;
    }

    public async Task<bool> TesterConnexionAsync()
    {
        try
        {
            await using var conn = await OuvrirAsync();
            return conn.State == System.Data.ConnectionState.Open;
        }
        catch
        {
            return false;
        }
    }
}
