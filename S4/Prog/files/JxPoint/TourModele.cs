namespace JxPoint.Models;

public enum ActionTour { Aucune, PlacerPoint, DeplacerBazooka, Tirer }

public class TourModele
{
    public PlateauModele Plateau { get; }
    public BazookaModele BazookaJ1 { get; }
    public BazookaModele BazookaJ2 { get; }

    public int ScoreJ1 => Plateau.ScoreJ1;
    public int ScoreJ2 => Plateau.ScoreJ2;

    public ActionTour DerniereAction { get; private set; } = ActionTour.Aucune;

    public TourModele(int lignes, int colonnes)
    {
        Plateau = new PlateauModele(lignes, colonnes);
        BazookaJ1 = new BazookaModele(1, lignes, colonnes);
        BazookaJ2 = new BazookaModele(2, lignes, colonnes);
    }

    /// <summary>Place un point pour le joueur actuel et passe le tour.</summary>
    public bool PlacerPoint(int col, int row)
    {
        if (!Plateau.PlacerPoint(col, row)) return false;
        DerniereAction = ActionTour.PlacerPoint;

        var (victoire, _) = Plateau.VerifierVictoireAvecLigne(col, row, Plateau.JoueurActuel);
        if (victoire)
            Plateau.AjouterScore(Plateau.JoueurActuel);

        Plateau.PasserTour();
        return true;
    }

    /// <summary>Déplace le bazooka du joueur vers une nouvelle ligne.</summary>
    public bool DeplacerBazooka(int joueur, int nouvelleRow)
    {
        if (Plateau.JoueurActuel != joueur) return false;

        if (joueur == 1) BazookaJ1.DeplacerVers(nouvelleRow);
        else BazookaJ2.DeplacerVers(nouvelleRow);

        DerniereAction = ActionTour.DeplacerBazooka;
        Plateau.PasserTour();
        return true;
    }

    /// <summary>
    /// Le joueur actuel tire avec son bazooka.
    /// Retourne le sommet touché ou null.
    /// </summary>
    public (int col, int row)? Tirer(int vitesse)
    {
        var bazooka = Plateau.JoueurActuel == 1 ? BazookaJ1 : BazookaJ2;
        var cible = bazooka.Tirer(vitesse, Plateau);

        DerniereAction = ActionTour.Tirer;

        if (cible.HasValue)
        {
            var (col, row) = cible.Value;
            int possesseur = Plateau.Etat[col, row];
            int tireur = Plateau.JoueurActuel;

            // Supprime seulement si c'est un point adverse
            if (possesseur != 0 && possesseur != tireur)
                Plateau.SupprimerPoint(col, row);
        }

        Plateau.PasserTour();
        return cible;
    }

    public List<(int col, int row)> ObtenirTrajectoire(int vitesse)
    {
        var bazooka = Plateau.JoueurActuel == 1 ? BazookaJ1 : BazookaJ2;
        return bazooka.CalculerTrajectoire(vitesse);
    }

    public void Reinitialiser()
    {
        Plateau.Reinitialiser();
        DerniereAction = ActionTour.Aucune;
    }
}
