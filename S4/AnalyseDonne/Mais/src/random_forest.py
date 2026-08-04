import numpy as np

from decision_tree import build_tree, predict_tree



# -----------------------------------
# Construction Random Forest
# -----------------------------------
def build_random_forest(
        X,
        y,
        n_trees=10,
        max_depth=5):


    forest = []


    for i in range(n_trees):


        # -----------------------------
        # Bagging
        # -----------------------------

        indices = np.random.choice(
            len(X),
            size=len(X),
            replace=True
        )


        X_bootstrap = X[indices]

        y_bootstrap = y[indices]



        # -----------------------------
        # Construction arbre
        # -----------------------------

        arbre = build_tree(
            X_bootstrap,
            y_bootstrap,
            depth=0,
            max_depth=max_depth
        )


        forest.append(arbre)



    return forest




# -----------------------------------
# Prédiction Random Forest
# -----------------------------------
def predict_forest(forest, sample):


    predictions = []


    # Chaque arbre vote

    for arbre in forest:


        prediction = predict_tree(
            arbre,
            sample
        )


        predictions.append(prediction)



    # Vote majoritaire

    resultat = np.bincount(
        predictions
    ).argmax()


    return resultat

