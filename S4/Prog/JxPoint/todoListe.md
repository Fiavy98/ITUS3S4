# Todo Liste — Jeu de points avec tir au bazooka
> Projet C# (Avalonia) + PostgreSQL

---

## ✅ Étape 1 — Plateau de jeu (TERMINÉ)

- [x] Créer la grille visuelle avec `Canvas` + `Line`
- [x] Comprendre la convention cases vs sommets
- [x] Adresser chaque sommet avec `(col, row)`
- [x] Grille N×N cases → (N+1)×(N+1) sommets
- [x] Placer les `Ellipse` cliquables sur les intersections

---

## ✅ Étape 2 — Points du joueur (TERMINÉ)

- [x] Créer `Models/PlateauModele.cs`
- [x] Tableau logique `int[,] Etat` (0=libre, 1=J1, 2=J2)
- [x] Tableau visuel `Ellipse[,] _vues`
- [x] `PlacerPoint(col, row)` — vérifie si libre, enregistre, alterne le tour
- [x] `JoueurActuel` qui bascule entre 1 et 2
- [x] Colorier l'ellipse au clic (bleu=J1, rouge=J2)
- [x] `VerifierVictoire(col, row, joueur)` — vérifie les 4 directions
- [x] `CompterDansDirection()` — compte les points consécutifs dans un sens

---

## 🔲 Étape 3 — Affichage du tour et score (UI)

- [ ] Créer un label dans `MainWindow.axaml` qui affiche "Tour du Joueur X"
- [ ] Mettre à jour ce label après chaque `PlacerPoint()`
- [ ] Créer un label score "J1 : 0  |  J2 : 0"
- [ ] Incrémenter le score quand `VerifierVictoire()` retourne `true`
- [ ] Afficher un message de victoire dans l'UI (pas seulement dans la console)
- [ ] Tracer une ligne visuelle entre les 5 points alignés (sur le `Canvas`)

---

## 🔲 Étape 4 — Bazooka (modèle logique)

- [ ] Créer `Models/BazookaModele.cs`
- [ ] Définir la position du bazooka : bord droit (J1), bord gauche (J2)
- [ ] Attribut `PositionRow` — la ligne sur laquelle le bazooka est aligné
- [ ] Méthode `DeplacerVersHaut()` — `PositionRow--` (min = 0)
- [ ] Méthode `DeplacerVersBas()` — `PositionRow++` (max = N)
- [ ] Calculer la trajectoire parabolique selon la vitesse (1 à 9)
  - [ ] Formule : `y = a * x² + b * x` (parabole concave)
  - [ ] Paramètre `a` varie selon la vitesse choisie
  - [ ] Retourner la liste des `(col, row)` traversés par la balle
- [ ] Méthode `Tirer(vitesse)` — retourne le sommet touché `(col, row)` ou `null`
- [ ] Logique de collision :
  - [ ] Si le sommet touché appartient à l'adversaire → le point disparaît
  - [ ] Si le sommet touché appartient au tireur → aucun effet

---

## 🔲 Étape 5 — Bazooka (visuel Avalonia)

- [ ] Dessiner le bazooka sur le `Canvas` (un rectangle ou image)
  - [ ] J1 : bord droit du plateau
  - [ ] J2 : bord gauche du plateau
- [ ] Boutons déplacement : ↑ Haut, ↓ Bas pour chaque joueur
- [ ] Mettre à jour la position visuelle du bazooka après déplacement
- [ ] Animer la trajectoire de la balle sur le `Canvas`
  - [ ] Dessiner la courbe parabolique avec un `Polyline` ou animation
  - [ ] Effacer la courbe après l'impact
- [ ] Si un point est détruit : remettre l'`Ellipse` en blanc (`Brushes.White`)

---

## 🔲 Étape 6 — Tour de jeu complet

- [ ] Créer `Models/TourModele.cs` ou enrichir `PlateauModele`
- [ ] À chaque tour, le joueur choisit UNE action :
  - [ ] Action A : Placer un point → appelle `PlacerPoint()`
  - [ ] Action B : Déplacer le bazooka → appelle `DeplacerVersHaut/Bas()`
  - [ ] Action C : Tirer → appelle `Tirer(vitesse)`
- [ ] Après chaque action → passer au joueur suivant
- [ ] Bloquer les actions du joueur qui n'est pas en train de jouer
- [ ] Désactiver les sommets cliquables pendant le tour du bazooka

---

## 🔲 Étape 7 — Base de données PostgreSQL

### Schéma des tables
- [ ] Créer la table `joueurs` : `id`, `nom`, `date_creation`
- [ ] Créer la table `parties` : `id`, `joueur1_id`, `joueur2_id`, `date`, `gagnant_id`, `statut`
- [ ] Créer la table `sommets` : `partie_id`, `col`, `row`, `joueur` (état à chaque coup)
- [ ] Créer la table `scores` : `partie_id`, `joueur_id`, `points`

### Connexion C#
- [ ] Installer le package `Npgsql` (driver PostgreSQL pour .NET)
- [ ] Créer `Models/DatabaseService.cs`
- [ ] Méthode `ConnecterAsync()` — ouvrir la connexion
- [ ] Méthode `SauvegarderPartie(PlateauModele)` — enregistrer l'état actuel
- [ ] Méthode `ChargerPartie(partieId)` — restaurer un état sauvegardé
- [ ] Méthode `EnregistrerJoueur(nom)` — créer un joueur
- [ ] Méthode `MettreAJourScore(partieId, joueurId, points)`

---

## 🔲 Étape 8 — Écrans et navigation

- [ ] Écran d'accueil : saisir les noms des deux joueurs
- [ ] Écran de jeu : plateau + score + indicateur de tour + bazooka
- [ ] Écran de fin : afficher le gagnant et le score final
- [ ] Bouton "Nouvelle partie"
- [ ] Bouton "Sauvegarder" (→ PostgreSQL)
- [ ] Bouton "Charger une partie"

---

## 🔲 Étape 9 — Tests et finitions

- [ ] Tester la détection de victoire sur toutes les directions
- [ ] Tester la trajectoire du bazooka avec différentes vitesses
- [ ] Tester la sauvegarde et le chargement d'une partie
- [ ] Vérifier que les actions bloquées le sont vraiment (pas de double clic)
- [ ] Ajuster la taille du plateau (paramètre configurable)

---

## Ordre recommandé

```
Étape 3 → Étape 4 → Étape 5 → Étape 6 → Étape 7 → Étape 8 → Étape 9
(UI tour)  (bazooka  (bazooka   (tour      (base de   (écrans)   (tests)
           logique)  visuel)    complet)   données)
```

---

## Structure du projet cible

```
JxPoint/
├── Models/
│   ├── PlateauModele.cs      ✅ fait
│   ├── BazookaModele.cs      🔲 à faire
│   ├── TourModele.cs         🔲 à faire
│   └── DatabaseService.cs    🔲 à faire
├── Views/
│   ├── MainWindow.axaml      🔲 à enrichir
│   ├── MainWindow.axaml.cs   🔲 à enrichir
│   ├── Plateaux.axaml        ✅ fait
│   └── Plateaux.axaml.cs     ✅ fait
└── Assets/
    └── (images bazooka éventuellement)
```
ça ne marche pas est ce que tu comprend le fonctionnalite d'abord 
si on arrive a tire un point d'un adversaire il disparait 
et si le joeur dont son point a ete retire arive a tirer sur le cordonnes où son point a ete place (point vide) son point revient
le joeur qui arrive a tirer un point obtenue 1 point (score)
donc li son adversaire arrive a revenir son point 
le point du joeur qui a tire le point diminue de point