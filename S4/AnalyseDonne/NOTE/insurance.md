# Partie 1 : Premier contact avec les données
## Charger un fichier csv 
- import pandas as pd
    - sert a analyser et conventire le donnes en tableau
- lire le fichier csv 
    - ``` python 
            df=pd.read_csv("../insurance.csv")
        ```
- afficher 
    - print(df.head())
        - head() : aff le 5 premier ligne par deffaut
        - on peut faire head(10)

### Dimensions
**- Le variable = nom du colonne**
- variable Variable numerique 
    -  valleur messurable (chiffre)
        - age , children , charges 
- variable categorielle 
    - groupe/categorie 
        - sex , region 
- Pour compter 
    -df.shape[1]


**- Observation = Ligne**
- Pour compter 
    - df.shape[0]

**- DataSet = Tableau Complet**

### Types de données
- df.info()

## Optimisation 
- conventir les variables textuelles en category
**Permet de conventir le text repetitif par de code**
- example 
    - sex : male
            female
            male
- appres la convention 
    - male : 1
    - femele : 0
        - d'où
            - sex : 1
                    0
    
- Pourquoi On fait ça ?
    - moins de memoire utilisée
    - Calculs plus rapides
    - meilleure gestion des variables qualitatives

-- 

# Partie 2 : ANALYSE DESCRIPTIVE ET QUALITE
## Resume statistique des variable numerique 
- count : nombre de valeur
    - mean : moyenne 
        - globalise l'ensemble : Age =  [10,20,30]/3 = 20 
            - 👉 Population plutot Jeune
    - std : ecart-type (elanelan'ireo valeur)
        - petit ecart-type : le valeurs sont plus proche de la moyenne
        - grand ecart-type : les valeurs sont tres differents
    - min : minimum :  valeur minimum
    - max : maximum : valeur maximum
    - en % : quartiles
        - Q1 
        - Q2 = mediane :  milieu
        - Q3 
            - ex : 0% ---- 25% ---- 50% ---- 75% ---- 100%
                        Q1        médiane     Q3

**Code : print(df.describe())**

## valeur manquante (Nan)
**Code : df.isnull().sum()**

**Comment le traiter ?**
- 1. Supprimer les lignes
    - df.dropna()
- 2. Remplacer par une valeur
    - Pour numerique
        - df["children"].fillna(df["children"].mean()) ## Remplacer par le moyenne
    - Categoriel
        - df["smoker"].fillna("unknown")

# Partie 3 : Visualisation univariée (Distribution)

## 1 . Tracez histogramme 
``` python
    import pandas as pd
    import matplotlib.pyplot as plt

    # Charger les données
    df = pd.read_csv("../insurance.csv")

    # Histogramme de l'âge
    plt.hist(df["age"], bins=10)
    plt.title("Distribution de l'âge")
    plt.xlabel("Âge")
    plt.ylabel("Fréquence")
    plt.show()

    # Histogramme du BMI
    plt.hist(df["bmi"], bins=10)
    plt.title("Distribution du BMI")
    plt.xlabel("BMI")
    plt.ylabel("Fréquence")
    plt.show()
```

## 2.👉 Symétrique = forme équilibrée :
- gauche ≈ droite

- asymétrie à droite
    - la majorité des valeurs sont petites ou moyennes
    - mais il existe quelques valeurs très élevées

# Partie 4 : Analyse de la dispersion (Boxplots)
**boxplot :**
- Un boxplot (boîte à moustaches) est un graphique qui permet de voir :
    - la distribution des données
    - la médiane (valeur centrale)
    - la dispersion (étalement)
    - les valeurs extrêmes (outliers)

**Outlier :**
    est une donnée qui est très différente des autres valeurs dans un dataset.

**IQR** = InterQuartile Range
- Il sert à détecter les valeurs anormales.

- Étapes de la méthode IQR :
    - Q1 = 25%
    - Q3 = 75%
    - IQR = Q3 - Q1

- Règle des outliers :
    - Une valeur est un outlier si :
       valeur < Q1 - 1.5 * IQR
            ou
       valeur > Q3 + 1.5 * IQR

**une fonction utilisant l'écart interquartile (IQR) pour calculer précisément le nombre d'outliers dans la colonne bmi.**

```python
df = pd.read_csv("../insurance.csv")

def compter_outliers_iqr(data, colonne):
    Q1 = data[colonne].quantile(0.25)
    Q3 = data[colonne].quantile(0.75)
    IQR = Q3 - Q1

    borne_inferieure = Q1 - 1.5 * IQR
    borne_superieure = Q3 + 1.5 * IQR

    outliers = data[
        (data[colonne] < borne_inferieure) |
        (data[colonne] > borne_superieure)
    ]

    return len(outliers)

print("Outliers BMI :", compter_outliers_iqr(df, "bmi"))
```