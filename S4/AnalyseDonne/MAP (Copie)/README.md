- Titre : Prediction du terrain dans un Map (map de Antananarivo Madagascar)
- Fonctionalite : on clic sur un terrain dans le Map puis le systeme montre son Prix en Metre carre
- Variables :
    - Variable explicatif :
        -Acces 
        -distance
        - batisable
        - distance par rapport au poto jirama
        - Papyer
            - titre borne
            - cadage
            - fifanolorana
        - commune
    - variable expliquer 
        - Le prix du terrain en m^2
- etape du analyse 
    1. Lit le donnees
    2. exploration donnes 
        - repondre au question 
            - Les donnees sont propres
            - nb colonne/ligne
            - type de donnes
            - les colonne important
    3. Statistique descriptive
        - Moyenne
        - Mediane
        - Minimum et Maximum
        - Écart-type
        - Quartiles
    4. regarde les valeurs Null
    5. suppression 
        - des valeurs NULL
        - des valeurs aberrantes : valeurs completement different des autre
    6. changer numeriqueent le valeur
    7. separer x et y
    8. diviser en donnees test et entrainement :
        - Donnes d'entrainement
            - Servent a apprendre le modele
                - Le modele regarde ses  donnees et apprendles relations
        - Donnes de test
            - Servent a evaluer le modele 
                - ce sont le donnees que le model n'a jamais vue
    9. Ajouter le Modele IA
        - entrainer le model IA sur le donnes pour qu il apprenne a faire le prediction
    10. Test et evaluation
        - il apprend au donnes d'entrainnement 
        - on comare sa prediction dans le donnes de test si il sont reel et logique
    11. Sauvegarde le Model 
        - dans un fichier.plk

- Technologie 
    - jango Python


terrain_prediction/
│
├── terrain.csv              -> données d’entraînement utilisées pour créer le modèle
├── analyse_prediction.py    -> script Python pour analyser les données et entraîner le modèle IA
├── terrain_model.pkl        -> modèle IA sauvegardé (cerveau entraîné prêt à prédire)
├── requirements.txt         -> liste des bibliothèques Python nécessaires
