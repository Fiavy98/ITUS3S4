import pandas as pd
import numpy as np

from sklearn.model_selection import train_test_split

from sklearn.tree import DecisionTreeClassifier
from sklearn.ensemble import RandomForestClassifier

from sklearn.metrics import (
    accuracy_score,
    precision_score,
    recall_score
)


# Import de Random Forest personnalisée
from random_forest import (
    build_random_forest,
    predict_forest
)



def train_models():


    # -----------------------------------
    # 1. Charger les features
    # -----------------------------------
    df = pd.read_csv("features_maïs.csv")


    # -----------------------------------
    # 2. Séparer X et y
    # -----------------------------------

    # Variables explicatives
    X = df[
        [
            "pct_rouille",
            "rugosite",
            "mon_variable"
        ]
    ]

    # Variable cible
    y = df["label"]



    # -----------------------------------
    # 3. Séparation entraînement / test
    # -----------------------------------

    X_train, X_test, y_train, y_test = train_test_split(
        X,
        y,
        test_size=0.2,
        random_state=42
    )



    # ===================================
    # 4. Arbre de décision Scikit-Learn
    # ===================================

    arbre = DecisionTreeClassifier(

        # Critère utilisé pour choisir le meilleur split
        criterion="gini",

        # Nombre maximum de niveaux dans l'arbre
        max_depth=5
    )


    # Apprentissage du modèle
    arbre.fit(
        X_train,
        y_train
    )


    # Prédiction sur les données de test
    prediction_arbre = arbre.predict(X_test)



    # Calcul du pourcentage de prédiction correcte du modèle
    score_arbre = accuracy_score(
        y_test,
        prediction_arbre
    )



    # ===================================
    # 5. Random Forest Scikit-Learn
    # ===================================

    foret = RandomForestClassifier(

        # Nombre d'arbres dans la forêt
        n_estimators=100,

        # Fixe le hasard pour obtenir les mêmes résultats
        random_state=42
    )


    # Apprentissage de la forêt
    foret.fit(
        X_train,
        y_train
    )


    # Prédiction
    prediction_foret = foret.predict(X_test)



    # Calcul de l'accuracy
    score_foret = accuracy_score(
        y_test,
        prediction_foret
    )



    # ===================================
    # 6. Random Forest Max-Minority
    #    (From Scratch)
    # ===================================


    # Conversion en tableau numpy
    X_train_np = X_train.values
    y_train_np = y_train.values


    X_test_np = X_test.values
    y_test_np = y_test.values



    # Construction du forêt personnalisée
    foret_personnelle = build_random_forest(
        X_train_np,
        y_train_np,

        # Nombre d'arbres personnalisés
        n_trees=10,

        # Profondeur maximale des arbres
        max_depth=5
    )



    # Liste pour stocker les prédictions
    prediction_personnelle = []


    # Chaque feuille de test passe dans la forêt
    for sample in X_test_np:

        prediction = predict_forest(
            foret_personnelle,
            sample
        )

        prediction_personnelle.append(prediction)



    # Calcul de l'accuracy du forêt
    score_personnelle = accuracy_score(
        y_test_np,
        prediction_personnelle
    )



    # ===================================
    # 7. Evaluation quantitative
    # Accuracy - Precision - Recall
    # ===================================


    # Tableau qui va contenir les résultats
    resultats = []



    # Ajouter les résultats Decision Tree Scikit-Learn

    resultats.append([
        "Decision Tree Scikit-Learn",

        accuracy_score(
            y_test,
            prediction_arbre
        ),

        precision_score(
            y_test,
            prediction_arbre,
            zero_division=0
        ),

        recall_score(
            y_test,
            prediction_arbre,
            zero_division=0
        )
    ])




    # Ajouter les résultats Random Forest Scikit-Learn

    resultats.append([
        "Random Forest Scikit-Learn",

        accuracy_score(
            y_test,
            prediction_foret
        ),

        precision_score(
            y_test,
            prediction_foret,
            zero_division=0
        ),

        recall_score(
            y_test,
            prediction_foret,
            zero_division=0
        )
    ])




    # Ajouter les résultats Random Forest Max-Minority

    resultats.append([
        "Random Forest Max-Minority",

        accuracy_score(
            y_test_np,
            prediction_personnelle
        ),

        precision_score(
            y_test_np,
            prediction_personnelle,
            zero_division=0
        ),

        recall_score(
            y_test_np,
            prediction_personnelle,
            zero_division=0
        )
    ])




    # Création du DataFrame de comparaison

    df_resultats = pd.DataFrame(
        resultats,
        columns=[
            "Modèle",
            "Accuracy",
            "Precision",
            "Recall"
        ]
    )



    # Affichage du tableau comparatif

    print("\n=== Comparaison des modèles ===")
    print(df_resultats)



    # -----------------------------------
    # Résultats simples
    # -----------------------------------

    print("\n=== Résultat Decision Tree Scikit-Learn ===")
    print("Accuracy :", score_arbre)


    print("\n=== Résultat Random Forest Scikit-Learn ===")
    print("Accuracy :", score_foret)


    print("\n=== Résultat Random Forest Max-Minority ===")
    print("Accuracy :", score_personnelle)



    return (
        arbre,
        foret,
        foret_personnelle
    )



# -----------------------------------
# Exécution du programme
# -----------------------------------

if __name__ == "__main__":

    arbre, foret, foret_personnelle = train_models()