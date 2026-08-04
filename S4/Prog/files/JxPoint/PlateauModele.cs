namespace JxPoint.Models;

public class PlateauModele
{
    public int Lignes { get; }
    public int Colonnes { get; }
    public int[,] Etat { get; }
    public int JoueurActuel { get; private set; } = 1;
    public int ScoreJ1 { get; private set; } = 0;
    public int ScoreJ2 { get; private set; } = 0;

    public PlateauModele(int lignes, int colonnes)
    {
        Lignes = lignes;
        Colonnes = colonnes;
        Etat = new int[colonnes + 1, lignes + 1];
    }

    public bool PlacerPoint(int col, int row)
    {
        if (col < 0 || col > Colonnes || row < 0 || row > Lignes) return false;
        if (Etat[col, row] != 0) return false;
        Etat[col, row] = JoueurActuel;
        return true;
    }

    public void PasserTour()
    {
        JoueurActuel = JoueurActuel == 1 ? 2 : 1;
    }

    public void AjouterScore(int joueur, int points = 1)
    {
        if (joueur == 1) ScoreJ1 += points;
        else if (joueur == 2) ScoreJ2 += points;
    }

    public bool VerifierVictoire(int col, int row, int joueur)
    {
        return VerifierVictoireAvecLigne(col, row, joueur).victoire;
    }

    public (bool victoire, (int col, int row)[] points) VerifierVictoireAvecLigne(int col, int row, int joueur)
    {
        (int dc, int dr)[] directions = { (1, 0), (0, 1), (1, 1), (1, -1) };

        foreach (var (dc, dr) in directions)
        {
            var pts = CollecterLigne(col, row, dc, dr, joueur);
            if (pts.Count >= 5)
                return (true, pts.GetRange(0, 5).ToArray());
        }

        return (false, Array.Empty<(int, int)>());
    }

    private List<(int col, int row)> CollecterLigne(int col, int row, int dc, int dr, int joueur)
    {
        var pts = new List<(int, int)>();

        // reculer jusqu'au début de la séquence
        int sc = col - dc * 4, sr = row - dr * 4;
        for (int i = -4; i <= 4; i++)
        {
            int c = col + dc * i;
            int r = row + dr * i;
            if (c < 0 || c > Colonnes || r < 0 || r > Lignes) continue;
            if (Etat[c, r] == joueur)
                pts.Add((c, r));
            else
                pts.Clear();

            if (pts.Count >= 5) break;
        }

        return pts;
    }

    public void SupprimerPoint(int col, int row)
    {
        if (col >= 0 && col <= Colonnes && row >= 0 && row <= Lignes)
            Etat[col, row] = 0;
    }

    public void Reinitialiser()
    {
        for (int c = 0; c <= Colonnes; c++)
            for (int r = 0; r <= Lignes; r++)
                Etat[c, r] = 0;
        JoueurActuel = 1;
        ScoreJ1 = 0;
        ScoreJ2 = 0;
    }
}
