# Gestion Stock 
- Fenetre 
    1. Ajouter article
        - id
        - nom
        - pu
    2. mouvement stock
        - id_article (liste deroulante )
        - Type
            - entrer
            - sortie 
        - qte
        - dt
        - pu (Juste confirmation , cette champ remplit directement le pu du article choisit qui est modifiable)
    3. etat stock actuel 
        - Filtrage par date 
            - si on ne choisit pas encore la date , il affiche la date ajourdhui
        - Choix entre FIFO/LIFO/CUMP
            - FIFO = somme (Qte sortie x  Prix des lots le plus enciens )
            - LIFO = somme (Qte sortie x Prix de lots le plus recents )
            - CUMP = Valeur total Stock / Quantite total en stock (Valeur totale=∑(Quantiteˊ entreˊe×Prix d′achat))
                - 10 unités à 1000
                - 10 unités à 2000
                - CUMP=(10×1000)+(10×2000) / 20


        - Date : date du mvnt
        - Quantite (+/-)
        - PU
        - Valeur = Qte x PU
        - Stock (Qte) : stock restant
        - Valeur stock : valeur financiere du stock restant 
            - CUMP = Valeur Stock=Quantiteˊ restante×CUMP
            - FIFO = la somme des lots restants les plus récents/non consommés.
            - LIFO = on retire les derniers lots d’abord.

NB : 
-  Generaliser le code 
- Le Systeme doit toujours fonctionner meme si on ajoute une autre classe
- Methode de Valorisation du stock
    - FIFO
        - izay produits tonga voalohany no avoaka voalohany 
            - 10 produits a 1000 ar
            - 10 poduits a 2000 ar
                - on vend 5 produits , 
                    -> on prend le l'ancien d'abord (1000 ar)
        - LIFO
            - Produits niditra farany no avoaka voalohany (ici 2000)
        - CUMP
            - Calcul du prix moyen 


- Database stock
    - article 
        - id
        - nom
        - pu
    - mvntSock
        - id
        - id_article
        - idType
        - quantite
        - date
    - typeStock
        - id (not auto_increment) : ENTRE / SORTIE
        - libelle
    - EtatStock (View)
        - Choix entre FIFO/LIFO/CUMP
            - FIFO = somme (Qte sortie x  Prix des lots le plus enciens )
            - LIFO = somme (Qte sortie x Prix de lots le plus recents )
            - CUMP = Valeur total Stock / Quantite total en stock (Valeur totale=∑(Quantiteˊ entreˊe×Prix d′achat))
                - 10 unités à 1000
                - 10 unités à 2000
                - CUMP=(10×1000)+(10×2000) / 20


        - Date : date du mvnt
        - Quantite (+/-)
        - PU
        - Valeur = Qte x PU
        - Stock (Qte) : stock restant
        - Valeur stock : valeur financiere du stock restant 
            - CUMP = Valeur Stock=Quantiteˊ restante×CUMP
            - FIFO = la somme des lots restants les plus récents/non consommés.
            - LIFO = on retire les derniers lots d’abord.

- Organisation du dossier 

│
├── controllers/
│   ├── ArticleController.java
│   ├── MvntStockController.java
│
├── services/
│   ├── ArticleService.java
│   ├── MethodeService.java
    ├──StockService.java
│
├── dao/
│   ├── GenericDAO.java
│   ├── ArticleDAO.java
│   ├── MvtStockDAO.java
│
├── models/
│   ├── Article.java
│   ├── MouvementStock.java
│   ├── TypeStock.java
│
├── views/
│   ├── AjoutStockView.java
│   ├── MvntStockView.java
│   ├── EtatStockView.java
│   ├── MainFrame.java
│
├── db/
│   ├── ConnectionDB.java
│
└── Main.java

un menu a chaque Page 
    - Ajouter article
    - Mouvements
    - Etat stock