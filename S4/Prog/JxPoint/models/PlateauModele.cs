using System;
using System.Collections.Generic;
using System.Linq;

namespace JxPoint.Models;

public class PlateauModele
{
    public int Lignes    { get; }
    public int Colonnes  { get; }
    public int[,] Etat   { get; }
    public int JoueurActuel { get; private set; } = 1;
    public string NomJoueur1 { get; set; } = "Joueur 1";
    public string NomJoueur2 { get; set; } = "Joueur 2";

    public PlateauModele(int lignes, int colonnes)
    {
        Lignes   = lignes;
        Colonnes = colonnes;
        Etat     = new int[colonnes + 1, lignes + 1];
    }

    public string GetNomJoueur(int joueur) => joueur == 1 ? NomJoueur1 : NomJoueur2;

    public Dictionary<(int col, int row), int> PointsDetruits { get; } = new();
    public Dictionary<(int col, int row), int> PointsTiresPar { get; } = new();

    public int CompteurPoints(int joueur)
    {
        var count = 0;
        for (int c = 0; c <= Colonnes; c++)
            for (int r = 0; r <= Lignes; r++)
                if (Etat[c, r] == joueur) count++;
        return count;
    }

    public bool EstPlein()
    {
        int total = 0;
        for (int c = 0; c <= Colonnes; c++)
            for (int r = 0; r <= Lignes; r++)
                if (Etat[c, r] != 0) total++;

        return total >= (Colonnes + 1) * (Lignes + 1);
    }

    // Retourne vrai si le placement est valide
    public bool PlacerPoint(int col, int row)
    {
        if (Etat[col, row] != 0) return false;

        Etat[col, row] = JoueurActuel;
        JoueurActuel   = (JoueurActuel == 1) ? 2 : 1;
        return true;
    }

    // Vérifie si le joueur vient d'aligner exactement 5
    public bool VerifierVictoire(int col, int row, int joueur)
    {
        var directions = new (int dc, int dr)[]
        {
            (1,  0),   // horizontal
            (0,  1),   // vertical
            (1,  1),   // diagonale ↘
            (1, -1),   // diagonale ↗
        };

        foreach (var (dc, dr) in directions)
        {
            int compte = 1;
            compte += CompterDansDirection(col, row, joueur, +dc, +dr);
            compte += CompterDansDirection(col, row, joueur, -dc, -dr);

            if (compte == 5) return true;
        }
        return false;
    }

    private static readonly (int dc, int dr)[] Directions =
    {
        (1,  0),   // horizontal
        (0,  1),   // vertical
        (1,  1),   // diagonale ↘
        (1, -1),   // diagonale ↗
    };

    private IEnumerable<(int col, int row)[]> TrouverSegmentsDe5(int joueur)
    {
        var lignes = new List<(int col, int row)[]>();

        foreach (var (dc, dr) in Directions)
        {
            for (int col = 0; col <= Colonnes; col++)
            {
                for (int row = 0; row <= Lignes; row++)
                {
                    if (Etat[col, row] != joueur) continue;

                    int prevCol = col - dc;
                    int prevRow = row - dr;
                    if (prevCol >= 0 && prevCol <= Colonnes && prevRow >= 0 && prevRow <= Lignes && Etat[prevCol, prevRow] == joueur)
                        continue; // déjà couvert par segment précédent

                    // parcours du run courant
                    var run = new List<(int col, int row)>();
                    int c = col, r = row;
                    while (c >= 0 && c <= Colonnes && r >= 0 && r <= Lignes && Etat[c, r] == joueur)
                    {
                        run.Add((c, r));
                        c += dc;
                        r += dr;
                    }

                    if (run.Count >= 5)
                    {
                        for (int start = 0; start <= run.Count - 5; start += 4)
                        {
                            var seg = run.GetRange(start, 5).ToArray();
                            lignes.Add(seg);
                        }
                    }
                }
            }
        }

        return lignes;
    }

    public (bool victoire, (int col, int row)[] ligne) VerifierVictoireAvecLigne(int col, int row, int joueur)
    {
        foreach (var segment in TrouverSegmentsDe5(joueur))
        {
            if (segment.Any(p => p.col == col && p.row == row))
                return (true, segment);
        }

        return (false, Array.Empty<(int col, int row)>());
    }

