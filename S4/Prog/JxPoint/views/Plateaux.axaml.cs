using Avalonia;
using Avalonia.Controls;
using Avalonia.Controls.Primitives;
using Avalonia.Controls.Shapes;
using Avalonia.Input;
using Avalonia.Interactivity;
using Avalonia.Media;
using JxPoint.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace JxPoint.Views;

public partial class Plateaux : UserControl
{
    private enum ActionMode { None, Place, Shoot }

    private TourModele _tour = null!;
    private Ellipse[,] _points = null!;
    private readonly DatabaseService _databaseService;
    private ActionMode _activeAction = ActionMode.None;
    private List<(int col, int row)> _suggestions4 = new();
    private List<(int col, int row)> _suggestions3 = new();

    private const double CanvasMargin = 24;
    private const double CellSize = 56;
    private int _taille;

    public Plateaux()
    {
        InitializeComponent();

        // Gestion clavier pour tir
        this.KeyDown += OnKeyDown;
        this.Focusable = true;
        this.AttachedToVisualTree += (_, __) => this.Focus();

        // Configuration PostgreSQL (adapter selon votre environnement)
        // Vérifier que la base existe : psql -U postgres -l
        // Si elle s'appelle "jxpoint" en minuscules, mettre Database=jxpoint
        var connectionString = "Host=localhost;Port=5432;Username=postgres;Password=postgres;Database=jxpoint";
        _databaseService = new DatabaseService(connectionString);

        ShowTitleScreen();
        _ = ChargerHistoriqueAsync();
    }

    private void ShowTitleScreen()
    {
        ScreenTitle.IsVisible = true;
        ScreenGame.IsVisible = false;
        ScreenEnd.IsVisible = false;
    }

    private void ShowGameScreen()
    {
        ScreenTitle.IsVisible = false;
        ScreenGame.IsVisible = true;
        ScreenEnd.IsVisible = false;
    }

    private void ShowEndScreen(string texte, string scores)
    {
        WinnerTitle.Text = texte;
        WinnerName.Text = "";
        EndScores.Text = scores;
        ScreenTitle.IsVisible = false;
        ScreenGame.IsVisible = false;
        ScreenEnd.IsVisible = true;
    }

    private void OnStartGame(object? sender, RoutedEventArgs e)
    {
        _taille = int.Parse((InputSize.SelectedItem as ComboBoxItem)?.Content?.ToString() ?? "10");
        var nomJ1 = string.IsNullOrWhiteSpace(InputJ1.Text) ? "Joueur 1" : InputJ1.Text.Trim();
        var nomJ2 = string.IsNullOrWhiteSpace(InputJ2.Text) ? "Joueur 2" : InputJ2.Text.Trim();

        _tour = new TourModele(_taille, _taille);
        _tour.Plateau.NomJoueur1 = nomJ1;
        _tour.Plateau.NomJoueur2 = nomJ2;

        InitialiseCanvas();
        UpdateUI("Prêt, placez un point ou tirez.");
        AddLog("sys", $"Partie démarrée : {nomJ1} vs {nomJ2} ({_taille}x{_taille})");

        _activeAction = ActionMode.None;

        ShowGameScreen();
    }

    private void InitialiseCanvas()
    {
        GameCanvas.Children.Clear();

        // Assurez la réactivité sur tous les clics du canevas
        GameCanvas.PointerPressed -= OnCanvasPointerPressed;
        GameCanvas.PointerPressed += OnCanvasPointerPressed;

        double size = _taille * CellSize + 2 * CanvasMargin;
        GameCanvas.Width = size;
        GameCanvas.Height = size;

        // stocks points d'affichage
        _points = new Ellipse[_taille + 1, _taille + 1];

        DrawGrid();
        DrawPoints();

        SpeedVal1.Text = Speed1.Value.ToString("0");
        SpeedVal2.Text = Speed2.Value.ToString("0");
        UpdateBazookaUI();
        UpdateScoreStats();
    }

