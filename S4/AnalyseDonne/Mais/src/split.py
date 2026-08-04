import numpy as np

def trouver_meilleur_split(X_column, y):
    """
    Recherche e meilleur seuil (split) pour une variable continue.

    Parametre : 
        X_column : liste des valeur d'une variable
        y : liste des labels (o=saine ,malade=1)
    Retour :
        meilleur seuil : seuil qui maximise P_split
        meuilleur purete : valeur maximale de P_split
    """

    # etape 1 : trier les donnees 
    indices = np.argsort(X_column)
    X_sorted = X_column[indices]
    y_sorted = y[indices]

    # etape 2 : Initialisation des variables 
    meilleure_seuil = None
    meilleure_purete = -1

    N = len(y_sorted)
    
    # etape 3 : Tester chaque seuil 
    for i in range(N - 1):
        
        # Ignorer les valeurs identique 
        if X_sorted[i] == X_sorted[i + 1]:
          continue

        # Calcul du seuil candidat
        seuil = (X_sorted[i] + X_sorted[i + 1]) / 2

        # Séparation des données
        gauche = y_sorted[X_sorted < seuil]
        droite = y_sorted[X_sorted >= seuil]

        # Calcul du purete du groupe gauche 
        if len(gauche) == 0:
            purete_gauche = 0
        else:
            nb_saines = np.sum(gauche==0)
            nb_malades = np.sum(gauche==1)

            purete_gauche = max(
                nb_saines / len(gauche),
                nb_malades / len(gauche)
            )

        # Calcul du purete du groupe droite 
        if len(droite) == 0:
            purete_droite = 0
        else:
            nb_saines = np.sum(droite == 0)
            nb_malades = np.sum(droite == 1)

            purete_droite = max(
                nb_saines / len(droite),
                nb_malades / len(droite)
            )
    
        # -----------------------------
        # Calcul de P_split
        # -----------------------------
        p_split = (
            (len(gauche) / N) * purete_gauche
            +
            (len(droite) / N) * purete_droite
        )

        # Garder le meilleur split
        if p_split > meilleure_purete:
            meilleure_purete = p_split
            meilleur_seuil = seuil

        # etape 4 : Retour du résultat
        return meilleur_seuil, meilleure_purete