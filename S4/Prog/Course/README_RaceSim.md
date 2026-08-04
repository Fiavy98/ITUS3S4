# RaceSim

Ce programme Java est une petite application de simulation de course de voiture.

## Objectif

L'application permet de configurer une voiture, de définir la longueur de la piste et le compte à rebours de départ, puis de lancer une simulation interactive où la voiture avance sur la piste.

## Structure principale

- `src/racesim/Main.java`
  - Point d'entrée du programme.
  - Lance l'interface graphique en affichant `FenetreAccueil`.

- `src/racesim/data/VoitureRepository.java`
  - Gère le chargement et l'ajout des voitures dans le fichier `data/voitures.txt`.
  - Si le fichier n'existe pas, il crée un fichier avec des voitures par défaut.

- `src/racesim/model/Voiture.java`
  - Représente une voiture avec un nom, une accélération (km/h/s) et une vitesse maximale (km/h).

- `src/racesim/model/Simulation.java`
  - Gère l'état de la simulation : vitesse, position, accélération, fin de course.
  - Calcule la vitesse en m/s et la position de la voiture sur la piste.

- `src/racesim/model/Chrono.java`
  - Chronomètre simple qui mesure le temps écoulé en millisecondes.
  - Utilisé pour afficher le temps de course et le compte à rebours.

- `src/racesim/ui/FenetreAccueil.java`
  - Interface de configuration de la simulation.
  - Permet de choisir une voiture, saisir la longueur de la piste et le départ.
  - Permet également d'ajouter une nouvelle voiture au fichier de données.

- `src/racesim/ui/FenetreSimulation.java`
  - Interface de simulation en direct.
  - Affiche la piste, le cockpit, le chrono de course et un bouton d'accélération.
  - Met à jour la simulation à chaque tick de la minuterie.

- `src/racesim/ui/VueCockpit.java`
  - Affiche la vitesse actuelle, le bouton d'accélération et la vitesse d'arrivée lorsque la course se termine.

- `src/racesim/ui/VuePiste.java`
  - Affiche graphiquement la piste et la position de la voiture.
  - Montre visuellement le déplacement de la voiture en fonction de sa position.

## Fonctionnement général

1. L'application démarre avec `Main` et ouvre `FenetreAccueil`.
2. `FenetreAccueil` charge la liste des voitures depuis `data/voitures.txt`.
3. L'utilisateur choisit une voiture, entre la longueur de piste et le temps de départ.
4. L'utilisateur peut aussi ajouter une nouvelle voiture, qui est sauvegardée dans le fichier de données.
5. Quand la simulation commence, `FenetreSimulation` crée un objet `Simulation` pour la voiture choisie.
6. Une minuterie (`Timer`) met à jour la simulation régulièrement.
7. Tant que l'utilisateur appuie sur le bouton "Accelerer", la simulation applique l'accélération.
8. La vitesse est limitée à la vitesse maximale de la voiture et la position progresse sur la piste.
9. Quand la voiture atteint la fin de la piste, la course s'arrête et la vitesse d'arrivée est affichée.

## Points importants

- La piste est mesurée en mètres.
- L'accélération est exprimée en km/h par seconde.
- La vitesse maximale est exprimée en km/h.
- Le chronomètre de la voiture démarre au moment où la voiture commence réellement à accélérer.

## Fichiers de données

- `data/voitures.txt`
  - Contient la liste des voitures disponibles.
  - Format de chaque ligne : `nom;accelerationKmHPerSec;vitesseMaxKmH`

## Résumé

RaceSim est une application Java Swing qui combine :
- une interface de configuration,
- une persistance simple de véhicules,
- un modèle physique élémentaire de simulation de course,
- et une visualisation graphique de la piste et du cockpit.
