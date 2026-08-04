namespace JxPoint.Models;

public class BazookaModele
{
    public int Joueur { get; }
    public int PositionRow { get; private set; }
    public int Lignes { get; }
    public int Colonnes { get; }

    // Joueur 1 : droite → tire vers la gauche (col décroissant)
    // Joueur 2 : gauche → tire vers la droite (col croissant)
    private bool TireVersGauche => Joueur == 1;

    public BazookaModele(int joueur, int lignes, int colonnes)
    {
        Joueur = joueur;
        Lignes = lignes;
        Colonnes = colonnes;
        PositionRow = lignes / 2;
    }

    public void DeplacerVersHaut()
    {
        if (PositionRow > 0) PositionRow--;
    }

    public void DeplacerVersBas()
    {
        if (PositionRow < Lignes) PositionRow++;
    }

    public void DeplacerVers(int row)
    {
        PositionRow = Math.Clamp(row, 0, Lignes);
    }

    /// <summary>
    /// Calcule la trajectoire parabolique de la balle.
    /// Vitesse 1 = arc très courbé (fort déplacement vertical)
    /// Vitesse 9 = trajectoire quasi plate
    /// Retourne la liste de sommets (col, row) traversés.
    /// </summary>
    public List<(int col, int row)> CalculerTrajectoire(int vitesse)
    {
        vitesse = Math.Clamp(vitesse, 1, 9);

        // Le bazooka couvre toute la largeur du plateau
        // x normalisé : 0 → 1 (de la source vers l'opposé)
        // y = a * x * (x - 1) → parabole concave vers le bas (arc)
        // Plus vitesse est faible, plus a est grand (arc prononcé)
        double a = (10 - vitesse) * 0.8; // vitesse 1 → a=7.2 (très courbé), vitesse 9 → a=0.8 (plat)

        var pts = new List<(int col, int row)>();
        int steps = Colonnes * 4; // résolution fine

        for (int i = 0; i <= steps; i++)
        {
            double xn = (double)i / steps;  // 0 à 1

            // déplacement vertical en nombre de cases
            double deltaRow = a * xn * (xn - 1) * Colonnes;

            double rowD = PositionRow + deltaRow;
            int col = TireVersGauche
                ? Colonnes - (int)Math.Round(xn * Colonnes)
                : (int)Math.Round(xn * Colonnes);

            int row = (int)Math.Round(rowD);

            if (col < 0 || col > Colonnes || row < 0 || row > Lignes) continue;

            var pt = (col, row);
            if (pts.Count == 0 || pts[^1] != pt)
                pts.Add(pt);
        }

        return pts;
    }

    /// <summary>
    /// Tire et retourne le premier sommet adverse touché, ou null.
    /// </summary>
    public (int col, int row)? Tirer(int vitesse, PlateauModele plateau)
    {
        var trajectoire = CalculerTrajectoire(vitesse);

        foreach (var (col, row) in trajectoire)
        {
            int occupant = plateau.Etat[col, row];
            if (occupant != 0)
                return (col, row);
        }

        return null;
    }
}