    public int CompterAlignements(int joueur)
    {
        var lignes = new HashSet<string>();

        foreach (var segment in TrouverSegmentsDe5(joueur))
        {
            var debut = segment.First();
            var fin = segment.Last();
            var key = $"{debut.col},{debut.row}-{fin.col},{fin.row}";

            if (!lignes.Contains(key))
                lignes.Add(key);
        }

        return lignes.Count;
    }

    public bool EstPointDansAlignement5(int col, int row)
    {
        if (col < 0 || col > Colonnes || row < 0 || row > Lignes) return false;
        var joueur = Etat[col, row];
        if (joueur == 0) return false;

        var (victoire, ligne) = VerifierVictoireAvecLigne(col, row, joueur);
        return victoire;
    }

    private bool EstAlignementDeN(int col, int row, int joueur, int n)
    {
        foreach (var (dc, dr) in Directions)
        {
            int count = 1;
            count += CompterDansDirection(col, row, joueur, dc, dr);
            count += CompterDansDirection(col, row, joueur, -dc, -dr);

            if (count == n)
                return true;
        }
        return false;
    }

    public IEnumerable<(int col, int row)> TrouverSuggestionsDe4(int joueur)
    {
        var suggestions = new HashSet<(int col, int row)>();

        for (int c = 0; c <= Colonnes; c++)
        {
            for (int r = 0; r <= Lignes; r++)
            {
                if (Etat[c, r] != 0) continue;

                // Simuler la pose virtuelle à cet emplacement
                Etat[c, r] = joueur;
                bool gagne = VerifierVictoireAvecLigne(c, r, joueur).victoire;
                bool has4 = !gagne && EstAlignementDeN(c, r, joueur, 4);
                Etat[c, r] = 0;

                if (gagne || has4)
                {
                    suggestions.Add((c, r));
                }
            }
        }

        return suggestions;
    }

    public IEnumerable<(int col, int row)> TrouverSuggestionsDe3(int joueur)
    {
        var suggestions = new HashSet<(int col, int row)>();

        foreach (var (dc, dr) in Directions)
        {
            for (int col = 0; col <= Colonnes; col++)
            {
                for (int row = 0; row <= Lignes; row++)
                {
                    if (Etat[col, row] != joueur) continue;

                    int prevCol = col - dc;
                    int prevRow = row - dr;
                    if (prevCol >= 0 && prevCol <= Colonnes && prevRow >= 0 && prevRow <= Lignes && Etat[prevCol, prevRow] == joueur)
                        continue; // ce point est interne à un run déjà géré

                    // compter la longueur du run
                    int c = col;
                    int r = row;
                    int run = 0;
                    while (c >= 0 && c <= Colonnes && r >= 0 && r <= Lignes && Etat[c, r] == joueur)
                    {
                        run++;
                        c += dc;
                        r += dr;
                    }

                    if (run != 3) continue;

                    // candidate avant
                    int beforeCol = col - dc;
                    int beforeRow = row - dr;
                    if (beforeCol >= 0 && beforeCol <= Colonnes && beforeRow >= 0 && beforeRow <= Lignes && Etat[beforeCol, beforeRow] == 0)
                    {
                        suggestions.Add((beforeCol, beforeRow));
                    }

                    // candidate après
                    int afterCol = col + dc * run;
                    int afterRow = row + dr * run;
                    if (afterCol >= 0 && afterCol <= Colonnes && afterRow >= 0 && afterRow <= Lignes && Etat[afterCol, afterRow] == 0)
                    {
                        suggestions.Add((afterCol, afterRow));
                    }
                }
            }
        }

        return suggestions;
    }

    private int CompterDansDirection(int col, int row, int joueur, int dc, int dr)
    {
        int compte = 0;
        int c = col + dc;
        int r = row + dr;

        while (c >= 0 && c <= Colonnes &&
               r >= 0 && r <= Lignes   &&
               Etat[c, r] == joueur)
        {
            compte++;
            c += dc;
            r += dr;
        }
        return compte;
    }

    public void PasserTour()
    {
        JoueurActuel = (JoueurActuel == 1) ? 2 : 1;
    }


}