    private void DrawGrid()
    {
        for (int i = 0; i <= _taille; i++)
        {
            var lineH = new Line
            {
                StartPoint = new Point(CanvasMargin, CanvasMargin + i * CellSize),
                EndPoint = new Point(CanvasMargin + _taille * CellSize, CanvasMargin + i * CellSize),
                Stroke = new SolidColorBrush(Color.Parse("#333")),
                StrokeThickness = 1
            };
            GameCanvas.Children.Add(lineH);

            var lineV = new Line
            {
                StartPoint = new Point(CanvasMargin + i * CellSize, CanvasMargin),
                EndPoint = new Point(CanvasMargin + i * CellSize, CanvasMargin + _taille * CellSize),
                Stroke = new SolidColorBrush(Color.Parse("#333")),
                StrokeThickness = 1
            };
            GameCanvas.Children.Add(lineV);
        }
    }

    private void DrawPoints()
    {
        for (int col = 0; col <= _taille; col++)
        {
            for (int row = 0; row <= _taille; row++)
            {
                var ellipse = new Ellipse
                {
                    Width = 10,
                    Height = 10,
                    Fill = new SolidColorBrush(Color.Parse("#bfbfbf")),
                    Stroke = Brushes.White,
                    StrokeThickness = 0.8,
                    Cursor = new Cursor(StandardCursorType.Hand)
                };

                var x = CanvasMargin + col * CellSize;
                var y = CanvasMargin + row * CellSize;
                Canvas.SetLeft(ellipse, x - ellipse.Width / 2);
                Canvas.SetTop(ellipse, y - ellipse.Height / 2);

                _points[col, row] = ellipse;

                int c = col, r = row;
                ellipse.PointerPressed += (_, _) => OnCanvasPointClick(c, r);
                GameCanvas.Children.Add(ellipse);
            }
        }

        DrawBazooka();
    }

    private void DrawBazooka()
    {
        // Supprime tout élément précédent qui est Rectangle et Tag bazooka
        var toRemove = GameCanvas.Children.
            OfType<Rectangle>().
            Where(r => r.Tag?.ToString()?.StartsWith("baz") == true).
            ToArray();

        foreach (var r in toRemove)
            GameCanvas.Children.Remove(r);

        void DrawOne(BazookaModele b, bool actif)
        {
            var rect = new Rectangle
            {
                Width = 16,
                Height = 16,
                Fill = actif ? Brushes.White : Brushes.Gray,
                Stroke = Brushes.Black,
                StrokeThickness = 1,
                Tag = $"baz{b.Bord}"
            };

            var x = b.Bord == 1
                ? CanvasMargin + _taille * CellSize + 12
                : CanvasMargin - 28;
            var y = CanvasMargin + b.PositionRow * CellSize - 8;

            Canvas.SetLeft(rect, x);
            Canvas.SetTop(rect, y);
            GameCanvas.Children.Add(rect);

            if (b.Bord == 1) BazRow1.Text = $"Ligne : {b.PositionRow}";
            else BazRow2.Text = $"Ligne : {b.PositionRow}";
        }

        DrawOne(_tour.BazookaJ1, _tour.Plateau.JoueurActuel == 1);
        DrawOne(_tour.BazookaJ2, _tour.Plateau.JoueurActuel == 2);
    }

