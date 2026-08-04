using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Shapes;
using Avalonia.Input;
using Avalonia.Media;
using Avalonia.Threading;
using JxPoint.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace JxPoint.Views;

public partial class Plateaux : UserControl
{
    // ─── Modèle ───────────────────────────────────────────────────────────────
    private TourModele _tour = null!;
    private int _lignes = 8;
    private int _colonnes = 8;
    private double _tailleCase = 56;

    // ─── Vues ─────────────────────────────────────────────────────────────────
    private Ellipse[,] _vues = null!;
    private Canvas _canvas = null!;
    private Rectangle _bazookaShapeJ1 = null!;
    private Rectangle _bazookaShapeJ2 = null!;
    private Polyline? _trajectoireVisu = null;
    private readonly List<Polyline> _lignesVictoire = new();

    // ─── Animation balle ──────────────────────────────────────────────────────
    private DispatcherTimer? _animTimer;
    private List<(int col, int row)>? _animPoints;
    private int _animIndex;
    private Ellipse? _balle;
    private int _animTireur;
    private bool _enAnimation;

    // ─── Événements vers MainWindow ───────────────────────────────────────────
    public event Action<int, int>? OnScoreChanged;
    public event Action<string>? OnMessageChanged;
    public event Action<int, string, string>? OnTourChanged;

    private readonly string[] _noms = { "", "Joueur 1", "Joueur 2" };

    // ─── Initialisation ───────────────────────────────────────────────────────
    public Plateaux()
    {
        InitializeComponent();
        _vues = new Ellipse[0, 0];
        _canvas = new Canvas();
        _bazookaShapeJ1 = new Rectangle();
        _bazookaShapeJ2 = new Rectangle();

        InitialiserJeu(_lignes, _colonnes);
        BrancherBoutons();
    }

    private void InitialiserJeu(int lignes, int colonnes)
    {
        _lignes = lignes;
        _colonnes = colonnes;
        _tour = new TourModele(lignes, colonnes);
        _vues = new Ellipse[colonnes + 1, lignes + 1];
        _lignesVictoire.Clear();

        _canvas = new Canvas
        {
            Width = colonnes * _tailleCase,
            Height = lignes * _tailleCase
        };

        DessinerGrille();
        CreerSommets();
        CreerBazookaShapes();

        Grille.Children.Clear();
        Grille.Children.Add(_canvas);

        J1PositionSlider.Minimum = 0;
        J1PositionSlider.Maximum = lignes;
        J1PositionSlider.Value = _tour.BazookaJ1.PositionRow;
        J2PositionSlider.Minimum = 0;
        J2PositionSlider.Maximum = lignes;
        J2PositionSlider.Value = _tour.BazookaJ2.PositionRow;

        DessinerBazookas();
        MettreAJourLabels("Partie démarrée — Joueur 1 commence !");
    }

    private void BrancherBoutons()
    {
        J1PositionSlider.PropertyChanged += (_, e) =>
        {
            if (e.Property.Name != "Value") return;
            J1PosLabel.Text = ((int)J1PositionSlider.Value).ToString();
        };
        J2PositionSlider.PropertyChanged += (_, e) =>
        {
            if (e.Property.Name != "Value") return;
            J2PosLabel.Text = ((int)J2PositionSlider.Value).ToString();
        };
        J1VitesseSlider.PropertyChanged += (_, e) =>
        {
            if (e.Property.Name != "Value") return;
            J1VitesseLabel.Text = ((int)J1VitesseSlider.Value).ToString();
        };
        J2VitesseSlider.PropertyChanged += (_, e) =>
        {
            if (e.Property.Name != "Value") return;
            J2VitesseLabel.Text = ((int)J2VitesseSlider.Value).ToString();
        };

        J1TirerBtn.Click += (_, _) => ActionBazooka(1);
        J2TirerBtn.Click += (_, _) => ActionBazooka(2);
        J1TrajectoireBtn.Click += (_, _) => AfficherTrajectoire(1);
        J2TrajectoireBtn.Click += (_, _) => AfficherTrajectoire(2);
    }

