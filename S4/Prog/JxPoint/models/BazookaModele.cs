using System;
using System.Collections.Generic;
using System.Linq;

namespace JxPoint.Models;

public class BazookaModele
{
    public int Bord { get; }           // 1 = droite (J1), 2 = gauche (J2)
    public int PositionRow { get; private set; }
    public int Lignes { get; }

    public BazookaModele(int bord, int positionInitiale, int lignes)
    {
        if (bord != 1 && bord != 2)
            throw new ArgumentException("Le bord doit être 1 (droite) ou 2 (gauche)", nameof(bord));

        Bord = bord;
        Lignes = lignes;
        PositionRow = Math.Clamp(positionInitiale, 0, lignes);
    }

    public void DeplacerVersHaut() => PositionRow = Math.Max(0, PositionRow - 1);
    public void DeplacerVersBas()  => PositionRow = Math.Min(Lignes, PositionRow + 1);
    public void FixerPosition(int row) => PositionRow = Math.Clamp(row, 0, Lignes);

    // Simule une trajectoire concave en dimension plateau et retourne la trajectoire d'animation (x/y flottants) + le sommet touché éventuel.
    public (List<(double x, double y)> path, (int col, int row)? cible) CalculerTrajectoire(int vitesse, PlateauModele plateau)
    {
        if (vitesse < 1 || vitesse > 9)
            throw new ArgumentOutOfRangeException(nameof(vitesse), "Vitesse entre 1 et 9");

        if (plateau == null) return (new List<(double x, double y)>(), null);

        int maxCol = plateau.Colonnes;
        int maxRow = plateau.Lignes;
        double startCol = Bord == 1 ? maxCol : 0;
        int dir = Bord == 1 ? -1 : +1;

        // Distance relative selon la valeur : 1=proche, 5=mi-distance, 9=longue.
        var ratio = (vitesse - 1) / 8.0; // 0..1
        var stepTarget = 1 + ratio * (maxCol - 1);

        // tracé en arc qui revient à la même ligne de départ (PositionRow) en fin de portée
        var arcMaxHeight = 1.8 * (vitesse / 9.0); // hauteur d'arc relative

        var path = new List<(double x, double y)>();
        (int col, int row)? cible = null;

        int targetRow = Math.Clamp(PositionRow, 0, maxRow); // cible toujours sur la même ligne du bazooka

        const double sampleStep = 0.1;
        double current = 0.0;

        while (current <= stepTarget)
        {
            double gridCol = startCol + dir * current;
            double t = stepTarget > 0 ? current / stepTarget : 0;

            // parabole 0->1->0 en hauteur relative, maximum au milieu
            double arcOffset = -arcMaxHeight * 4 * t * (1 - t);
            double gridRow = Math.Clamp(PositionRow + arcOffset, 0, maxRow);

            path.Add((gridCol, gridRow));

            int colRound = (int)Math.Round(gridCol);
            colRound = Math.Clamp(colRound, 0, maxCol);

            if (cible == null && plateau.Etat[colRound, targetRow] != 0)
            {
                cible = (colRound, targetRow);
                break;
            }

            // si la case est vide mais correspond à un point détruit mémorisé, c'est aussi une cible (restauration logique)
            if (cible == null && plateau.PointsDetruits.ContainsKey((colRound, targetRow)))
            {
                cible = (colRound, targetRow);
                break;
            }

            current += sampleStep;
        }

        if (cible == null)
        {
            // dernier point à la fin de la portée si pas de cible (retour ligne du bazooka)
            double finalCol = startCol + dir * stepTarget;
            double finalRow = Math.Clamp(PositionRow, 0, maxRow);
            path.Add((finalCol, finalRow));
        }

        return (path, cible);
    }

    public (int col, int row)? Tirer(int vitesse, PlateauModele plateau)
    {
        var result = CalculerTrajectoire(vitesse, plateau);
        return result.cible;
    }
}