    private void OnCanvasPointClick(int col, int row)
    {
        if (_tour == null) return;
        if (_activeAction != ActionMode.Place)
        {
            AddLog("sys", "Choisissez l’action Placer pour cliquer sur un sommet.");
            return;
        }

        if (_tour.Plateau.Etat[col, row] != 0)
        {
            AddLog("sys", "Case déjà occupée !");
            return;
        }

        var joueur = _tour.Plateau.JoueurActuel;
        _tour.PlacerPoint(col, row);
        _points[col, row].Fill = joueur == 1 ? Brushes.CornflowerBlue : Brushes.OrangeRed;

        var (victoire, ligne) = _tour.Plateau.VerifierVictoireAvecLigne(col, row, joueur);
        if (victoire)
        {
            DrawVictoryLine(ligne);
            AddLog("sys", $"{_tour.Plateau.GetNomJoueur(joueur)} marque un point pour 5 alignés !");
            // continuer le jeu jusqu'à plateau plein
        }

        if (_tour.Plateau.EstPlein())
        {
            string titre;
            if (_tour.ScoreJ1 > _tour.ScoreJ2)
                titre = $"{_tour.Plateau.GetNomJoueur(1)} gagne !";
            else if (_tour.ScoreJ2 > _tour.ScoreJ1)
                titre = $"{_tour.Plateau.GetNomJoueur(2)} gagne !";
            else
                titre = "MATCH NUL";

            AddLog("sys", "Plateau rempli, partie terminée.");
            ShowEndScreen(titre, $"Score: J1 {_tour.ScoreJ1} - J2 {_tour.ScoreJ2}");
            return;
        }

        UpdateUI();
    }

    private void OnCanvasPointerPressed(object? sender, PointerPressedEventArgs e)
    {
        if (_tour == null) return;
        if (_activeAction != ActionMode.Place)
        {
            AddLog("sys", "Choisissez l’action Placer pour cliquer sur un sommet.");
            return;
        }

        var position = e.GetPosition(GameCanvas);
        int col = (int)Math.Round((position.X - CanvasMargin) / CellSize);
        int row = (int)Math.Round((position.Y - CanvasMargin) / CellSize);

        if (col < 0 || col > _taille || row < 0 || row > _taille)
            return;

        OnCanvasPointClick(col, row);
    }

    private void DrawVictoryLine((int col, int row)[] coords)
    {
        var line = new Line
        {
            Stroke = Brushes.Gold,
            StrokeThickness = 3,
            Opacity = 0.8,
            StartPoint = new Point(CanvasMargin + coords.First().col * CellSize, CanvasMargin + coords.First().row * CellSize),
            EndPoint = new Point(CanvasMargin + coords.Last().col * CellSize, CanvasMargin + coords.Last().row * CellSize)
        };

        GameCanvas.Children.Add(line);
    }

    private void UpdateUI(string status = "")
    {
        TurnLabel.Text = $"Tour : {_tour.Plateau.JoueurActuel} - {_tour.Plateau.GetNomJoueur(_tour.Plateau.JoueurActuel)}";
        ScoreLabel.Text = $"J1: {_tour.ScoreJ1} | J2: {_tour.ScoreJ2}";
        LargeScoreLabel.Text = $"Score (grand): J1 {_tour.ScoreJ1} - J2 {_tour.ScoreJ2}";
        ActionHintLabel.Text = _activeAction switch
        {
            ActionMode.Place => "📌 Cliquez sur un sommet pour placer",
            ActionMode.Shoot => "💣 Cliquez sur TIRER pour lancer le bazooka",
            _ => "Sélectionnez une action"
        };

        PlacedJ1.Text = $"Points posés : {_tour.Plateau.CompteurPoints(1)}";
        PlacedJ2.Text = $"Points posés : {_tour.Plateau.CompteurPoints(2)}";
        HitsJ1.Text = $"Tirs réussis : {_tour.HitsJ1}";
        HitsJ2.Text = $"Tirs réussis : {_tour.HitsJ2}";

        DrawBazooka();
        UpdateSuggestions();
    }