    // ─── Dessin grille ────────────────────────────────────────────────────────
    private void DessinerGrille()
    {
        _canvas.Children.Clear();

        for (int row = 0; row <= _lignes; row++)
        {
            _canvas.Children.Add(new Line
            {
                StartPoint = new Point(0, row * _tailleCase),
                EndPoint = new Point(_colonnes * _tailleCase, row * _tailleCase),
                Stroke = new SolidColorBrush(Color.Parse("#2a2a38")),
                StrokeThickness = 1
            });
        }
        for (int col = 0; col <= _colonnes; col++)
        {
            _canvas.Children.Add(new Line
            {
                StartPoint = new Point(col * _tailleCase, 0),
                EndPoint = new Point(col * _tailleCase, _lignes * _tailleCase),
                Stroke = new SolidColorBrush(Color.Parse("#2a2a38")),
                StrokeThickness = 1
            });
        }
    }

    // ─── Création des sommets cliquables ──────────────────────────────────────
    private void CreerSommets()
    {
        for (int col = 0; col <= _colonnes; col++)
        {
            for (int row = 0; row <= _lignes; row++)
            {
                var sommet = new Ellipse
                {
                    Width = 14,
                    Height = 14,
                    Fill = new SolidColorBrush(Color.Parse("#333344")),
                    Stroke = new SolidColorBrush(Color.Parse("#555566")),
                    StrokeThickness = 1,
                    Cursor = new Cursor(StandardCursorType.Hand)
                };

                Canvas.SetLeft(sommet, col * _tailleCase - 7);
                Canvas.SetTop(sommet, row * _tailleCase - 7);
                _vues[col, row] = sommet;

                int c = col, r = row;
                sommet.PointerEntered += (_, _) =>
                {
                    if (_tour.Plateau.Etat[c, r] == 0 && !_enAnimation)
                        sommet.Fill = new SolidColorBrush(
                            _tour.Plateau.JoueurActuel == 1
                                ? Color.Parse("#1D4ED8")
                                : Color.Parse("#B91C1C"));
                };
                sommet.PointerExited += (_, _) =>
                {
                    if (_tour.Plateau.Etat[c, r] == 0)
                        sommet.Fill = new SolidColorBrush(Color.Parse("#333344"));
                };
                sommet.PointerPressed += (_, _) => OnSommetClique(c, r);

                _canvas.Children.Add(sommet);
            }
        }
    }

    private void OnSommetClique(int col, int row)
    {
        if (_enAnimation) return;
        int joueur = _tour.Plateau.JoueurActuel;

        // Déplace d'abord le bazooka si le slider a changé
        var slider = joueur == 1 ? J1PositionSlider : J2PositionSlider;
        int nouvPos = (int)slider.Value;
        var bazooka = joueur == 1 ? _tour.BazookaJ1 : _tour.BazookaJ2;
        if (nouvPos != bazooka.PositionRow)
            bazooka.DeplacerVers(nouvPos);

        if (!_tour.PlacerPoint(col, row))
        {
            MettreAJourLabels("⚠ Sommet déjà occupé !");
            return;
        }

        _vues[col, row].Fill = CouleurJoueur(joueur);
        _vues[col, row].Width = 18;
        _vues[col, row].Height = 18;
        Canvas.SetLeft(_vues[col, row], col * _tailleCase - 9);
        Canvas.SetTop(_vues[col, row], row * _tailleCase - 9);

        var (victoire, pts) = _tour.Plateau.VerifierVictoireAvecLigne(col, row, joueur);
        if (victoire)
        {
            DessinerLigneVictoire(pts);
            MettreAJourLabels($"🏆 {_noms[joueur]} aligne 5 points !");
        }

        OnScoreChanged?.Invoke(_tour.Plateau.ScoreJ1, _tour.Plateau.ScoreJ2);
        AjouterLog($"{_noms[joueur]} place un point en ({col},{row})");
        MettreAJourTourUI();

        if (victoire)
            MettreAJourLabels($"🏆 {_noms[joueur]} aligne 5 points ! Score mis à jour.");
        else
            MettreAJourLabels($"{_noms[_tour.Plateau.JoueurActuel]} joue.");
    }

