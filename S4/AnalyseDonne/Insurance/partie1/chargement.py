import pandas as pd

# charger le fichier csv 
df = pd.read_csv("../insurance.csv")

# Afficher le 5 premier ligne 
print(df.head())

## nb d'observations 
print(df.shape[0])

## nb de variable
print(df.shape[1])

## Type de donnees 
print(df.info())