    private void UpdateSuggestions()
    {
        if (_tour == null) return;

        _suggestions4 = _tour.Plateau.TrouverSuggestionsDe4(_tour.Plateau.JoueurActuel).ToList();
        _suggestions3 = _tour.Plateau.TrouverSuggestionsDe3(_tour.Plateau.JoueurActuel).ToList();

        if (BtnSuggest != null)
            BtnSuggest.Content = $"📌 Nombre de 4 : {_suggestions4.Count}";

        if (BtnSuggest3 != null)
            BtnSuggest3.Content = $"📌 Nombre de 3 : {_suggestions3.Count}";

        AfficherSuggestions();
    }

    private void AfficherSuggestions()
    {
        if (_points == null) return;

        for (int c = 0; c <= _taille; c++)
        {
            for (int r = 0; r <= _taille; r++)
            {
                var ellipse = _points[c, r];
                if (ellipse == null) continue;

                if (_suggestions4.Contains((c, r)))
                {
                    ellipse.Stroke = Brushes.Lime;
                    ellipse.StrokeThickness = 3;
                }
                else if (_suggestions3.Contains((c, r)))
                {
                    ellipse.Stroke = Brushes.Gold;
                    ellipse.StrokeThickness = 3;
                }
                else
                {
                    ellipse.Stroke = Brushes.White;
                    ellipse.StrokeThickness = 0.8;
                }
            }
        }

        if (SuggestionLabel != null)
            SuggestionLabel.Text = _suggestions4.Count > 0
                ? $"4 points possible : {_suggestions4.Count} ; 3 points : {_suggestions3.Count}"
                : (_suggestions3.Count > 0
                    ? $"3 points possible : {_suggestions3.Count}"
                    : "Aucune suggestion disponible");
    }

    private void OnSelectPlace(object? sender, RoutedEventArgs e)
    {
        _activeAction = ActionMode.Place;
        UpdateUI();
    }

    private void OnSelectShoot(object? sender, RoutedEventArgs e)
    {
        _activeAction = ActionMode.Shoot;
        UpdateUI();
    }

    private void OnSuggest(object? sender, RoutedEventArgs e)
    {
        if (_tour == null) return;

        _suggestions4 = _tour.Plateau.TrouverSuggestionsDe4(_tour.Plateau.JoueurActuel).ToList();
        _suggestions3 = _tour.Plateau.TrouverSuggestionsDe3(_tour.Plateau.JoueurActuel).ToList();
        BtnSuggest.Content = $"📌 Nombre de 4 : {_suggestions4.Count}";
        BtnSuggest3.Content = $"📌 Nombre de 3 : {_suggestions3.Count}";
        AfficherSuggestions();
        AddLog("sys", $"💡 Suggestions générées : {_suggestions4.Count} (4), {_suggestions3.Count} (3) pour { _tour.Plateau.GetNomJoueur(_tour.Plateau.JoueurActuel)}.");
    }

    private void OnBazUp1(object? sender, RoutedEventArgs e)
    {
        if (_tour.Plateau.JoueurActuel != 1) { AddLog("sys", "Ce n'est pas votre tour !"); return; }
        _tour.BazookaJ1.DeplacerVersHaut();
        UpdateUI();
    }

    private void OnBazDown1(object? sender, RoutedEventArgs e)
    {
        if (_tour.Plateau.JoueurActuel != 1) { AddLog("sys", "Ce n'est pas votre tour !"); return; }
        _tour.BazookaJ1.DeplacerVersBas();
        UpdateUI();
    }

    private void OnBazUp2(object? sender, RoutedEventArgs e)
    {
        if (_tour.Plateau.JoueurActuel != 2) { AddLog("sys", "Ce n'est pas votre tour !"); return; }
        _tour.BazookaJ2.DeplacerVersHaut();
        UpdateUI();
    }

    private void OnBazDown2(object? sender, RoutedEventArgs e)
    {
        if (_tour.Plateau.JoueurActuel != 2) { AddLog("sys", "Ce n'est pas votre tour !"); return; }
        _tour.BazookaJ2.DeplacerVersBas();
        UpdateUI();
    }

