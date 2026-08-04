# Projet Panneaux Solaire
**But :** Destiner le Panneaux solaire et la Baterie pour le materielle

- **Input**
    - materielle : radio , tele, frigidaire, etc
    - heure d'utilisation : 9h a 12h
    - tranche : 
        - matin : 6 h à 17 h 
        - avant soir : 17 h à 19 h 
        - soir : 19 h a 6 h 
    - Puissance que cette Materiele ont besoin : example 75 w

- **Configuration :**
    - Le panneaux solaire utilise seulement 40% de sa puissance totale 
    - Le vrai puissace du baterie = puissance (wh) x 1,5
    - La batterie du panneaux solaire obligatoiremet à 100% pour qu il peuvent utiliser le soir

    - Pour le Tranche : 
        - matin : 6 - 17 h 
            - utilise 100% pour le 40% du panneaux(car le vrai puissance du panneaux est  son 40% )
            - le panneaux charge son baterie et peut donner toujours le puissance (100% pour ça 40%)
        - avant soir 
            - le capacite du puissance du panneaux diminue donc il donne un puissance 
            50% (on n'oblie pas que son vrai puissance est 40% donc il utilise 50% pour ça 40%)
        - soir 
            - c'est toute la baterie qui travaille (a condition qu il soit charge à 100%)

- **Output** 
    - Example : *Le panneaux solaire compatible au  materielle*
        - Materielle : Pc
        - panneaux slolaire : 150 w
        - baterie : 1000 wh

        - Materielle : radio
        - panneaux slolaire : 15 w
        - baterie : 100 wh

        - etc


- **Technologie**
    - Python 
    - mysql
    - NB : on utilise pas du html , on utilise l'interface qui existe dans Python

- **Base de donnes :**
    - Table tranche
        - id
        - nom
        - H_depart
        - H_arrive

    - Table materielle
        - id
        - nom
        - heure
        - IdTranche
        - watt
    
    - Table pannSolaire
        - id
        - nom
        - puissance
        - batterie
    
    - Table destination 
        - id
        - idMaterielle
        - idPanSolaire
    
### Plan 
- Page 1 : formulaire  - Menu voir les Destination
    - nom
    - heure d'utilisation
    - Tranche (liste deroulante)
    - watt
    - **Boutton valider**

- Background : inserer le materielle + calcul /algorithme + insertion dans la table destination 
    - *ajuster pour chercher le panneaux sollaire et la batterie compatible au materielle* 
    *selon son heure d'utilisation(et heure total d'utilisation), Tranche et son watt* 
        
    - **Des divers condition et calcul  :**

    - **Stockage dans la table destination**

- Page 2 resultat  (Destination)

    - Example : *Le panneaux solaire compatible au  materielle*
        - Materielle : Pc
        - panneaux slolaire : 150 w
        - baterie : 1000 wh

        - Materielle : radio
        - panneaux slolaire : 15 w
        - baterie : 100 wh

        - etc

solar_project/
│
├── main.py                → point d’entrée
├── config/
│   └── db.py             → connexion MSSQL
│
├── models/
│   ├── materielle.py
│   ├── tranche.py
│   ├── panneau.py
│   └── destination.py
│
├── services/
│   ├── calcul_service.py → ALGORITHME PRINCIPAL
│   └── panneau_service.py → choix panneau/batterie
│
├── repository/
│   ├── materielle_repo.py
│   ├── panneau_repo.py
│   └── destination_repo.py
│
├── ui/
│   └── app.py            → interface Tkinter
│
└── database.sql          → script MSSQL

Compiler : python main.py