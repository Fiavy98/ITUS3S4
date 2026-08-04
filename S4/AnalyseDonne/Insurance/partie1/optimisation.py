import pandas as pd

df = pd.read_csv("../insurance.csv")

# Convention de variable textuelle en categorie
df["sex"]=df["sex"].astype("category")
df["smoker"]=df["smoker"].astype("category")
df["region"]=df["region"].astype("category")

# Vérification des types
print(df.dtypes)