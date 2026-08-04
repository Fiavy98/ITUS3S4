## 14-04-2026

### Config:
On a des input de materiel, exemple:
- Appareil: radio
- Consommation: 10w (watt)
- Heure de début: 18h
- Heure de fin: 20h

On a des tranche d'heure, configurable:
- 06-17h: Journée
- 17-19h: Soirée
- 19-06h: Nuit

Durant la journée, on utilise un panneau solaire source d'energie en w ou en kw, la soirée le même panneau mais avec une puissance réduite de n% (configurable), et la nuit une batterie en wh ou en kwh (killo-Watt-Heure)

### Charge la batterie:
La batterie se recharge dans la configuration journée, pour exemple ici de 6 à 19h, elle se recharge pendant 13h avec le panneau solaire, si la capacité de ma batterie est de 1kwh, et que mon panneau produit 100w, il me faut 10h pour la recharger, mais nous on cherche la puissance de charge à utilisé pendant les 13h, donc vers 76.92w

### Consommation Journée:
On peut consideré que plusieurs appareil peuvent etre utiliser en même temps, exemple radio  6-10h 10w, et télé 8-12h 100w, du coup on a besoin d'un panneau qui supporte 186.92w parce que entre 8-10h on utilise la télé de 100, une radio de 10 et on charge une batterie de 76.92

### Configuration soirée:
A propos de la configuration soirée: la puissance du panneau est réduite de n%
On considere alors un panneau capable de faire la recharge optimale de la batterie avec une période ou la puissance du panneau est réduite de n%, et on peut aussi utilisé d'autres appareils durant cette période

### Configuration nuit:
On utilise uniquement la batterie pour les appareils de cette période
Par exemple je veux utilisé une télé de 100w pendant 1h, il me faut 100wh, et une ampoule de 50w, on aura besoin de 150wh, puisque l'on additionne les 100wh et les 50wh

### Théorique et pratique:
On vient de caluler la puissance théorique du panneau et la capacité théorique de la batterie
- Pour obtenir la puissance pratique, on considère que la totalité de la puissance théorique - - équivaut à m% de la puissance pratique
- Pour la capacité pratique, c'est la capacité théorique + p% d'elle même
m% et p% configurables;

### Output:
L'output demandé:
- puissance pratique du panneau 
- capacité pratique de la batterie

### Aléa
Ajouter un nouveau panneau ; on a deja P1 , celui avec 40% de capa
On a un nouveau panneau P2, 30%, et le truc nous motre quelle puissance si on choisis P1 ou P2

On affiche aussi le converstissseur requis en w convertisseur : consommation pic * 2 dans la journée entiere (6h-6h demain)

Du coup on affiche les détails de P1, P2, Batterie et connvertisseur


### Alléa : 
changement 1:
le calcul de la charge de la batterie est maintenant sur la capacité pratique mais non théorique 

changement 2:
On efface de l'ui les modification d'hier (p2 et conservateur), seulement de l'ui, pas besoin de cassé le back-end

changement 3:
plusieurs type de panneau :

- input
libelle: 
prix unitaire:
energie unitaire (pratique):
rendement: le % de l'energie pratique, comme le 40% et le 30% de P1 et P2 d'hier
ajouter un panneau
on doit trouver combien de chaque panneau pour réussir à composer la puissance théorique dont on a besoin d'après les calcul existant
le nombre des panneau P doivent être entier 

- output:
P1 et prix total P1
P2 et prix total P2
P3 et prix total P3
...
choix : (le plus petit prix total)

As-tu des questions ???


## 21-04-2026
### Changement:
- On néglige la présence d'une batterie et la période de nuit (plus rien de tout ça dans nos calculs)
- On cherche toujours le pic de consommation dans la journée et la soirée 
- lors de la soirée, la puissance du panneau est toujours de -n% (comme ce qui est déjà là)
- Ajouter 3 champs : 
    - un champ pour insérer un chiffre en watt-heure,
    - un autre champ pour le prix de l'énergie selon le chiffre lors des jours férier 
    - et lors des jours ordinnaires

### Ce qu'on veut:
- d'apres le pic de consommation dans un cycle, par exemple 300 watt, on calcul combien d'argent on gagne pour un jour ordinnaire et pour les jours fériers. Par exemple j'ai une Tv de 100w de 8-11h, et un réfrigérateur de 200w de 10-12h, donc pour le pic on a 300w de 10-11h, là ça reste encore dans nos calculs actuels, ce qu'on veut maintenant c'est l'argent rapporter, pour notre exemple on va dire 1000ar pour 100wh jour ordinnaire, 2000ar jour férier, puisque notre pic de consommation est de 10-11h (1 heure), on a donc 200w de disponnible de 8-10h, 100w de disponnible de de 11-12h, et le reste de la journée 300w, du coup on obtient combien lors du jour ordinnaire et du jour férier