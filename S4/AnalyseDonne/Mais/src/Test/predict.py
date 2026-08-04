import cv2
import numpy as np
import joblib
from feature_engineering import extract_features

# -----------------------------
# Charger le modèle entraîné
# -----------------------------
model = joblib.load("modele_maïs.pkl")

# -----------------------------
# Image à tester
# -----------------------------
image_path = "dataset/test3.jpeg"   

# -----------------------------
# Extraction des features
# -----------------------------
pct_rouille, rugosite, mon_variable = extract_features(image_path)

X_new = [[pct_rouille, rugosite, mon_variable]]

# -----------------------------
# Prédiction
# -----------------------------
prediction = model.predict(X_new)

# -----------------------------
# Résultat
# -----------------------------
if prediction[0] == 1:
    print("🌿 Feuille MALADE")
else:
    print("🌿 Feuille SAINE")