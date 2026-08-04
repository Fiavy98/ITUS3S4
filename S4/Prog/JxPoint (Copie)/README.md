# JxPoint

Jeu d'alignement 2 joueurs avec bazooka (Avalonia + PostgreSQL)

## Structure du projet

- `models/`
  - `PlateauModele.cs` : logique plateau, placement, victoire, score
  - `BazookaModele.cs` : position, déplacement, trajectoire, tir
  - `TourModele.cs` : tour par tour
  - `DatabaseService.cs` : accès PostgreSQL (Npgsql)

- `views/`
  - `MainWindow.axaml` : interface principale (Canvas + commandes)
  - `MainWindow.axaml.cs` : code-behind pour événements et rendering
  - `Plateaux.axaml` / `Plateaux.axaml.cs` : placeholder

- `Program.cs`, `App.axaml`, `App.axaml.cs` : démarrage Avalonia

## Fonctionnement

- Grille carrée N+1 sommets (points cliquables)
- 2 joueurs (1 / 2)
- 1 point posé = sommet occupé
- Victoire si alignement 5 points (horizontal / vertical / diagonal)
- Bazooka
  - P1 à droite, P2 à gauche
  - déplace haut/bas
  - tire courbe parabolique (y = a x² + b x)
  - cible ennemi et supprime point adverse

- Chaque tour :
  1. placer point ou
  2. déplacer bazooka ou
  3. tirer
  4. changer de joueur

## Instructions

1. `dotnet build`
2. `dotnet run`

## Notes

- `DatabaseService` reste à compléter selon modèle de données et besoins exacts
- Npgsql version v7.0.0 a avertissement de vulnérabilité ; migrer si besoin
