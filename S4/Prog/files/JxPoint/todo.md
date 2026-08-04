dotnet run
# Plateaux de jeux 
    - Comment gerer le nombreux petit carre et ses sommets dans le plateaux?
        -On donne un adresse a chaque sommet 
            -On fait un carre de taille N et cette carre admet un sommet N+1 
            -on fait un  sommet de taille N+1 on prend son adresse (ligne,colone)

# Point du joueur
    -Comment gerer le point du joeur
            - comment placer le point
            -le point est bien placer
            - comment connaitre que cette point  s'agit a quelle joueur 
                    -le couleur de point a chaque joueur est logique que ce soit different couleur mais comment il peuvent se reconaitre pour qu on peut tracer un ligne (si 5 point aligne)
            -comment connaitre le point d'un joueur peut gaigner un point (aligner en 5)

# Implémentation réalisée
- Interface de jeu dans `views/Plateaux.axaml` : plateau, boutons mode, contrôle bazooka, vitesse tir, score, réinitialisation.
- Logique de jeu dans `views/Plateaux.axaml.cs`
  - placement de point (P1 bleu, P2 rouge)
  - mode bazooka et tir (impact adversaire, pas d'effet sur alliés)
  - mouvement bazooka haut/centre/bas
  - détection alignement 5 (horizontal, vertical, diagonales)
  - alternance des tours
- Persistance PostgreSQL via `Npgsql`
  - création tables `joueurs`, `parties`, `points`
  - sauvegarde de partie et points
  - gestion des erreurs PostgreSQL en fallback mode hors-ligne
- Ajout dépendance `Npgsql` dans `JxPoint.csproj`
- Build et exécution réussies (avec avertissement de vulnérabilité `Npgsql` 7.0.0)

# Prochaines améliorations- tirer bazooka : position curseur + bouton P1/P2 (implémenté)
- représentation visuelle type "image" du bazooka (implem. en formes)- animation réelle du tir courbé (parabole) dans `GameCanvas`
- création de compte utilisateur, gestion des sessions
- tests unitaires / E2E
- support config pour chaîne de connexion PostgreSQL