    private void OnSpeedChanged(object? sender, RangeBaseValueChangedEventArgs e)
    {
        SpeedVal1.Text = ((int)Speed1.Value).ToString();
        SpeedVal2.Text = ((int)Speed2.Value).ToString();
    }

    private async void OnFire1(object? sender, RoutedEventArgs e)
    {
        if (_tour.Plateau.JoueurActuel != 1) { AddLog("sys", "Ce n'est pas votre tour !"); return; }

        if (_activeAction != ActionMode.Shoot) { AddLog("sys", "Sélectionnez Tirer d'abord !"); return; }

        int vit = (int)Speed1.Value;
        var result = _tour.TirerAvecTrajectoire(vit);
        await AnimateProjectileAsync(result.chemin);
        HandleFireResult(result.cible);
        UpdateUI();
    }

    private async void OnFire2(object? sender, RoutedEventArgs e)
    {
        if (_tour.Plateau.JoueurActuel != 2) { AddLog("sys", "Ce n'est pas votre tour !"); return; }

        if (_activeAction != ActionMode.Shoot) { AddLog("sys", "Sélectionnez Tirer d'abord !"); return; }

        int vit = (int)Speed2.Value;
        var result = _tour.TirerAvecTrajectoire(vit);
        await AnimateProjectileAsync(result.chemin);
        HandleFireResult(result.cible);
        UpdateUI();
    }

    private async Task AnimateProjectileAsync(List<(double x, double y)> chemin)
    {
        if (chemin == null || chemin.Count == 0) return;

        var projectile = new Ellipse
        {
            Width = 16,
            Height = 16,
            Fill = Brushes.Yellow,
            Stroke = Brushes.Orange,
            StrokeThickness = 2
        };

        GameCanvas.Children.Add(projectile);
        projectile.SetValue(Canvas.ZIndexProperty, 100);

        foreach (var p in chemin)
        {
            var x = CanvasMargin + p.x * CellSize;
            var y = CanvasMargin + p.y * CellSize;
            Canvas.SetLeft(projectile, x - projectile.Width / 2);
            Canvas.SetTop(projectile, y - projectile.Height / 2);

            await Task.Delay(120);
        }

        GameCanvas.Children.Remove(projectile);
    }

