## — Partie 1 : Feature Engineering (Maïs / Rouille Polysora)
- *Transformation de variables pour ameliorer l'analyse ou modele*
    - ex : 
    Donne origine : dt de naissance 2003 --> Nouvelle feature cree : Age 23 ans 
    Pixel -> matrice Point

### A faire 1
- **QUOI**
    - Transformer le image pixel en vecteur Caracteristique 
        - image_01.jpg → [0.35, 12, 1450, 0.72]
        - Vecteur 
            - Moyenne de la couleur 
            - texture 
            - surface de zones malades 
- **POURQUOI**
    - Les model d'analyse ou de machine learning travaillent mieux (performant) 
    avec de caracteristique qu'avec des millions de pixel brutes 
- **COMMENT**
    - 0. on utilise les bibliotheque 
        - OpenCV
        - NumPy
    - 1. Analyse de couleur (Espace HSV : un modele de presentation de couleurs)
        - *H: Hue (teinte), S : Saturation (intensite de la couleur), V : value (la liminosite)*
            - a. Convention l'image RGB en HSV  
            ```py
                <!-- On  utilise le fonction -->
                 cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
            ```
            - b. Deffiniser un masque de couleurs 
                - les pixel rechercher (mis en 1 ou blanc)
                    - jaune , orange , marron 
                - les autre : mise en 0 ou noire
                
            - c. Caracteristique X1 : pct_rouille : pourcentage de la feuille toucher par la rouille 
                ```py
                    pct_rouille = Nombre de Pixels de rouille / Nombre total de pixels de la feuille
                ```   
    - 2. Extraction de texture et rugosite (Filtre de Sobel)
        - *sobel : dececteurs du contours -> estimation de l'etat du feuille*
            - ✔ Sobel détecte les bords / changements de couleur
            - ✔ puis on calcule une valeur (moyenne ou variance)
        -  Les pustules de rouille créent une irrégularité tactile et visuelle sur la feuille. Appliquez un filtre de Sobel pour détecter les contours et les variations brusques d’intensité
            ```py
                <!-- Sobel = détecte les bords / variations -->
                cv2.Sobel(img, cv2.CV_64F, 1, 0, ksize=3)
                //img → image d’entrée
                // cv2.CV_64F → type de résultat (valeurs avec décimales, plus précis)
                //1, 0 → direction du calcul :
                    //1, 0 = détecte les bords horizontaux
                    //0, 1 = détecte les bords verticaux
                //ksize=3 → taille du filtre (3×3 pixels)
            ```
        - Calcul du caracteristique X2 : Rugosite (on mesure a quelle point le bord changent dans l'image)
            - Moyenne (quantité de bords : petit → peu de taches, grand → beaucoup de taches) :  ```py sobel.mean() ``` 
            - Variance (intensité des différences de bords) : ```py sobel.var() ```
    
    - 3. Extraction d'un feature de votre choix 
        - On transforme l’image en plusieurs informations utiles (features) pour aider à classer : saine (0) / malade (1) 
        
    - **Livrabre intermetiaire :** Un tableau Pandas DataFrame contenant la structure suivant 
        - ID_Image 
        - pct_rouille
        - rugosite 
        - **mon_variable : Moyenne de la couleur verte (indice de santé de la feuille) = np.sobel.mean()**
        - label malade (malade : 1 , saine : 0)

## Partie 2 : Le Défi Algorithmique — L’Indice "Max-Minority"
- *On construit un arbre de decission dans cette partie*
prediction : desicion a partir d'un intervale (x et y)
classement : decission manierre conditionnel (if/else)
Split : regle qui divise le donnes en 2 groupe (noeud) pour construire un arbre de decission
Noeude :  point de decission dans l'arbre 
                pct_rouille > 20 ?
                   (nœud)
                  /      \
               Oui        Non
            malade      saine
            
### Fondement Mathematique 
    - la purete d'un noeud : P(t) = max(nc/N) : Qualite d'un noeud 
    - nc : valeur 
    - N : Total
    - ex: si un noeud contient 90% feuille saine et 10% malades 
    sa purete est P(t) = max(90/100,10/100)= 90/100 = 0.9
    si le noeud est parfaitement  melange (50/50) alors P(t)= 0.5  

### Algorithme de calcul du meilleur split (split.py)
**But :** Trouver le meilleur seuil (split) pour separer le donnees 
en 2 et construire un meilleur arbre dont le but est d'avoir la purete de cet arbre (P_split)
- seuil : une valeur limite pour faire une separation
    - ex : pct_rouille < 50

1. Trier les valeur  : 
    - On prend une variable (ex : pct_rouille)
        - avant : 
        | pct_rouille | label |
        | ----------- | ----- |
        | 35          | 1     |
        | 5           | 0     |
        | 20          | 1     |
        | 10          | 0     |

        - apres :
        | pct_rouille | label |
        | ----------- | ----- |
        | 5           | 0     |
        | 10          | 0     |
        | 20          | 1     |
        | 35          | 1     |

2. Tester plusieurs seuils (splits)
- On teste des valeurs entre deux valeurs consécutives.
    - Valeurs triées : 5   10   20   35
    - Les seuils candidats sont : 7.5   15   27.5

3. Separer les donnees 
- Par exemple avec le seuil 15 :
- Gauche (G) :
| pct_rouille | label |
| ----------- | ----- |
| 5           | 0     |
| 10          | 0     |

- Droite (D) :
| pct_rouille | label |
| ----------- | ----- |
| 20          | 1     |
| 35          | 1     |


4. Calculer la purete :
- On calcul :
    - la purete du groupe gauche (G)
    - la purete du groupe droite (D)
- puis on le combine 
    - P_split = (G/N) x P(G) + (D/N) x P(D)

- Example :
    - Supposons que l'on teste le split : pct_rouille < 15
    - Groupe gauche 
        | Feuille | Label      |
        | ------- | ---------- |
        | A       | Saine (0)  |
        | B       | Saine (0)  |
        | C       | Malade (1) |

        Il y a : 2 saines , 1 malade, Total = 3
        - La pureté est : P(G) = max(2/3, 1/3) = 2/3 = 0,67

    - Groupe droite 
    | Feuille | Label      |
    | ------- | ---------- |
    | D       | Malade (1) |
    | E       | Malade (1) |
    | F       | Malade (1) |

    Il y a : 0 saine , 3 malades , Total = 3
        - La pureté est : P(D) = max(0/3, 3/3) = 1
    - Calcul de la pureté pondérée du split 
        - |G| = 3 , |D| = 3 , N = 6
    - Donc P_split = (3/6 × 0,67) + (3/6 × 1)  = 0,835 

### Travail a faire 
Ecrivez une fonction Python trouver_meilleur_split(X_column,y)
qui teste tous les seuils possibles pour une variables donnee 
et retourne le seuil s qui maximisera  P_split , ainsi que la valeur de cette purete 

```py
    def  trouver_meilleur_split(X_column,y) :
        # X_column : array-like des valeurs de la variable a tester
        # y : array-like des labels correspondants (0 ou 1)
        
        # etape 1 : Trier les donnees par X_column
        np.argsort()

        # etape 2 : Initialiser les variable pour suivre les meiller seuil et la purete maximal
        meilleure_seuil = None
        meilleure_purete = -1

        # etape 3 : Parcourir les seuils candidats et calculer P_split pour chacun
         
        # etape 4 : Retourner le seuils optimal et sa purete associee 
```

## Partie 3 : Arbres et Forets 
**BUT :** Construire et comparer un arbre de decission (et une foret aleatoire)

### 4.1 Implémentation "From Scratch" (Fait Maison)
- Programmer l'algorithme de l'arbre de decision  
- En utilisant la fonction trouver_meilleur_split développée à la Partie 2, vous devez pro-
        grammer vos propres structures prédictives
    
    1. **L’Arbre de Décision Max-Minority** : CONSTRUIRE UN ARBRE ET DECIDER DANS MODEL SI SAINE OU MALADE 
        - fonction build_tree(X, y, depth, max_depth) : Construire une arbre et cree un noeuds jusqu a obtenir des feuilles finales qui donnent une decission (saine / malades) : purete=1

            - depth : nos position dans l'arbre
            - max_depth : nos position maximal
        - On commance avec toute les donnnees (100 feuille : saine - malade)
        - chercher le meilleur split : trouver_meilleur_split()
        - Diviser les donnees 
                      Toutes les feuilles
                    |
             pct_rouille < 20 ?
              /              \
          Gauche             Droite
        - appeler le fonction encore une fois (recursion)
            - Pour chaque groupe 
                - build_tree(gauche)
                - build_tree(droite)
        - Condition d'arret 
            - si le noeud est pur 
            - Profondeur maximal atteinte 

    2. **Le Random Forest Max-Minority** : model_training.py
    => Construire plusieurs arbre personnaliser avec de donnees differents puis combiner leur prediction avec le majoritaire 
    - Etape : 
        - Cree sous ensemble de donnees (Bagging) : np.random.choice()
        - Construire plusieurs arbre : build_tree()
        - Cree la fonction d'entrainnement Random Forest : def build_random_forest(X, y, n_trees, max_depth)
        - Predire avec tous les arbres 
        - predict_tree()
        - Vote majoritaie : Choisir la classe qui apparait plus : np.bincount()

### 4.2 Utilisation des modèles de Scikit-Learn : model_training.py
     Entrainner le model du Machine Learning  
        - Separer le donnees 
            - 80% → données d'entraînement
            - 20% → données de test
        - Entraîner un arbre Scikit-Learn : 
            - DecisionTreeClassifier(criterion="gini", max_depth=5)
                - criterion="gini" : Critere utiliser pour choisir le meilleur split (indice de Gini pour mesurer la pureté des noeuds)
                - max_depth=5 : limite de l'arbre

                - le model apprend 
                    - le meilleur splits 
                    - les seuils
                    - les règles de décision (classer)

            - Entraîner une forêt aléatoire
                - RandomForestClassifier(n_estimators=100) : 
                    - n_estimators=100 : nb d'arbre de decision utiliser dans le forets 
                    - => Construit 100 arbres et combine leur  décision


### 4.3 Comparaison et Analyse critique 
1. Arbre " From  Scratch" (Metrique Max-Minority)
- construire l'arbre avec : build_tree(X_train,y_train,depth=0,max_depth=5)
    => construire l'arbre et cree les noeud pour obtenir un feuille final 
    qui donnent : saine/malade
- Faire des prediction sur X_test : predict_tree(arbre, sample)
- comparer le prediction avec y_test
- Calculer 
    - Accuracy : Pourcentage de prediction correctes : accuracy_score()
    - Precision : Combien son reelement malades : precision_score()
    - Recall : Combien on ete detecter par le modele : recall_score()

2. Votre Random Forest "From Scratch" (Métrique Max-Minority).
- Construire le foret avec : build_random_forest(    X_train,y_train, n_trees=10, max_depth=5)
    => Construire plusieurs arbre personnaliser avec de donnees differents puis combiner leur prediction avec le majoritaire 

- Faire des prediction sur X_test : predict_forest
- Comparer les prediction avec y_test
- Calculer : Accuracy , Precision, Recall

## 5 Partie 4 : Déploiement d’une Application Web avec Streamlit