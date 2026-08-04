using System;
using System.Collections.Generic;

namespace JxPoint.Models;

public enum ActionType
{
    Placer,
    DeplacerBazooka,
    Tirer
}

public class TourModele
{
    public PlateauModele Plateau { get; }
    public BazookaModele BazookaJ1 { get; }
    public BazookaModele BazookaJ2 { get; }

    public int ScoreJ1 { get; private set; }
    public int ScoreJ2 { get; private set; }

    public int HitsJ1 { get; private set; }
    public int HitsJ2 { get; private set; }

    public TourModele(int lignes, int colonnes)
    {
        Plateau   = new PlateauModele(lignes, colonnes);
        BazookaJ1 = new BazookaModele(2, lignes / 2, lignes); // J1 gauche
        BazookaJ2 = new BazookaModele(1, lignes / 2, lignes); // J2 droite
    }

    public bool PlacerPoint(int col, int row)
    {
        int joueur = Plateau.JoueurActuel;
        if (!Plateau.PlacerPoint(col, row))
            return false;

        RecalculerScore();
        return true;
    }

    public void RecalculerScore()
    {
        ScoreJ1 = Plateau.CompterAlignements(1);
        ScoreJ2 = Plateau.CompterAlignements(2);
    }

    public void AjouterHit(int joueur)
    {
        if (joueur == 1) HitsJ1++; else HitsJ2++;
    }

    public void EnleverHit(int joueur)
    {
        if (joueur == 1) HitsJ1 = Math.Max(0, HitsJ1 - 1);
        else HitsJ2 = Math.Max(0, HitsJ2 - 1);
    }

    public bool DeplacerBazooka(int joueur, int nouvelleRow)
    {
        var bazooka = joueur == 1 ? BazookaJ1 : BazookaJ2;
        if (Plateau.JoueurActuel != joueur) return false;

        bazooka.FixerPosition(nouvelleRow);
        Plateau.PasserTour();
        return true;
    }

    public (int col, int row)? Tirer(int vitesse)
    {
        var joueur = Plateau.JoueurActuel;
        var bazooka = joueur == 1 ? BazookaJ1 : BazookaJ2;
        if (bazooka == null) return null;

        var result = bazooka.CalculerTrajectoire(vitesse, Plateau);
        Plateau.PasserTour();
        return result.cible;
    }

    public (List<(double x, double y)> chemin, (int col, int row)? cible) TirerAvecTrajectoire(int vitesse)
    {
        var joueur = Plateau.JoueurActuel;
        var bazooka = joueur == 1 ? BazookaJ1 : BazookaJ2;
        if (bazooka == null) return (new List<(double x, double y)>(), null);

        var result = bazooka.CalculerTrajectoire(vitesse, Plateau);
        Plateau.PasserTour();
        return (result.path, result.cible);
    }
}
