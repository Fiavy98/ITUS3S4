## I.Initialisation 
1. mkdir projet
2. git add .
3. git commit -m "Initialisation projet"
4. Connexion a github
	- git remote add origin Url
	- git branch -M main
	- git push -u origin main
---
## Les autre Mambre
1. recupere le projet 
	- git clone url
---
## Creation de la branche Develop : **Une seul fois**
- git checkout -b develop
- git push -u origin develop
---
## Tavail quotidien : **feature branch**
1. M a j
	- git pull origin main
2. creation de la branche de travail 
	- git checkout -b feature/fonctionalite1
3. Travail + commits (sauvegarde localement)
	- git add.
	- git commit -m "Page d'accueil"
4. Push de la feature (livrer dans dev github)
	- git push origin feature/fonctionalite1
5 . quand la fonctionalite est termine
	- git checkout develop
	- git pull origin develop
	- git merge feature/M2-auth
	- git push origin develop
## Tout le monde peut recuperer le branche dev venant de cette feature par
- git pull origin develop

## Livraison du projet deja ok dans Main
- git checkout main
- git pull origin main
- git merge develop
- git push origin main