    // ─── Bazooka ──────────────────────────────────────────────────────────────
    private void CreerBazookaShapes()
    {
        _bazookaShapeJ1 = new Rectangle
        {
            Width = 10, Height = 28,
            Fill = new SolidColorBrush(Color.Parse("#3B82F6")),
            RadiusX = 3, RadiusY = 3
        };
        _bazookaShapeJ2 = new Rectangle
        {
            Width = 10, Height = 28,
            Fill = new SolidColorBrush(Color.Parse("#EF4444")),
            RadiusX = 3, RadiusY = 3
        };
        _canvas.Children.Add(_bazookaShapeJ1);
        _canvas.Children.Add(_bazookaShapeJ2);
    }

    private void DessinerBazookas()
    {
        PositionnerBazooka(_bazookaShapeJ1, _tour.BazookaJ1, estDroite: true);
        PositionnerBazooka(_bazookaShapeJ2, _tour.BazookaJ2, estDroite: false);
    }

    private void PositionnerBazooka(Rectangle forme, BazookaModele baz, bool estDroite)
    {
        double x = estDroite
            ? _colonnes * _tailleCase + 6
            : -forme.Width - 6;
        double y = baz.PositionRow * _tailleCase - forme.Height / 2;
        Canvas.SetLeft(forme, x);
        Canvas.SetTop(forme, Math.Clamp(y, 0, _lignes * _tailleCase - forme.Height));
        Canvas.SetZIndex(forme, 10);
    }

    private void ActionBazooka(int joueur)
    {
        if (_enAnimation) return;
        if (_tour.Plateau.JoueurActuel != joueur)
        {
            MettreAJourLabels($"⚠ C'est au tour du {_noms[_tour.Plateau.JoueurActuel]} !");
            return;
        }

        // Sync position slider → bazooka
        var slider = joueur == 1 ? J1PositionSlider : J2PositionSlider;
        int vitesse = (int)(joueur == 1 ? J1VitesseSlider.Value : J2VitesseSlider.Value);
        var bazooka = joueur == 1 ? _tour.BazookaJ1 : _tour.BazookaJ2;
        bazooka.DeplacerVers((int)slider.Value);
        DessinerBazookas();

        // Calculer trajectoire avant le tir (pour animation)
        var trajectoire = bazooka.CalculerTrajectoire(vitesse);

        // Démarrer animation
        EffacerTrajectoireVisu();
        AnimerBalle(joueur, trajectoire, vitesse);
    }

    // ─── Animation de la balle ───────────────────────────────────────────────
    private void AnimerBalle(int tireur, List<(int col, int row)> pts, int vitesse)
    {
        _enAnimation = true;
        _animPoints = pts;
        _animIndex = 0;
        _animTireur = tireur;

        _balle = new Ellipse
        {
            Width = 10, Height = 10,
            Fill = tireur == 1
                ? new SolidColorBrush(Color.Parse("#93C5FD"))
                : new SolidColorBrush(Color.Parse("#FCA5A5")),
        };
        _canvas.Children.Add(_balle);
        Canvas.SetZIndex(_balle, 20);

        // Tracer la courbe en pointillé
        var poly = new Polyline
        {
            Stroke = tireur == 1
                ? new SolidColorBrush(Color.Parse("#1D4ED8"))
                : new SolidColorBrush(Color.Parse("#B91C1C")),
            StrokeThickness = 1.5,
            StrokeDashArray = new Avalonia.Collections.AvaloniaList<double> { 4, 4 },
            Opacity = 0.5
        };
        foreach (var p in pts)
            poly.Points.Add(new Point(p.col * _tailleCase, p.row * _tailleCase));
        _canvas.Children.Add(poly);
        _trajectoireVisu = poly;
        Canvas.SetZIndex(poly, 9);

        // Vitesse de l'animation : plus vite = moins de délai
        int delaiMs = Math.Max(8, 30 - vitesse * 2);
        _animTimer = new DispatcherTimer { Interval = TimeSpan.FromMilliseconds(delaiMs) };
        _animTimer.Tick += TickAnimation;
        _animTimer.Start();
    }

