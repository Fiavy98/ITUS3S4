# JxPoint — Jeu de Bazooka

Jeu d'alignement 2 joueurs avec bazooka, développé en C# (Avalonia 11) + PostgreSQL.

## Prérequis

- .NET 8 SDK : `sudo apt install dotnet-sdk-8.0`
- PostgreSQL (optionnel pour la sauvegarde) : `sudo apt install postgresql`

## Lancer le jeu

```bash
cd JxPoint
dotnet run
```

## Structure du projet

```
JxPoint/
├── Models/
│   ├── PlateauModele.cs      # Logique plateau, placement, victoire
│   ├── BazookaModele.cs      # Position, déplacement, trajectoire parabolique
│   ├── TourModele.cs         # Gestion des tours, scores
│   └── DatabaseService.cs   # Accès PostgreSQL via Npgsql
├── Views/
│   ├── MainWindow.axaml      # Fenêtre principale (scores, boutons globaux)
│   ├── MainWindow.axaml.cs
│   ├── Plateaux.axaml        # Contrôle plateau + panneaux bazooka
│   └── Plateaux.axaml.cs    # Rendu Canvas, animation balle
├── App.axaml / App.axaml.cs
├── Program.cs
└── JxPoint.csproj
```

## Configuration PostgreSQL (optionnel)

```sql
-- Créer la base
CREATE DATABASE jxpoint;
CREATE USER jxuser WITH PASSWORD 'jxpass';
GRANT ALL PRIVILEGES ON DATABASE jxpoint TO jxuser;
```

Modifier la chaîne de connexion dans `Models/DatabaseService.cs` :

```csharp
"Host=localhost;Port=5432;Database=jxpoint;Username=jxuser;Password=jxpass"
```

Les tables sont créées automatiquement au premier lancement si la DB est disponible.

## Règles du jeu

- Grille 8×8 cases (9×9 sommets cliquables)
- À chaque tour, le joueur choisit :
  - **Placer** un point sur un sommet libre (clic)
  - **Déplacer** son bazooka (slider Position) — passe le tour
  - **Tirer** avec son bazooka (bouton 🔥 TIRER)
- **5 points alignés** (horizontal / vertical / diagonal) = +1 point au score
- **Tir bazooka** : courbe parabolique (vitesse 1=arc fort, 9=plat)
  - Touche un point adverse → il disparaît
  - Touche son propre point → aucun effet
- Le bouton 👁 Trajectoire affiche l'arc avant de tirer
