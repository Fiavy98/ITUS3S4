#### 4.3 Comparaison et analyse critique 
Réalisez une étude comparative rigoureuse des quatre configurations suivantes 

| Critère | Arbre From Scratch (Max-Minority) | Random Forest From Scratch (Max-Minority) | Decision Tree Scikit-Learn (Gini) | Random Forest Scikit-Learn (Gini) |
|----------|-----------------------------------|-------------------------------------------|-----------------------------------|-----------------------------------|
| Implémentation | Programmée manuellement | Programmée manuellement | Bibliothèque Scikit-Learn | Bibliothèque Scikit-Learn |
| Algorithme de split | Max-Minority | Max-Minority | Indice de Gini | Indice de Gini |
| Nombre d'arbres | 1 | Plusieurs | 1 | Plusieurs (100 par défaut) |
| Construction de l'arbre | `build_tree()` | `build_random_forest()` | `DecisionTreeClassifier()` | `RandomForestClassifier()` |
| Recherche du meilleur split | `trouver_meilleur_split()` | `trouver_meilleur_split()` | Automatique | Automatique |
| Bagging | Non | Oui (`np.random.choice`) | Non | Oui (intégré) |
| Vote majoritaire | Non | Oui | Non | Oui |
| Facilité d'implémentation | Difficile | Très difficile | Facile | Facile |
| Compréhension de l'algorithme | Très élevée | Très élevée | Moyenne | Moyenne |
| Vitesse d'exécution | Moyenne | Moyenne à lente | Rapide | Très rapide |
| Optimisation | Basique | Basique | Optimisée | Très optimisée |
| Utilisation en production | Peu utilisée | Rare | Très utilisée | Très utilisée |
| Résultat attendu | Classification Saine / Malade | Classification Saine / Malade | Classification Saine / Malade | Classification Saine / Malade |
| Accuracy | À mesurer | À mesurer | À mesurer | À mesurer |

## Conclusion

- **Arbre From Scratch** : permet de comprendre le fonctionnement interne d'un arbre de décision.
- **Random Forest From Scratch** : montre comment plusieurs arbres peuvent être combinés grâce au bagging et au vote majoritaire.
- **Decision Tree Scikit-Learn** : fournit une implémentation optimisée et simple à utiliser.
- **Random Forest Scikit-Learn** : est généralement le modèle offrant les meilleures performances grâce à l'utilisation de plusieurs arbres optimisés.



**Question et analyses attendues**
1. Évaluation quantitative : Calculez et affichez sous forme de tableau comparatif l’Accuracy
(exactitude), la Précision et le Rappel (Recall) sur le jeu de test pour chacun des quatre
modèles

# Évaluation quantitative des modèles

L'objectif est de comparer les performances des 4 modèles :

1. Arbre From Scratch (Max-Minority)
2. Random Forest From Scratch (Max-Minority)
3. Decision Tree Scikit-Learn (Gini)
4. Random Forest Scikit-Learn (Gini)

Les métriques utilisées sont :

- Accuracy (Exactitude)
- Precision (Précision)
- Recall (Rappel)


---

# 1. Matrice de confusion

Avant de calculer les métriques, on utilise la matrice de confusion.

| | Prédit Sain (0) | Prédit Malade (1) |
|---|---|---|
| Réel Sain (0) | TN | FP |
| Réel Malade (1) | FN | TP |


## Signification :

- **TP (True Positive)** :
  - Feuille réellement malade
  - Modèle prédit malade

- **TN (True Negative)** :
  - Feuille réellement saine
  - Modèle prédit saine

- **FP (False Positive)** :
  - Feuille réellement saine
  - Modèle prédit malade

- **FN (False Negative)** :
  - Feuille réellement malade
  - Modèle prédit saine


---

# 2. Accuracy (Exactitude)

## Définition :

L'Accuracy mesure le pourcentage total de bonnes prédictions.


## Formule :

\[
Accuracy = \frac{TP + TN}{TP + TN + FP + FN}
\]


## Interprétation :

- Proche de 1 (100%) → modèle performant
- Faible → beaucoup d'erreurs


## Exemple :

Si :

```
TP = 40
TN = 50
FP = 5
FN = 5
```

Alors :

\[
Accuracy = \frac{40+50}{40+50+5+5}
\]

\[
Accuracy = \frac{90}{100}=0.90
\]

Donc :

```
Accuracy = 90%
```


---

# 3. Precision (Précision)

## Définition :

La précision mesure la fiabilité des prédictions positives.

Elle répond à la question :

> Parmi les feuilles prédites malades, combien sont réellement malades ?


## Formule :

\[
Precision = \frac{TP}{TP+FP}
\]


## Exemple :

Le modèle prédit :

```
50 feuilles malades
```

Parmi elles :

```
45 sont réellement malades
```

Donc :

\[
Precision = \frac{45}{45+5}
\]


\[
Precision = 0.90
\]


Donc :

```
Precision = 90%
```


---

# 4. Recall (Rappel)

## Définition :

Le Recall mesure la capacité du modèle à détecter toutes les feuilles malades.

Il répond à la question :

> Parmi toutes les feuilles réellement malades, combien ont été détectées ?


## Formule :

\[
Recall = \frac{TP}{TP+FN}
\]


## Exemple :

Il existe :

```
50 feuilles réellement malades
```

Le modèle en détecte :

```
45
```

Donc :

\[
Recall = \frac{45}{45+5}
\]


\[
Recall = 0.90
\]


Donc :

```
Recall = 90%
```


---

# 5. Comparaison finale des modèles

Les résultats seront présentés sous forme de tableau :


