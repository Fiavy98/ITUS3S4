## Mots cle 
- **Ctrl + entre pour faire une calcul**
- **Donner un valeur a un variable : x:2** *Kil pour supprimer un valeur*
- **%: signifie le dernier resultat calculer**
- **cree une foncion : f(x):= 2x + 2**
- float : transforme le nb rationnel en decimal 
	- float(1/2) donnee : 0.5
- rationalize : sert a transformer un nombre decimal  en rationnel 
	- rationalize(0.5) donnee : 1/2
- sqrt : racine carre
	- sqrt(9) donnee 3
- floor: prend le nombre avant virgule 
	- floor(88.99) donne 88
- ceiling : nb avant virgule + 1
	- celing(2.1) donne 3
- factor : factorizer un nombre
	- factorfactor(x^2 + 5*x + 6) donne (x+2)·(x+3) 
- expand : developper en expression mathematique 
	- expand((x+2)*(x+3)) donne x^2+5*x+6
	- expand(%pi) donne π
- gcd : donne le pgcd 
	- gcd(123,456) : 12
- lcm : donne le ppcm 

- conjugate : conjguer un nombre complexe
- abs : valeur absolue
	- abs(-12) donne 12
- carg : argument d'un nb complexe 
	- carg(-1 + %i); donne 3π/4
- exponentialize : volena exponentielle 
- fpprec : presission ny nombre havoaka
	- float(sqrt(2)) = 1.414213562373095
	- augmanter la pecission 
		- fpprec:50;
		- puis bfloat(sqrt(2));
		- 1.4142135623730950488016887242096980785696718753769
- sum(expression → ce que tu additionnes, variable qui change , début, fin)
	- sum(i^2, i, 1, 4)  donne 1^2+2^2+3^2+4^2 = 30
# Fonction 
## Introduction 
- cree une fonction :
	## f(x) := 2x + 2
	## g(x):= if x < 2 then x/(x-3) else (x-2) - 2
    ## h(x) := if x >= 0 and x < 2 then x + 3 elseif x >= 2 and x < 5 then x + 4 else 1;
- deriver : 
	- diff(f(x),x,n)
		- n : ordre de la derive
			- n= 1: f'(x)
			- n=2 : f''(x)
	- cree une fonction a partir du derive qui sont reutilisable
		- definie(df(x),diff(x),x)
			- donne df := la derive de f(x)

- courbe : 
	- plot2d(f(x), [x, -5, 5]);
		- “trace la fonction en faisant varier x de -5 à 5”
	- wxplot2d
		- wxplot2d([f(x), df(x)], [x, -5, 5]);
		- wxplot2d([sin(x),cos(x)],[x,-5,5],[y,-2,2]);
			- tracer sin(x) et cos(x)
			- pour x entre -5 et 5
			- et afficher seulement y entre -2 et 2

## Etude complet d'une fonction 
- on a f(x)
1. Dommaine de definition
	- **rationnelle:**
		- on cherche x pour le denominateur
			- f(x) n'est pas defini si on change le valeur du x dans le fonction et on obtient un expression incorrect
				- Df=R\{-2} //x=-2
			- sinon : pour (condition et fonction) ,est une fonction rationnelle toujours  definie 
	- **polynome:** 
		- pour (condition et fonction) est une fonction polynome toujours definie
2. continute et derivabilite
	- **rationnelle :**
		- pour (condition et fonction) est une fonction rationnelle donc toujours continue et derivable dans son domaine
	- **polynome :**
		- pour (codition et fonction) est une polynome toujours definie partout
3. limite a gauche , limite a droite 
	- 'limit(f(x),x,valeur,direction)=limit(f(x),x,valeur,direction)
		- valeur : ca depent du df parfois
		- direction
			- min : a gauche
			- plus : droite
			- inf : infinie
4. derive
	- fonction simple
		- define(df(x),factor(diff(f(x),x)))
	- fonction avec condition
		- ex : g(x):=if x  < 2 then x/(x-3) else(x-2)^2 -2
		- **Derive a gauche :** define(dg(x),factor(diff(x/x-3),x))
		- **Derive a droite :** define(dd(x),factor(diff((x-2)^2 -2,x)))
5. tableau de variation
	- point critique : au centre du tv
	- l'infini : ℝ → −∞,+∞
	- signe de f'(x) 
	- on calul f(−∞,+∞,pt critique)
	- cr. : miakatra
	- dec. : midina


	x     |-∝                       2                             +∝
    ------|----------------------------------------------------------
  	g'(x) |    x/(x-3)                    ∥          (x-2)^2 -2
   -------|----------------------------------------------------------
  	g'(x) |                  -          -3∥0                +
   -------|-----------------------------------------------------------
          | 1                             |                        +∝
          |               dec.            |            cr.
          |                               |-2
----------------------------------------------------------------
6. courbe 
	- wxplot2d([g(x),1,x=-2],[x,-7,5],[y,-5,7],[color,blue,red,red]);
	- wxplot2d([f(x), g(x)], [x, a, b]);

# Equation Polynomiale
- eq de la forme P(x)=0 
	- 2em degre : x^2−3x+2=0
	- 3em degre : x^3+x−1=0
- **resoudre une equation :** *Solve*
	- solve(equation=0,variable)
		- ex : solve(2^x-4=0,x) donne x=-2 et x=2
- **Trouve tous les solution numerique d'une fonction :** *allroots*
	- allroots(x^2 - 2);
- **trouver la racine d'un eq dans un intervale:** *find_root*
	- find_root(expression, x, a, b)
		- trouve une solution de l’équation entre a et b	
- **Metn. Newton pour trouver un racine**
	- newton(f(x), x, x0, precision);
		- load(newton1)$
		- newton(x-2-log(x), x, 0.1, 1E-8);
		- newton(x-2-log(x), x, 3, 1E-8);

# Periodisation
- Une fonction qui se repete a l'infini
