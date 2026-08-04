# 🚗 Projet Java Swing : Simulation de course (400m)

## 🎯 Objectif
Créer une application Java Swing simulant une voiture qui parcourt une piste de 400 mètres avec :
- Une simulation réaliste (accélération progressive)
- Deux vues (cockpit + vue globale)
- Deux chronomètres
- Données stockées dans un fichier TXT

---

# 🧱 1. Architecture du projet

## 🔹 Pattern utilisé : MVC simplifié

### 📦 Model (Données)
- Voiture
- Simulation
- Chrono

### 🖥️ View (Interface graphique)
- FenetreAccueil (choix voiture)
- FenetreSimulation
  - VueCockpit
  - VuePiste

### 🎮 Controller (Logique)
- Gestion accélération
- Gestion chronos
- Mise à jour simulation

---

# 🚘 2. Modélisation des classes

## 🔸 Classe Voiture
Contient :
- nom
- acceleration (km/h/s)
- vitesseMax (km/h)

---

## 🔸 Classe Simulation
Responsable de :
- vitesse actuelle
- position (en mètres)
- état accélération (true/false)

### Comportement :
- Si bouton appuyé → accélération
- Sinon → vitesse constante
- Limiter à vitesse max
- Convertir km/h → m/s
- Mettre à jour position

---

## 🔸 Classe Chrono

### 🔹 Chrono 1 (automatique)
- Démarre au début
- S’arrête à 400m

### 🔹 Chrono 2 (manuel)
- Boutons Start / Stop
- Commence à -5 secondes
- Continue même si la voiture finit

---

# 📄 3. Gestion des données (TXT)

## Format fichier : voitures.txt
Nom;Acceleration;VitesseMax
Ferrari;20;320
Bugatti;25;400
Toyota;10;180


## Fonctionnalités :
- Lire fichier
- Charger liste voitures
- Permettre ajout manuel de nouvelles voitures

---

# 🖥️ 4. Interface graphique

## 🔹 Fenêtre 1 : Accueil
Contient :
- Liste des voitures (ComboBox)
- Bouton "Démarrer"

### Action :
- Sélection voiture
- Ouvrir simulation

---

## 🔹 Fenêtre 2 : Simulation

### 🧩 Composants principaux :

#### 1. Vue Cockpit
- Compteur de vitesse
- Aiguille dynamique
- Bouton accélération (maintenir)
- Affichage vitesse actuelle

#### 2. Vue Piste
- Piste de 400m
- Voiture qui avance
- Position proportionnelle à la distance

#### 3. Chronos
- Chrono automatique (course)
- Chrono manuel (Start / Stop)

---

# 🎮 5. Gestion de l'accélération

## Comportement :
- Bouton pressé → accélération active
- Bouton relâché → vitesse constante

## Important :
Simulation en temps réel (pas instantanée)

---

# 🔄 6. Boucle de simulation

## Utilisation :
- Timer Swing (~60 FPS)

## À chaque frame :
- Calcul deltaTime
- Mettre à jour vitesse
- Mettre à jour position
- Mettre à jour chronos
- Rafraîchir affichage

---

# 📏 7. Règles physiques

- Accélération en km/h/s
- Conversion en m/s
- Position += vitesse * temps
- Limite : vitesse max

---

# 🎨 8. Affichage graphique

## Vue piste :
- Ligne de 400m
- Voiture (rectangle/image)
- Déplacement fluide

## Cockpit :
- Compteur circulaire ou barre
- Aiguille dynamique

---

# ⏱️ 9. Gestion des chronos

## Chrono course :
- Démarrage automatique
- Stop à 400m

## Chrono manuel :
- Bouton Start → commence à -5s
- Bouton Stop → arrête
- Indépendant de la course

---

# ⚠️ 10. Contraintes

- Pas de base de données (TXT uniquement)
- Simulation fluide obligatoire
- Interaction temps réel (maintenir bouton)
- Code structuré (séparation logique / UI)

---

# 🚀 11. Extensions possibles

- Ajouter frein
- Ajouter plusieurs voitures en même temps
- Ajouter son moteur
- Ajouter météo (impact sur vitesse)
- Mode multijoueur

---

# 🧠 Conclusion

Le projet est une simulation interactive avec :
- Physique simple mais réaliste
- Interface graphique double vue
- Gestion utilisateur en temps réel
- Données dynamiques via fichier TXT