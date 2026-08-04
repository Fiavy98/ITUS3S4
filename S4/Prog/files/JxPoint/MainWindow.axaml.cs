using Avalonia.Controls;
using Avalonia.Threading;
using JxPoint.Models;
using JxPoint.Views;

namespace JxPoint;

public partial class MainWindow : Window
{
    private readonly DatabaseService _db = new();
    private int _partieId = -1;
    private int _tourCount = 0;

    public MainWindow()
    {
        InitializeComponent();
        MonPlateau.OnScoreChanged += MettreAJourScores;
        MonPlateau.OnMessageChanged += MettreAJourMessage;
        MonPlateau.OnTourChanged += MettreAJourTour;

        BtnNouvelle.Click += (_, _) => NouvellePartie();
        BtnSauvegarder.Click += async (_, _) => await SauvegarderAsync();
        BtnCharger.Click += async (_, _) => await ChargerAsync();

        _ = VerifierConnexionDbAsync();
    }

    private void NouvellePartie()
    {
        MonPlateau.ReinitialiserJeu();
        _tourCount = 0;
        LabelMessage.Text = "";
    }

    private void MettreAJourScores(int scoreJ1, int scoreJ2)
    {
        ScoreJ1.Text = scoreJ1.ToString();
        ScoreJ2.Text = scoreJ2.ToString();
        _tourCount++;
    }

    private void MettreAJourMessage(string msg)
    {
        LabelMessage.Text = msg;
    }

    private void MettreAJourTour(int joueur, string nomJ1, string nomJ2)
    {
        LabelTour.Text = $"Tour du {(joueur == 1 ? nomJ1 : nomJ2)}";
        LabelTour.Foreground = joueur == 1
            ? Avalonia.Media.Brushes.CornflowerBlue
            : Avalonia.Media.Brushes.Salmon;
        NomJ1Label.Text = nomJ1;
        NomJ2Label.Text = nomJ2;
    }

    private async Task SauvegarderAsync()
    {
        if (!await _db.TesterConnexionAsync())
        {
            LabelMessage.Text = "⚠ Base de données non disponible.";
            return;
        }

        try
        {
            if (_partieId < 0)
            {
                int j1Id = await _db.EnregistrerJoueurAsync("Joueur 1");
                int j2Id = await _db.EnregistrerJoueurAsync("Joueur 2");
                _partieId = await _db.CreerPartieAsync(j1Id, j2Id);
            }

            var plateau = MonPlateau.ObtenirPlateau();
            await _db.SauvegarderEtatAsync(_partieId, plateau, _tourCount);
            await _db.MettreAJourScoreAsync(_partieId, 1, plateau.ScoreJ1);
            await _db.MettreAJourScoreAsync(_partieId, 2, plateau.ScoreJ2);
            LabelMessage.Text = "✓ Partie sauvegardée.";
        }
        catch (Exception ex)
        {
            LabelMessage.Text = $"Erreur sauvegarde : {ex.Message}";
        }
    }

    private async Task ChargerAsync()
    {
        if (!await _db.TesterConnexionAsync())
        {
            LabelMessage.Text = "⚠ Base de données non disponible.";
            return;
        }

        try
        {
            if (_partieId < 0)
            {
                LabelMessage.Text = "Aucune partie à charger.";
                return;
            }

            var plateau = MonPlateau.ObtenirPlateau();
            await _db.ChargerEtatAsync(_partieId, plateau);
            MonPlateau.RefreshVues();
            LabelMessage.Text = "✓ Partie chargée.";
        }
        catch (Exception ex)
        {
            LabelMessage.Text = $"Erreur chargement : {ex.Message}";
        }
    }

    private async Task VerifierConnexionDbAsync()
    {
        bool ok = await _db.TesterConnexionAsync();
        LabelDB.Text = ok ? "● DB: connectée" : "● DB: non connectée";
        LabelDB.Foreground = ok
            ? Avalonia.Media.Brushes.MediumSeaGreen
            : Avalonia.Media.Brushes.DimGray;

        if (ok)
        {
            try { await _db.InitialiserBaseDeDonneesAsync(); }
            catch { /* schéma déjà créé ou erreur ignorée */ }
        }
    }
}