    private void TickAnimation(object? s, EventArgs e)
    {
        if (_animPoints == null || _balle == null)
        {
            FinAnimation(null);
            return;
        }

        if (_animIndex >= _animPoints.Count)
        {
            FinAnimation(null);
            return;
        }

        var (col, row) = _animPoints[_animIndex];
        Canvas.SetLeft(_balle, col * _tailleCase - 5);
        Canvas.SetTop(_balle, row * _tailleCase - 5);

        // Vérifier collision
        if (_tour.Plateau.Etat[col, row] != 0 && _animIndex > 0)
        {
            FinAnimation((col, row));
            return;
        }

        _animIndex++;
    }

    private void FinAnimation((int col, int row)? impact)
    {
        _animTimer?.Stop();
        _animTimer = null;

        if (_balle != null)
        {
            _canvas.Children.Remove(_balle);
            _balle = null;
        }

        EffacerTrajectoireVisu();

        if (impact.HasValue)
        {
            var (col, row) = impact.Value;
            int possesseur = _tour.Plateau.Etat[col, row];
            int tireur = _animTireur;

            if (possesseur != 0 && possesseur != tireur)
            {
                // Destruction !
                _tour.Plateau.Etat[col, row] = 0;
                _vues[col, row].Fill = new SolidColorBrush(Color.Parse("#333344"));
                _vues[col, row].Width = 14;
                _vues[col, row].Height = 14;
                Canvas.SetLeft(_vues[col, row], col * _tailleCase - 7);
                Canvas.SetTop(_vues[col, row], row * _tailleCase - 7);

                // Retirer lignes de victoire concernées
                RetirerLignesVictoireImpliquant(col, row);

                AjouterLog($"💥 {_noms[tireur]} détruit le point de {_noms[possesseur]} en ({col},{row}) !");
                MettreAJourLabels($"💥 Point de {_noms[possesseur]} détruit !");
            }
            else if (possesseur == tireur)
            {
                AjouterLog($"Tir de {_noms[tireur]} sur son propre point — aucun effet.");
                MettreAJourLabels("Tir sur son propre point, aucun effet.");
            }
        }
        else
        {
            AjouterLog($"Tir de {_noms[_animTireur]} — raté, aucune cible.");
            MettreAJourLabels("Tir raté.");
        }

        // Passer au tour suivant
        _tour.Plateau.PasserTour();
        OnScoreChanged?.Invoke(_tour.Plateau.ScoreJ1, _tour.Plateau.ScoreJ2);
        MettreAJourTourUI();
        DessinerBazookas();
        _enAnimation = false;
    }

    // ─── Trajectoire visuelle (aperçu) ───────────────────────────────────────
    private void AfficherTrajectoire(int joueur)
    {
        if (_enAnimation) return;
        EffacerTrajectoireVisu();

        var bazooka = joueur == 1 ? _tour.BazookaJ1 : _tour.BazookaJ2;
        var slider = joueur == 1 ? J1PositionSlider : J2PositionSlider;
        bazooka.DeplacerVers((int)slider.Value);

        int vitesse = (int)(joueur == 1 ? J1VitesseSlider.Value : J2VitesseSlider.Value);
        var pts = bazooka.CalculerTrajectoire(vitesse);

        var poly = new Polyline
        {
            Stroke = joueur == 1
                ? new SolidColorBrush(Color.Parse("#3B82F6"))
                : new SolidColorBrush(Color.Parse("#EF4444")),
            StrokeThickness = 1.5,
            StrokeDashArray = new Avalonia.Collections.AvaloniaList<double> { 5, 5 },
            Opacity = 0.6
        };
        foreach (var p in pts)
            poly.Points.Add(new Point(p.col * _tailleCase, p.row * _tailleCase));

        _trajectoireVisu = poly;
        _canvas.Children.Add(poly);
        Canvas.SetZIndex(poly, 8);
    }

