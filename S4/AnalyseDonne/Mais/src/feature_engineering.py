import cv2
import numpy as np
import os
import pandas as pd 

# Fonction qui extrait le feature d'une image
def extract_features(image_path):
    
    # Charger l'image 
    img = cv2.imread(image_path)
    # redimensionner pour uniformer touteles image
    img = cv2.resize(img,(256,256))

    # ---------------------------
    # 1. Extraction couleur (HSV)
    # ---------------------------

    # Conversion RGB -> HSV
    hsv = cv2.cvtColor(img,cv2.COLOR_BGR2HSV)

    # Masquer pour detecter la rouille (jaune/orange/marron)
    lower = np.array([10,50,50]) # Couleur plus faible accepter 
    upper = np.array([35,255,255]) # couleur plus fort accepter
    mask = cv2.inRange(hsv,lower,upper) # Contient le zonne du rouille

    # Pourcentage de pixel de rouille
    pct_rouille = np.sum(mask > 0) / mask.size * 100

    # ---------------------------
    # 2. Extraction texture (Sobel)
    # ---------------------------

    # Image en niveaux de gris
    gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)

    # Détection des bords horizontaux et verticaux
    sobelx = cv2.Sobel(gray, cv2.CV_64F, 1, 0, 3)
    sobely = cv2.Sobel(gray, cv2.CV_64F, 0, 1, 3)

    # Intensité totale des bords
    sobel = np.sqrt(sobelx**2 + sobely**2)

    # Rugosité = intensite de variation des bords
    rugosite = sobel.var()

    # -----------------------------
    # 3. Feature personnelle
    # -----------------------------

    # Moyenne de la couleur verte (indice de santé de la feuille)
    mon_variable = np.mean(img[:, :, 1])

    return pct_rouille, rugosite, mon_variable

data = []
# Dossier des feuilles saines (label = 0)  et malades (label = 1)
for folder, label in [("dataset/saines", 0), ("dataset/malades", 1)]:

    # Lire chaque image a l'interieur du dossier
    for file in os.listdir(folder):
        
        path = os.path.join(folder, file)

        try:
            # Transformer l'image en caracteristique
            pct_rouille, rugosite, mon_variable = extract_features(path)

            # Ajout dans la liste
            data.append([
                file,
                pct_rouille,
                rugosite,
                mon_variable,
                label
            ])

        except:
            # Ignore les images corrompues ou erreurs
            pass

# Création du DataFrame final

df = pd.DataFrame(data, columns=[
    "ID_Image",
    "pct_rouille",
    "rugosite",
    "mon_variable",
    "label"
])


# Affichage du livrable intermédiaire
print("=== DATASET FEATURES ===")
print(df)

# Optionnel : sauvegarde
df.to_csv("features_maïs.csv", index=False)


# 1. on definit le fonction extract_features (image_path) 
# qui retourne pct_rouille, rugosite, mon_variable

# 2. on parcourt le dossier saine (0) et malade(1)

# 3. on lit chaque image a l'interieur de ce dossier 

# 4. on appele le fonction extract_features avec le fichier a lire a l'instant t pour retourner son caracteristique 

# 5 . on ajoute dans le dataFrame 