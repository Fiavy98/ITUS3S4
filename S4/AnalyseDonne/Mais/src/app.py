import os
import shutil

import streamlit as st
import joblib
import numpy as np

from feature_engineering import extract_features


# ==========================================================
# Configuration de la page
# ==========================================================

st.set_page_config(
    page_title="Détection de la rouille du maïs",
    page_icon="🌽",
    layout="wide"
)


# ==========================================================
# Titre de l'application
# ==========================================================

st.title("Détection automatique de la rouille du maïs")

st.write("""
Cette application permet de détecter automatiquement si une
feuille de maïs est **saine** ou **malade** grâce à un modèle
de Machine Learning.
""")


# ==========================================================
# Création du dossier qui mémorise les images analysées
# ==========================================================

UPLOAD_FOLDER = "uploads"

if not os.path.exists(UPLOAD_FOLDER):
    os.makedirs(UPLOAD_FOLDER)


# ==========================================================
# Chargement du modèle entraîné
# ==========================================================

# Le modèle est chargé une seule fois
model = joblib.load("modele_maïs.pkl")


# ==========================================================
# Upload d'une nouvelle image
# ==========================================================

uploaded_file = st.file_uploader(
    "Téléverser une feuille de maïs",
    type=["png", "jpg", "jpeg"]
)


# ==========================================================
# Analyse de l'image
# ==========================================================

if uploaded_file is not None:

    # ------------------------------------------------------
    # Sauvegarder l'image dans uploads/
    # ------------------------------------------------------

    image_path = os.path.join(
        UPLOAD_FOLDER,
        uploaded_file.name
    )

    with open(image_path, "wb") as f:
        shutil.copyfileobj(uploaded_file, f)


    # ------------------------------------------------------
    # Afficher l'image téléversée
    # ------------------------------------------------------

    st.image(
        image_path,
        caption="Image analysée",
        use_container_width=True
    )


    # ------------------------------------------------------
    # Extraire les caractéristiques de la feuille
    # ------------------------------------------------------

    pct_rouille, rugosite, mon_variable = extract_features(
        image_path
    )


    # ------------------------------------------------------
    # Afficher les caractéristiques extraites
    # ------------------------------------------------------

    st.subheader("Caractéristiques extraites")

    col1, col2, col3 = st.columns(3)

    with col1:
        st.metric(
            "Rouille (%)",
            f"{pct_rouille:.2f}"
        )

    with col2:
        st.metric(
            "Rugosité",
            f"{rugosite:.2f}"
        )

    with col3:
        st.metric(
            "Moyenne du vert",
            f"{mon_variable:.2f}"
        )


    # ------------------------------------------------------
    # Préparer les données pour le modèle
    # ------------------------------------------------------

    X_new = np.array([
        [
            pct_rouille,
            rugosite,
            mon_variable
        ]
    ])


    # ------------------------------------------------------
    # Effectuer la prédiction
    # ------------------------------------------------------

    prediction = model.predict(X_new)


    # ------------------------------------------------------
    # Afficher le diagnostic
    # ------------------------------------------------------

    st.subheader("Résultat de l'analyse")

    if prediction[0] == 1:

        st.error(
            "⚠️ Feuille MALADE "
        )

        diagnostic = "Malade"

    else:

        st.success(
            "✅ Feuille SAINE"
        )

        diagnostic = "Saine"


    # ------------------------------------------------------
    # Afficher le diagnostic détaillé
    # ------------------------------------------------------

    st.write("### Diagnostic")

    st.write(f"**Résultat :** {diagnostic}")


# ==========================================================
# Galerie des analyses précédentes
# ==========================================================

st.divider()

st.header("📂 Historique des images analysées")

images = os.listdir(UPLOAD_FOLDER)

if len(images) == 0:

    st.info("Aucune image analysée pour le moment.")

else:

    # Création de 3 colonnes
    cols = st.columns(3)

    for i, image in enumerate(images):

        chemin = os.path.join(
            UPLOAD_FOLDER,
            image
        )

        with cols[i % 3]:

            st.image(
                chemin,
                caption=image,
                use_container_width=True
            )

            st.caption("Image analysée")