    private void HandleFireResult((int col, int row)? cible)
    {
        if (!cible.HasValue)
        {
            AddLog("sys", "Tir hors plateau.");
            return;
        }

        var (col, row) = cible.Value;
        var owner = _tour.Plateau.Etat[col, row];
        var shooter = _tour.Plateau.JoueurActuel == 1 ? 2 : 1;

        // Cas: point du joueur -> aucun effet
        if (owner == shooter)
        {
            AddLog("sys", "Tir raté — point du tireur touché, aucun effet.");
            return;
        }

        // Réservation de l'historique pour restauration
        var hasHistoric = _tour.Plateau.PointsDetruits.TryGetValue((col, row), out var formerOwner);
        _tour.Plateau.PointsTiresPar.TryGetValue((col, row), out var historicShooter);

        // 1) Si le tireur correspond à l'ancien propriétaire récupère son point (coord vide ou occupé par l'adversaire)
        if (hasHistoric && formerOwner == shooter)
        {
            AddLog("sys", $"🔍 Restaurer possible sur ({col},{row}) pour { _tour.Plateau.GetNomJoueur(shooter)} (historique owner={formerOwner}).");
            if (_tour.Plateau.EstPointDansAlignement5(col, row) && owner != 0)
            {
                AddLog("sys", $"⛔ Point protégé : ({col},{row}) fait partie d'un alignement 5, pas d'effet.");
                return;
            }

            // Si on détruit l'ennemi pour reprendre le point, on retire son hit également
            if (owner != 0 && owner != shooter)
            {
                _tour.EnleverHit(owner);
            }

            // Si on vient d'une restauration sur case vide, on annule le hit du tir précédent
            if (historicShooter != 0)
            {
                _tour.EnleverHit(historicShooter);
            }

            // Restaurer le point du tireur
            _tour.Plateau.Etat[col, row] = shooter;
            _points[col, row].Fill = shooter == 1 ? Brushes.CornflowerBlue : Brushes.OrangeRed;

            // Mémoriser l'ancien occupant (0 si vide, ou le joueur détruit)
            if (owner != 0)
                _tour.Plateau.PointsDetruits[(col, row)] = owner;
            else
                _tour.Plateau.PointsDetruits.Remove((col, row));

            _tour.Plateau.PointsTiresPar[(col, row)] = shooter;

            _tour.AjouterHit(shooter);
            _tour.RecalculerScore();

            // Vérifier alignement 5 au moment de restauration
            var (victoire, ligne) = _tour.Plateau.VerifierVictoireAvecLigne(col, row, shooter);
            if (victoire)
            {
                DrawVictoryLine(ligne);
                AddLog("sys", $"🏅 { _tour.Plateau.GetNomJoueur(shooter)} forme un alignement de 5 en ({col},{row}) et gagne +1.");
            }
            else
            {
                AddLog("sys", $"🔄 { _tour.Plateau.GetNomJoueur(shooter)} récupère son point en ({col},{row}) !");
            }

            return;
        }

        // 2) Tir sur un point adverse normal => suppression et stockage pour récupération ultérieure
        if (owner != 0 && owner != shooter)
        {
            if (_tour.Plateau.EstPointDansAlignement5(col, row))
            {
                AddLog("sys", $"⛔ Point protégé : ({col},{row}) fait partie d'un alignement 5, pas d'effet.");
                return;
            }

            _tour.Plateau.PointsDetruits[(col, row)] = owner;
            _tour.Plateau.PointsTiresPar[(col, row)] = shooter;
            _tour.Plateau.Etat[col, row] = 0;
            _points[col, row].Fill = Brushes.LightGray;
            AddLog("sys", $"💥 { _tour.Plateau.GetNomJoueur(shooter)} a détruit { _tour.Plateau.GetNomJoueur(owner)} en ({col},{row}) !");

            _tour.AjouterHit(shooter);
            _tour.RecalculerScore();
            return;
        }

        // 3) le tir sur un emplacement vide sans historique ou historique autre n'a pas d'effet
        if (owner == 0)
        {
            AddLog("sys", "Tir dans le vide.");
            return;
        }

        AddLog("sys", "Tir sans effet.");
    }

    private void AddLog(string type, string message)
    {
        var text = new TextBlock
        {
            Text = type == "sys" ? $"▸ {message}" : message,
            Foreground = type == "sys" ? Brushes.Gold : Brushes.White,
            FontSize = 12,
            Margin = new Thickness(2, 1)
        };

        LogPanel.Children.Add(text);
        // Keep log short
        while (LogPanel.Children.Count > 20)
            LogPanel.Children.RemoveAt(0);
    }

    private void UpdateBazookaUI()
    {
        BazRow1.Text = $"Ligne : {_tour.BazookaJ1.PositionRow}";
        BazRow2.Text = $"Ligne : {_tour.BazookaJ2.PositionRow}";
    }

    public void HandleKeyShortcut(Avalonia.Input.KeyEventArgs e)
    {
        OnKeyDown(this, e);
    }

