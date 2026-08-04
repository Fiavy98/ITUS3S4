import numpy as np
from split import trouver_meilleur_split


# -----------------------------------
# Fonction pour trouver la classe majoritaire
# -----------------------------------
def classe_majoritaire(y):
    """
    Retourne la classe la plus présente dans un noeud
    0 = saine
    1 = malade
    """

    nb_saines = np.sum(y == 0)
    nb_malades = np.sum(y == 1)

    if nb_malades > nb_saines:
        return 1
    else:
        return 0


# -----------------------------------
# Construction récursive de l'arbre
# -----------------------------------
def build_tree(X, y, depth=0, max_depth=5):

    """
    Construit un arbre de décision récursivement.

    Paramètres :
        X : variables explicatives
            (pct_rouille, rugosite, mon_variable)

        y : labels
            0 = saine
            1 = malade

        depth : profondeur actuelle de l'arbre

        max_depth : profondeur maximale autorisée

    Retour :
        un dictionnaire représentant l'arbre
    """


    # -----------------------------------
    # Condition d'arrêt 1 :
    # Le noeud est totalement pur
    # -----------------------------------
    if len(np.unique(y)) == 1:

        return {
            "type": "leaf",
            "classe": y[0]
        }


    # -----------------------------------
    # Condition d'arrêt 2 :
    # Profondeur maximale atteinte
    # -----------------------------------
    if depth >= max_depth:

        return {
            "type": "leaf",
            "classe": classe_majoritaire(y)
        }



    # -----------------------------------
    # Chercher le meilleur split
    # -----------------------------------

    meilleur_score = -1
    meilleur_feature = None
    meilleur_seuil = None


    # Tester chaque variable :
    # pct_rouille, rugosite, mon_variable

    for feature_index in range(X.shape[1]):

        seuil, score = trouver_meilleur_split(
            X[:, feature_index],
            y
        )


        # Garder le meilleur split
        if score > meilleur_score:

            meilleur_score = score
            meilleur_feature = feature_index
            meilleur_seuil = seuil



    # Si aucun split trouvé
    if meilleur_feature is None:

        return {
            "type": "leaf",
            "classe": classe_majoritaire(y)
        }



    # -----------------------------------
    # Division des données
    # -----------------------------------

    gauche = X[:, meilleur_feature] < meilleur_seuil

    droite = X[:, meilleur_feature] >= meilleur_seuil



    # -----------------------------------
    # Création récursive des enfants
    # -----------------------------------

    arbre = {

        "type": "node",

        "feature": meilleur_feature,

        "seuil": meilleur_seuil,


        "gauche": build_tree(
            X[gauche],
            y[gauche],
            depth + 1,
            max_depth
        ),


        "droite": build_tree(
            X[droite],
            y[droite],
            depth + 1,
            max_depth
        )
    }


    return arbre




# -----------------------------------
# Prédiction d'un arbre
# -----------------------------------
def predict_tree(tree, sample):


    # Si on arrive à une feuille
    if tree["type"] == "leaf":

        return tree["classe"]


    # Récupérer le split
    feature = tree["feature"]
    seuil = tree["seuil"]


    # Aller à gauche ou droite

    if sample[feature] < seuil:

        return predict_tree(
            tree["gauche"],
            sample
        )

    else:

        return predict_tree(
            tree["droite"],
            sample
        )
    