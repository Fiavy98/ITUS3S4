# Gestion d'energie - Panneau Solaire (Python + PyQt5 + SQL Server)

Application desktop Python pour calculer:
- la puissance du panneau (theorique + pratique)
- la composition optimale par type de panneau (quantite entiere, cout total)
- le gain estime pour jour ordinaire et jour ferie

selon des periodes configurees (journee, soiree, nuit), des appareils, et des pourcentages de correction.

## Stack
- Python 3.10+
- PyQt5 (interface desktop)
- SQL Server (historique des simulations)
- pyodbc (acces base)

## Installation

1. Creer un environnement virtuel
```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
```

2. Installer les dependances
```powershell
pip install -r requirements.txt
```

## Base SQL Server

1. Creer une base (ex: `PanneauDB`)
2. Executer le script [sql/schema.sql](sql/schema.sql)

## Sauvegarde automatique SQL (sans UI base)

Apres chaque clic sur "Calculer", l'application tente d'enregistrer la simulation en base.

Variables d'environnement disponibles:
- `PANNEAU_DB_AUTO_SAVE`: `1` (par defaut) ou `0` pour desactiver
- `PANNEAU_DB_SERVER`: `localhost` (par defaut)
- `PANNEAU_DB_NAME`: `PanneauDB` (par defaut)
- `PANNEAU_DB_TRUSTED`: `1` (auth Windows, par defaut) ou `0`
- `PANNEAU_DB_USER`: requis si `PANNEAU_DB_TRUSTED=0`
- `PANNEAU_DB_PASSWORD`: requis si `PANNEAU_DB_TRUSTED=0`

Un statut de sauvegarde est affiche en bas de la fenetre apres calcul.

## Lancer l'application

```powershell
python main.py
```

## Regles implementees
- 3 periodes fixes: journee, soiree, nuit (configurables)
- gestion des heures avec minutes (`HH:MM`)
- gestion des intervalles traversant minuit
- unites mixtes W/kW converties en W
- reduction de puissance du panneau en soiree: `1 - n%`
- version 21-04: batterie et periode nuit ignorees dans les calculs
- comparaison de plusieurs types de panneaux (un seul type choisi a la fois)

## Formules

- Puissance theorique panneau:
  - `P_theorique = max(pic_jour, pic_soiree/(1 - n/100))`

- Puissance pratique panneau (P1 historique):
  - `P_pratique = P_theorique / (m/100)`

- Capacite pratique batterie:
  - version 21-04: non utilisee (valeur forcee a 0)

- Energie disponible sur cycle (jour + soiree):
  - `E_disponible = somme(max(0, P_theorique - conso_jour)) + somme(max(0, P_theorique*(1-n/100) - conso_soiree))`

- Gains:
  - `Gain_ordinaire = (E_disponible / reference_wh) * prix_ordinaire`
  - `Gain_ferie = (E_disponible / reference_wh) * prix_ferie`

- Charge batterie (nouvelle regle):
  - `charge_base_w = B_pratique / (H_jour + H_soiree * (1 - n/100))`

- Pour chaque type de panneau i:
  - `P_i_pratique_requise = P_theorique / (rendement_i/100)`
  - `quantite_i = ceil(P_i_pratique_requise / energie_unitaire_pratique_i)`
  - `prix_total_i = quantite_i * prix_unitaire_i`

- Choix final:
  - type avec le plus petit `prix_total_i`