    private void OnKeyDown(object? sender, Avalonia.Input.KeyEventArgs e)
    {
        if (e.KeyModifiers.HasFlag(Avalonia.Input.KeyModifiers.Control))
        {
            int power = e.Key switch
            {
                Avalonia.Input.Key.D1 => 1,
                Avalonia.Input.Key.D2 => 2,
                Avalonia.Input.Key.D3 => 3,
                Avalonia.Input.Key.D4 => 4,
                Avalonia.Input.Key.D5 => 5,
                Avalonia.Input.Key.D6 => 6,
                Avalonia.Input.Key.D7 => 7,
                Avalonia.Input.Key.D8 => 8,
                Avalonia.Input.Key.D9 => 9,
                _ => -1
            };

            if (power >= 1 && power <= 9)
            {
                SetPower(power);
                AddLog("sys", $"⚡ Vitesse définie à {power} (Ctrl+{power})");
                e.Handled = true;
                return;
            }
        }

        if (e.Key == Avalonia.Input.Key.Enter)
        {
            ExecuteShoot();
            e.Handled = true;
        }
    }

    private void SetPower(int value)
    {
        if (_tour == null) return;

        if (_tour.Plateau.JoueurActuel == 1)
        {
            Speed1.Value = value;
            SpeedVal1.Text = value.ToString();
        }
        else
        {
            Speed2.Value = value;
            SpeedVal2.Text = value.ToString();
        }
    }

    private void ExecuteShoot()
    {
        if (_tour == null) return;

        if (_activeAction != ActionMode.Shoot)
        {
            _activeAction = ActionMode.Shoot; 
            AddLog("sys", "Mode tir activé (raccourci Entrée).");
            UpdateUI();
        }

        var args = new RoutedEventArgs();
        if (_tour.Plateau.JoueurActuel == 1)
        {
            OnFire1(this, args);
        }
        else
        {
            OnFire2(this, args);
        }
    }

    private void UpdateScoreStats()
    {
        PlacedJ1.Text = $"Points posés : {_tour.Plateau.CompteurPoints(1)}";
        PlacedJ2.Text = $"Points posés : {_tour.Plateau.CompteurPoints(2)}";
        HitsJ1.Text = $"Tirs réussis : {_tour.HitsJ1}";
        HitsJ2.Text = $"Tirs réussis : {_tour.HitsJ2}";
    }

    private async void OnSaveGame(object? sender, RoutedEventArgs e)
    {
        try
        {
            var j1Nom = (InputJ1?.Text ?? "Joueur 1").Trim();
            var j2Nom = (InputJ2?.Text ?? "Joueur 2").Trim();
            var j1Id = await _databaseService.EnregistrerJoueur(string.IsNullOrEmpty(j1Nom) ? "Joueur 1" : j1Nom);
            var j2Id = await _databaseService.EnregistrerJoueur(string.IsNullOrEmpty(j2Nom) ? "Joueur 2" : j2Nom);
            var partieId = await _databaseService.SauvegarderPartie(j1Id, j2Id, null, "en cours");

            // Supprimer anciens sommets de cette partie si réécriture
            await _databaseService.ViderSommets(partieId);

            for (int c = 0; c <= _taille; c++)
            {
                for (int r = 0; r <= _taille; r++)
                {
                    var joueur = _tour.Plateau.Etat[c, r];
                    if (joueur != 0)
                        await _databaseService.EnregistrerSommet(partieId, c, r, joueur);
                }
                
            }

            AddLog("sys", "Partie sauvegardée en base PostgreSQL. ");
             await ChargerHistoriqueAsync();
        }
        catch (Exception ex)
        {
            AddLog("sys", "Erreur sauvegarde : " + ex.Message);
        }
        
    }

    private async void OnLoadGame(object? sender, RoutedEventArgs e)
    {
        try
        {
            var partieId = await _databaseService.ChargerDernierePartieId();
            if (partieId == null)
            {
                AddLog("sys", "Aucune partie à charger.");
                return;
            }

            var sommets = await _databaseService.ChargerSommets(partieId.Value);
            if (sommets.Count == 0)
            {
                AddLog("sys", "Sauvegarde vide ou structure non initialisée.");
                return;
            }

            // reconstruire plateau
            if (_taille <= 0) _taille = 10;
            _tour = new TourModele(_taille, _taille);
            InitializeCanvasFromState(sommets);
            ShowGameScreen();
            UpdateUI("Partie chargée");
            AddLog("sys", "Partie chargée depuis PostgreSQL.");
            await ChargerHistoriqueAsync();
        }
        catch (Exception ex)
        {
            AddLog("sys", "Erreur chargement : " + ex.Message);
        }
    }

