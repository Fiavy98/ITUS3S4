# 📊 Projet d’Analyse de Données
## 🎯 Prédiction du prix au m² des terrains à Antananarivo (Madagascar)

---

# 🧠 1. Objectif du projet

Ce projet vise à construire un modèle de **régression linéaire multiple** capable de prédire :

> 💰 Le prix au m² d’un terrain dans la ville d’Antananarivo

👉 Le système fonctionnera de manière interactive :
- L’utilisateur clique sur une carte 🗺️
- Le système retourne un prix estimé 💰

---

# 📊 2. Variables du modèle

## 🎯 Variable expliquée (Y)

- 💰 Prix au m² du terrain

---

## 🔍 Variables explicatives (X)

- 🚗 Accès (facilité d’accès)
- 🛣️ Distance par rapport aux routes nationales
- 🏗️ Bâtissable (oui/non)
- 💧 Distance par rapport aux points JIRAMA
- 📄 Statut administratif :
  - Titre borné
  - Cadastre (cadage)
  - Fifanolorana (transfert volontaire)
  - Commune

---

# 🧰 3. Technologies utilisées et leurs rôles

## 🗺️ QGIS
👉 entity["software","QGIS","GIS software"]

### 🎯 Rôle : préparation des données géographiques
- Gérer les coordonnées (latitude, longitude)
- Importer les données de terrain
- Calculer les distances (routes, JIRAMA, etc.)
- Nettoyer et structurer les données spatiales
- Exporter les données vers CSV ou GeoJSON

👉 C’est la phase **analyse et préparation SIG**

---

## 🌐 Django
👉 entity["software","Django","Python web framework"]

### 🎯 Rôle : backend (cerveau du système)
- Recevoir les coordonnées du clic sur la carte
- Charger le modèle de régression linéaire multiple
- Appliquer les calculs de prédiction
- Gérer les données (API)
- Retourner le prix estimé au frontend

👉 C’est la **logique de calcul et serveur**

---

## 🗺️ Leaflet (Frontend)
👉 entity["software","Leaflet","JavaScript mapping library"]

### 🎯 Rôle : interface utilisateur
- Afficher la carte interactive
- Permettre le clic sur un point
- Envoyer les coordonnées au backend Django
- Afficher le prix retourné

👉 C’est la **visualisation et interaction utilisateur**

---

# 🔁 4. Architecture globale du système

```
Utilisateur clique sur la carte (Leaflet)
            ↓
Coordonnées (lat, lon)
            ↓
Django reçoit la requête
            ↓
Modèle de régression calcule le prix
            ↓
Django renvoie le résultat
            ↓
Leaflet affiche le prix sur la carte
```

---

# ⚙️ 5. Étapes de réalisation du projet

## 🟢 Étape 1 : collecte des données
- Récupérer les informations des terrains
- Ajouter les variables explicatives
- Ajouter les coordonnées GPS

---

## 🟢 Étape 2 : traitement SIG
Avec entity["software","QGIS","GIS software"] :
- Importer les données
- Calculer les distances (routes, infrastructures)
- Vérifier la cohérence des données
- Exporter les données (CSV / GeoJSON)

---

## 🟢 Étape 3 : analyse statistique
- Nettoyer les données
- Étudier les corrélations
- Vérifier les variables importantes

---

## 🟢 Étape 4 : modèle de régression linéaire multiple
- Séparer X (variables explicatives) et Y (prix)
- Entraîner le modèle
- Tester la précision

---

## 🟢 Étape 5 : backend Django
Avec entity["software","Django","Python web framework"] :
- Créer une API `/predict`
- Charger le modèle ML
- Recevoir lat/lon + variables
- Retourner le prix estimé

---

## 🟢 Étape 6 : frontend carte interactive
Avec entity["software","Leaflet","JavaScript mapping library"] :
- Afficher la carte d’Antananarivo
- Gérer le clic utilisateur
- Envoyer les données à Django
- Afficher le résultat

---

# 🧠 6. Résumé du rôle de chaque technologie

| Technologie | Rôle |
|------------|------|
| QGIS | Préparation et analyse des données SIG |
| Django | Backend et calcul du modèle |
| Leaflet | Carte interactive et interface utilisateur |

---

# 🚀 7. Résultat attendu

✔ Carte interactive d’Antananarivo
✔ Clic sur un terrain
✔ Affichage du prix estimé au m²
✔ Modèle basé sur régression linéaire multiple

---

# 🎯 Conclusion

Ce projet combine :
- 📊 Analyse de données
- 🗺️ SIG (géospatial)
- 🤖 Machine Learning (régression)
- 🌐 Développement web

👉 C’est un projet complet de type **Data Science + GIS + Web Application**.