    private void EffacerTrajectoireVisu()
    {
        if (_trajectoireVisu != null)
        {
            _canvas.Children.Remove(_trajectoireVisu);
            _trajectoireVisu = null;
        }
    }

    // ─── Lignes de victoire ───────────────────────────────────────────────────
    private void DessinerLigneVictoire((int col, int row)[] points)
    {
        var poly = new Polyline
        {
            Stroke = new SolidColorBrush(Color.Parse("#F5C542")),
            StrokeThickness = 4,
            Opacity = 0.85
        };
        foreach (var p in points)
            poly.Points.Add(new Point(p.col * _tailleCase, p.row * _tailleCase));

        _lignesVictoire.Add(poly);
        _canvas.Children.Add(poly);
        Canvas.SetZIndex(poly, 5);
    }

    private void RetirerLignesVictoireImpliquant(int col, int row)
    {
        var aRetirer = _lignesVictoire
            .Where(p => p.Points.Any(pt =>
                Math.Abs(pt.X - col * _tailleCase) < 1 &&
                Math.Abs(pt.Y - row * _tailleCase) < 1))
            .ToList();

        foreach (var p in aRetirer)
        {
            _canvas.Children.Remove(p);
            _lignesVictoire.Remove(p);
        }
    }

    // ─── UI helpers ───────────────────────────────────────────────────────────
    private void MettreAJourLabels(string message = "")
    {
        OnMessageChanged?.Invoke(message);
    }

    private void MettreAJourTourUI()
    {
        OnTourChanged?.Invoke(_tour.Plateau.JoueurActuel, _noms[1], _noms[2]);

        // Mettre en valeur le panneau du joueur actuel
        bool j1Active = _tour.Plateau.JoueurActuel == 1;
        PanneauJ1.Opacity = j1Active ? 1.0 : 0.45;
        PanneauJ2.Opacity = j1Active ? 0.45 : 1.0;
    }

    private void AjouterLog(string msg)
    {
        LogText.Text = LogText.Text + "\n" + msg;
        // Scroll vers le bas
        Dispatcher.UIThread.Post(() => LogScroll.ScrollToEnd());
    }

    private static IBrush CouleurJoueur(int joueur) => joueur switch
    {
        1 => new SolidColorBrush(Color.Parse("#3B82F6")),
        2 => new SolidColorBrush(Color.Parse("#EF4444")),
        _ => new SolidColorBrush(Color.Parse("#333344"))
    };

    // ─── API publique pour MainWindow ────────────────────────────────────────
    public PlateauModele ObtenirPlateau() => _tour.Plateau;

    public void ReinitialiserJeu()
    {
        _enAnimation = false;
        _animTimer?.Stop();
        _animTimer = null;
        _lignesVictoire.Clear();
        EffacerTrajectoireVisu();
        InitialiserJeu(_lignes, _colonnes);
        MettreAJourTourUI();
        OnScoreChanged?.Invoke(0, 0);
    }

    public void RefreshVues()
    {
        for (int col = 0; col <= _colonnes; col++)
        {
            for (int row = 0; row <= _lignes; row++)
            {
                int val = _tour.Plateau.Etat[col, row];
                _vues[col, row].Fill = CouleurJoueur(val);
                double r = val != 0 ? 9 : 7;
                _vues[col, row].Width = r * 2;
                _vues[col, row].Height = r * 2;
                Canvas.SetLeft(_vues[col, row], col * _tailleCase - r);
                Canvas.SetTop(_vues[col, row], row * _tailleCase - r);
            }
        }
    }
}