| Modèle | Accuracy | Precision | Recall |
|---|---|---|---|
| Arbre From Scratch (Max-Minority) | valeur | valeur | valeur |
| Random Forest From Scratch (Max-Minority) | valeur | valeur | valeur |
| Decision Tree Scikit-Learn (Gini) | valeur | valeur | valeur |
| Random Forest Scikit-Learn (Gini) | valeur | valeur | valeur |


---

# Analyse des résultats

- Une **Accuracy élevée** indique que le modèle classe correctement la majorité des feuilles.
- Une **Precision élevée** indique que lorsque le modèle détecte une maladie, il se trompe rarement.
- Un **Recall élevé** indique que le modèle détecte la majorité des feuilles malades.

Pour la détection de maladie des feuilles de maïs, le **Recall est particulièrement important**, car manquer une feuille malade (FN) peut être plus grave que détecter une fausse maladie (FP).



# 2. Analyse du comportement des modèles

## Comparaison entre l'algorithme personnalisé et Scikit-Learn

L'algorithme personnalisé (From Scratch) utilise la métrique **Max-Minority** pour choisir les meilleurs splits, tandis que les modèles Scikit-Learn utilisent principalement l'indice de **Gini**.

Les performances peuvent être proches si :

- les données sont suffisamment nombreuses ;
- les features sont bien choisies ;
- l'algorithme de recherche du meilleur split est correctement implémenté.

Cependant, les modèles Scikit-Learn obtiennent souvent de meilleurs résultats car ils utilisent :

- des optimisations internes ;
- des techniques avancées de recherche de seuil ;
- une gestion plus efficace des cas particuliers ;
- une implémentation plus robuste.


---

# Pourquoi une Forêt Aléatoire est plus robuste qu'un arbre unique ?

Un arbre de décision unique dépend fortement des données utilisées pour son apprentissage.

Un changement léger dans les données peut modifier complètement la structure de l'arbre.

La Forêt Aléatoire améliore cette robustesse en combinant plusieurs arbres.


## Fonctionnement :

1. Création de plusieurs sous-ensembles de données grâce au Bagging.

2. Construction de plusieurs arbres différents.

3. Chaque arbre donne une prédiction.

4. La décision finale est obtenue par vote majoritaire.


Exemple :

| Arbre | Prédiction |
|---|---|
| Arbre 1 | Malade |
| Arbre 2 | Malade |
| Arbre 3 | Saine |
| Arbre 4 | Malade |
| Arbre 5 | Malade |


Résultat final :

```
4 arbres sur 5 prédisent malade
```

Donc :

```
La feuille est classée malade
```


---

# Avantages de la Forêt Aléatoire

| Arbre unique | Forêt Aléatoire |
|-|-|
| Un seul modèle de décision | Plusieurs modèles combinés |
| Sensible aux variations des données | Plus stable |
| Risque élevé de sur-apprentissage | Réduction du sur-apprentissage |
| Peut faire des erreurs importantes | Les erreurs sont compensées par le vote |


---

# Conclusion

L'algorithme From Scratch permet de comprendre le fonctionnement interne d'un arbre et peut obtenir des performances proches de Scikit-Learn.

Cependant, les modèles Scikit-Learn sont généralement plus performants grâce à leurs optimisations.

La Forêt Aléatoire est plus robuste qu'un arbre unique car elle combine plusieurs arbres indépendants et utilise un vote majoritaire, ce qui réduit l'impact des erreurs d'un seul arbre.


# 3. Discussion agronomique (Madagascar)

## Contexte

Dans le contexte agricole malgache, un mauvais diagnostic d'une maladie sur les feuilles de maïs peut avoir des conséquences importantes.

Deux types d'erreurs sont possibles :

### Faux Négatif (FN)

Une feuille réellement malade est classée comme saine.

Conséquences :

- La maladie n'est pas détectée.
- L'agriculteur ne traite pas la plante.
- La maladie peut se propager aux autres plants.
- Les pertes de récolte peuvent être importantes.

C'est l'erreur la plus grave.

---

### Faux Positif (FP)

Une feuille réellement saine est classée comme malade.

Conséquences :

- L'agriculteur applique un traitement inutile.
- Augmentation du coût des pesticides.
- Perte économique.
- Impact environnemental.

Cette erreur est moins grave qu'un Faux Négatif, car la récolte reste protégée.

---

# Importance de la Précision et du Rappel

## Précision (Precision)

La précision mesure la fiabilité des prédictions positives.

Elle répond à la question :

> Parmi les feuilles prédites malades, combien sont réellement malades ?

Une précision élevée signifie que le modèle déclenche peu de traitements inutiles.

---

## Rappel (Recall)

Le rappel mesure la capacité du modèle à détecter toutes les feuilles malades.

Il répond à la question :

> Parmi toutes les feuilles réellement malades, combien ont été détectées ?

Un rappel élevé signifie que très peu de feuilles malades sont oubliées.

Dans un contexte agricole, le **Recall est la métrique la plus importante**, car manquer une feuille malade (Faux Négatif) peut entraîner la propagation de la maladie dans toute la parcelle.

---

# Modèle recommandé

Le modèle recommandé est celui qui présente :

- un Recall élevé, afin de détecter le maximum de feuilles malades ;
- une bonne Precision, afin de limiter les traitements inutiles.

Dans la majorité des cas, le **Random Forest Scikit-Learn** est le meilleur choix, car il combine plusieurs arbres de décision, ce qui améliore la stabilité et réduit les erreurs de classification.

---

# Conclusion

Pour un déploiement auprès des techniciens agricoles à Madagascar, il est recommandé d'utiliser **Random Forest Scikit-Learn**, car il offre généralement le meilleur compromis entre **Recall** et **Precision**.

Ce modèle permet de détecter davantage de feuilles malades tout en limitant les faux diagnostics, ce qui contribue à protéger les récoltes tout en réduisant les coûts de traitement.
