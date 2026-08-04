# 🌞 Projet Panneaux Solaire

## 🎯 But
Destiner le panneau solaire et la batterie adaptés pour alimenter un matériel donné selon son utilisation.

---

## 📥 Input
- Matérielle : radio, télé, frigidaire, PC, etc  
- Heure d'utilisation : ex : 9h à 12h  
- Tranche :
  - matin : 6 h à 17 h  
  - avant soir : 17 h à 19 h  
  - soir : 19 h à 6 h  
- Puissance du matérielle : ex : 75 W  

---

## ⚙️ Configuration
- Le panneau solaire utilise seulement 40% de sa puissance totale  
- La vraie capacité de la batterie = batterie × 1.5  
- La batterie doit être chargée à 100% pour utilisation le soir  

---

## 🕒 Gestion des tranches

| Tranche | Fonctionnement |
|--------|--------------|
| Matin (6h–17h) | Panneau = 100% de ses 40%, alimente + charge batterie |
| Avant soir (17h–19h) | Panneau = 50% de ses 40% |
| Soir (19h–6h) | Batterie uniquement |

---

## 🔢 Calculs et Algorithmes

### 1. Énergie consommée
E = P × t

```python
energie = watt * heure
```

---

### 2. Puissance réelle du panneau
P_reel = P_panneau × 0.4

```python
puissance_reelle = puissance_panneau * 0.4
```

---

### 3. Batterie réelle
B_reel = batterie × 1.5

```python
batterie_reelle = batterie * 1.5
```

---

### 4. Facteur tranche

```python
if tranche == "matin":
    facteur_tranche = 1.0
elif tranche == "avant soir":
    facteur_tranche = 0.5
else:
    facteur_tranche = 0
```

---

### 5. Énergie fournie par le panneau
E_panneau = P_reel × heure × facteur

```python
energie_panneau = puissance_reelle * heure * facteur_tranche
```

---

### 6. Énergie batterie nécessaire

```python
if tranche == "soir":
    energie_batterie = energie
else:
    energie_batterie = energie - energie_panneau
```

---

### 7. Conditions de compatibilité

```python
condition1 = puissance_reelle >= watt
condition2 = (energie_panneau + batterie_reelle) >= energie
condition3 = batterie_reelle >= energie_batterie
```

---

### 8. Recherche du panneau

```python
for panneau in panneaux:
    puissance_reelle = panneau.puissance * 0.4
    batterie_reelle = panneau.batterie * 1.5

    if condition1 and condition2 and condition3:
        resultat.append(panneau)
```

---

### 9. Choix du meilleur panneau

```python
resultat.sort(key=lambda x: x.puissance)
meilleur = resultat[0]
```

---

### 10. Insertion base de données

```sql
INSERT INTO destination (idMaterielle, idPanSolaire)
VALUES (?, ?)
```

---

## 📊 Output

### Exemple :

- Matérielle : PC  
  - Panneau solaire : 150 W  
  - Batterie : 1000 Wh  

- Matérielle : Radio  
  - Panneau solaire : 15 W  
  - Batterie : 100 Wh  

---

## 🗄️ Base de données

### Table tranche
- id  
- nom  
- H_depart  
- H_arrive  

### Table materielle
- id  
- nom  
- heure  
- IdTranche  
- watt  

### Table pannSolaire
- id  
- nom  
- puissance  
- batterie  

### Table destination
- id  
- idMaterielle  
- idPanSolaire  

---

## 🖥️ Interface

### Page 1 : Formulaire
- nom  
- heure d'utilisation  
- tranche  
- watt  
- bouton Valider  

### Background
- insertion du matériel  
- calcul automatique  
- recherche panneau compatible  
- insertion dans destination  

### Page 2 : Résultat
- affichage des panneaux compatibles  

---

## 🚀 Améliorations possibles
- multi-matériels  
- batterie optimale automatique  
- facteur météo  
- optimisation coût


- **Technologie**
    - Python 
    - mssql
    - NB : on utilise pas du html , on utilise l'interface qui existe dans Python

- Organisation du projet
projet_panneau_solaire/
│
├── main.py  
│   # 🚀 Point d'entrée du programme
│   # Lance l'application et initialise l'interface utilisateur
│
├── config/
│   └── database.py  
│       # ⚙️ Configuration de la base de données
│       # Gère la connexion à MSSQL (connexion, paramètres)
│
├── models/
│   ├── materielle.py  
│   │   # 📦 Représente un matériel (nom, heure, watt, tranche)
│   │
│   ├── panneau.py  
│   │   # ☀️ Représente un panneau solaire (puissance, batterie)
│   │
│   ├── tranche.py  
│   │   # 🕒 Représente une tranche horaire (matin, soir, etc.)
│   │
│   └── destination.py  
│       # 🔗 Lien entre matériel et panneau compatible
│
├── services/
│   ├── calcul_service.py  
│   │   # 🧮 Contient tous les calculs (énergie, puissance réelle, batterie)
│   │
│   └── panneau_service.py  
│       # 🧠 Logique principale du projet
│       # Cherche le panneau solaire compatible selon les conditions
│
├── repository/
│   ├── materielle_repo.py  
│   │   # 🗄️ Accès base de données pour les matériels (CRUD)
│   │
│   ├── panneau_repo.py  
│   │   # 🗄️ Récupère les panneaux solaires depuis la base
│   │
│   └── destination_repo.py  
│       # 🗄️ Enregistre les résultats (compatibilité)
│
├── ui/
│   ├── form_page.py  
│   │   # 🖥️ Interface formulaire (saisie utilisateur)
│   │
│   └── result_page.py  
│       # 📊 Interface affichage des résultats
│
├── utils/
│   └── helpers.py  
│       # 🧰 Fonctions utilitaires (validation, conversion, etc.)
│
└── README.md  
    # 📄 Documentation du projet (explication, calculs, utilisation)