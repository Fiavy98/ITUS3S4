# Chargement
### Afficher le 5 premier ligne 
    - print(df.head())

### Les differents variable presentes 
- age
- sex
- bmi
- Children
- smoker
- region
- charges


## Dimensions
### nombre d' Observations = 1338
- print(df.shape[0])

### nombre de variable = 7
- print(df.shape[1])

--
## Type de donnees 
**- print(df.info())**

    <class 'pandas.DataFrame'>
    RangeIndex: 1338 entries, 0 to 1337
    Data columns (total 7 columns):
     #   Column    Non-Null Count  Dtype  
    ---  ------    --------------  -----  
     0   age       1338 non-null   int64  
     1   sex       1338 non-null   str    
     2   bmi       1338 non-null   float64
     3   children  1336 non-null   float64
     4   smoker    1336 non-null   str    
     5   region    1338 non-null   str    
     6   charges   1338 non-null   float64
    dtypes: float64(3), int64(1), str(3)
    memory usage: 73.3 KB
    None

# Les types sont-ils cohérents (logique) ?
Oui, globalement les types attribués par défaut sont cohérents
    Les variables numériques sont bien en int64 / float64
    Les variables catégorielles sont en str

-- 

## Optimisation
**Optimisation : Pour les variables textuelles (sex, smoker, region), convertissez-les au type category de Pandas. Quel est l'avantage de faire cela ?**
- Avantage : 
    - moins de memoire utilisée
    - Calculs plus rapides
    - meilleure gestion des variables qualitatives

--
# Partie 2 : Analyse Descriptive et Qualité

### 1. Résumé statistique
**Question d'interprétation : Quel est l'âge moyen des bénéficiaires ? Que pouvez-vous dire sur le BMI moyen par rapport aux normes de santé ?**
- Age moyen : 39.207025 
- bmi moyen :  30.663397 

**- L’âge moyen des bénéficiaires est d’environ 39 ans, ce qui indique une population principalement adulte.**
**- La moyenne est au-dessus de 30**
**→ Donc la population est en zone d’obésité**

### 2. Données manquantes : Vérifiez si le dataset contient des valeurs nulles ou manquantes (NaN). Si oui, comment devriez-vous les traiter ?
**isnull().sum()**
- children    2
- smoker      2

**Comment le traiter ?**
- 1. Supprimer les lignes
    - df.dropna()
- 2. Remplacer par une valeur
    - Pour numerique
        - df["children"].fillna(df["children"].mean()) ## Remplacer par le moyenne
    - Categoriel
        - df["smoker"].fillna("unknown")

### 3. Analyse des extrêmes
En observant la colonne charges, on remarque que la valeur maximale est d’environ 63 770, alors que 75% des individus ont des charges inférieures à environ 16 640.

Cela montre qu’il existe des valeurs très élevées par rapport au reste des données. Ces valeurs peuvent être considérées comme des valeurs extrêmes (outliers).

Cependant, dans le domaine de l’assurance, ces montants élevés peuvent être justifiés par des cas particuliers (maladies graves, personnes fumeuses, traitements coûteux). Ils ne sont donc pas forcément des erreurs.

# Partie 3 : Visualisation univariée (Distribution)

### 1-Question : La distribution du BMI suit-elle une forme particulière (ex: loi normale) ?
    Dans le dataset insurance, le BMI :
est souvent autour de 25 à 35
mais avec une queue vers les valeurs élevées (jusqu’à ~53)
donc la distribution n’est pas parfaitement normale
mais elle est proche d’une forme quasi normale légèrement asymétrique à droite

La distribution des charges n’est pas symétrique. Elle présente une asymétrie à droite, ce qui signifie que la majorité des individus ont des charges relativement faibles, tandis qu’une minorité de personnes paient des montants très élevés.

### 2.Les charges : Affichez la distribution de la variable charges. Est-elle symétrique ? Que signifie une "asymétrie à droite" dans ce contexte financier ?

Dans un contexte financier, cette asymétrie indique que quelques cas exceptionnels (par exemple des clients avec des maladies graves ou des comportements à risque comme le tabagisme) génèrent des coûts très importants, ce qui tire la distribution vers des valeurs élevées.

### 3. Variables catégorielles : Créez un graphique (ex: countplot) pour compter le nombre d'individus par région et par statut de fumeur. Les classes sont-elles équilibrées ?

Les classes ne sont pas toutes équilibrées. La variable region est globalement équilibrée, car les effectifs sont similaires entre les différentes régions. En revanche, la variable smoker est déséquilibrée, car le nombre de non-fumeurs est largement supérieur à celui des fumeurs.

# Partie 4 : Analyse de la dispersion (Boxplots)

## 2. Identification des outliers : Laquelle de ces variables présente le plus de valeurs atypiques ?

| Variable | Outliers ?   |
|----------|--------------|
| age      | non          |
| bmi      | quelques     |
| charges  | oui beaucoup |


→ harges a le plus d’outliers

**une fonction utilisant l'écart interquartile (IQR) pour calculer précisément le nombre d'outliers dans la colonne bmi.**

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