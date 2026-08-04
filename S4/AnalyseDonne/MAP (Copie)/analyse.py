import pandas as pd
import joblib

from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_absolute_error

# 1. Lecture de donnees
df = pd.read_csv("terrain.csv")

print("\n===== DONNEES =====")
print(df.head())

# /

print("\n===== DIMENSION =====")
print("Lignes :", df.shape[0])
print("Colonnes :", df.shape[1])

print("\n===== COLONNES =====")
print(df.columns)

# 3. Statistique descriptive
print("\n===== STATISTIQUES =====")
print(df.describe())

# 4. Valeurs NULL
print("\n===== VALEURS NULL =====")
print(df.isnull().sum())

#  5. Suppresion valeur NULL
df = df.dropna()

# 6. suppression des valleur abberantes
df = df[df["prix_m2"] < 2000000]

# 7. Transformation de texte en Nombre
encoders = {}

colonnes_categorielles = [
    "acces",
    "batisable",
    "papier",
    "commune"
]

for col in colonnes_categorielles:

    encoder = LabelEncoder()

    df[col] = encoder.fit_transform(df[col])

    encoders[col] = encoder


# 8. Division x et y
X = df.drop("prix_m2", axis=1)

y = df["prix_m2"]

# 9. Entrainement et test
X_train, X_test, y_train, y_test = train_test_split(
    X,
    y,
    test_size=0.2,
    random_state=42
)

# 10 . Modele IA
model = RandomForestRegressor()
model.fit(X_train, y_train)

# 11. Test et evaluation 

predictions = model.predict(X_test)

error = mean_absolute_error(y_test, predictions)

print("\n===== ERREUR =====")
print("Erreur moyenne :", error)

# 12. sauvegarde le Model

joblib.dump(model, "terrain_model.pkl")

print("\nModele sauvegarde")




# =========================
# 13. TEST PREDICTION
# =========================

nouveau_terrain = {
    "acces": "bon",
    "distance": 2,
    "batisable": "oui",
    "distance_jirama": 100,
    "papier": "titre_borne",
    "commune": "ivandry"
}


# Encodage
for col in encoders:
    nouveau_terrain[col] = encoders[col].transform(
        [nouveau_terrain[col]]
    )[0]


# DataFrame
nouveau_df = pd.DataFrame([nouveau_terrain])


# Prediction
prix = model.predict(nouveau_df)

print("\n===== PREDICTION =====")
print("Prix estimé :", round(prix[0]), "Ar/m²")