    private async Task ChargerHistoriqueAsync()
    {
        try
        {
            var historique = await _databaseService.ChargerHistoriqueParties();
            var items = historique.Select(h => new ListBoxItem
            {
                Content = $"[{h.Date:yyyy-MM-dd HH:mm}] {h.Joueur1} vs {h.Joueur2} ({h.Statut})",
                Tag = h.PartieId
            }).ToList();
            HistoryList.ItemsSource = items;

            if (items.Count > 0)
                HistoryList.SelectedIndex = 0;

        }
        catch (Exception ex)
        {
            AddLog("sys", "Erreur historique : " + ex.Message);
        }
    }

    private void OnHistorySelectionChanged(object? sender, SelectionChangedEventArgs e)
    {
        // Pas d'action nécessaire, on garde la sélection pour charger
    }

    private async void OnLoadSelectedGame(object? sender, RoutedEventArgs e)
    {
        if (HistoryList.SelectedItem is not ListBoxItem item || item.Tag is not int partieId)
        {
            AddLog("sys", "Aucune partie sélectionnée.");
            return;
        }

        try
        {
            var sommets = await _databaseService.ChargerSommets(partieId);
            if (sommets.Count == 0)
            {
                AddLog("sys", "Sauvegarde vide ou structure non initialisée.");
                return;
            }

            if (_taille <= 0) _taille = 10;
            _tour = new TourModele(_taille, _taille);
            InitializeCanvasFromState(sommets);
            ShowGameScreen();
            UpdateUI("Partie chargée");
            AddLog("sys", $"Partie #{partieId} chargée depuis historique.");
        }
        catch (Exception ex)
        {
            AddLog("sys", "Erreur chargement sélectionné : " + ex.Message);
        }
    }

    private void InitializeCanvasFromState(List<(int col, int row, int joueur)> sommets)
    {
        foreach (var point in sommets)
        {
            _tour.Plateau.Etat[point.col, point.row] = point.joueur;
            if (_points != null && point.col <= _taille && point.row <= _taille)
                _points[point.col, point.row].Fill = point.joueur == 1 ? Brushes.CornflowerBlue : Brushes.OrangeRed;
        }

        _tour.RecalculerScore();
        RedrawVictoryLines();
        UpdateUI();
    }

    private void RedrawVictoryLines()
    {
        // On s'appuie sur l'état pour retrouver les 5 alignés exacts et afficher les traits
        var lignesTracees = new HashSet<string>();

        for (int col = 0; col <= _taille; col++)
        {
            for (int row = 0; row <= _taille; row++)
            {
                int joueur = _tour.Plateau.Etat[col, row];
                if (joueur == 0) continue;

                var (victoire, ligne) = _tour.Plateau.VerifierVictoireAvecLigne(col, row, joueur);
                if (!victoire) continue;

                // Clé pour ne pas dessiner deux fois la même ligne (même couple de points extrémités)
                var debut = ligne.First();
                var fin = ligne.Last();
                var key = $"{joueur}:{debut.col},{debut.row}->{fin.col},{fin.row}";
                if (lignesTracees.Contains(key))
                    continue;

                lignesTracees.Add(key);
                DrawVictoryLine(ligne);
            }
        }
    }

    private void OnQuit(object? sender, RoutedEventArgs e)
    {
        ShowEndScreen("Partie terminée", $"Score : J1 {_tour.ScoreJ1} - J2 {_tour.ScoreJ2}");
    }

    private void OnRestart(object? sender, RoutedEventArgs e)
    {
        ShowTitleScreen();
        LogPanel.Children.Clear();
    }
} 
