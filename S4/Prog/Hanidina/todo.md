## Projet Jeux avion en C++
- A saissir avannt de lancer le programme 
	- cette donnes sont stocker dans un data.txt
		- id Avion
		- Vitesse : Vx et Vy
		- Altitude (m) : altitude par rapport a avion
		- Distance par rapport au piste (m) : valeur negatif
		- Vitesse de decrochage (km/h)
		- limite Freinage X et Y
		- longueur du piste (km)

- Mettre en Place les compossants 
	- Avion (en 3D)
		- un petit avion dans le ciel
		- vue changable avec le souris
			- en avant , gauche , droite , en bas , en arriere
			- peut voir la piste 
			- peut installer sur la piste  
	- Freinage 
		- Code a conventie en c++ pour pouvoir utiliserdans le projet
		```c
			#include <stdio.h>
			#include <math.h>
			#include <stdlib.h>

			/// INITIALISATION
			float Vx         = 200.0; // a saissir 
			float Vy         = 0.0; // a saisir
			float Vtotal     = 0.0; 
			float freinage_x = 0.0; 
			float freinage_y = -36.0; 
			float altitude   = 1000.0; // a saissir
			float distance   = -50000.0; // a saisir
			float t          = 0.016;
			float pasFreinage = 36.0; // a saisir
			float decrochage  = 100.0; // asaisir

			void mettreAjour(float t);
			void freinage(char boutonX, char boutonY, float *freinage_x, float *freinage_y, float pasFreinage);

			int main() {
			    printf("Ndao hanidina pr !!\n");

			    char boutonX, boutonY;

			    // Saisie des boutons AVANT la boucle (ou dans la boucle si interactif)
			    printf("Bouton X (+/-/n) : "); scanf(" %c", &boutonX);
			    printf("Bouton Y (+/-/n) : "); scanf(" %c", &boutonY);
			    freinage(boutonX, boutonY, &freinage_x, &freinage_y, pasFreinage);

			    int continuer = 1;
			    while (continuer) {
				
			        mettreAjour(t);

			        printf("Vx=%.2f  Vy=%.2f  Vtotal=%.2f  Alt=%.2f  Dist=%.2f\n",
			                Vx, Vy, Vtotal, altitude, distance);

			        // Verification decrochage
			        if (Vtotal < decrochage) {
			            printf("L'avion s'explose !\n");
			            continuer = 0;
			        }

			        // Verification atterrissage
			        if (altitude <= 0 && distance >= 0) {
			            printf("ATTERRISSAGE !\n");
			            continuer = 0;
			        }

			        // Verification crash
			        if (altitude <= 0 && distance < 0) {
			            printf("CRASH avant la piste !\n");
			            continuer = 0;
			        }

			    }   // ← accolade while

			    return 0;
			}
			void freinage(char boutonX, char boutonY, float *freinage_x, float *freinage_y, float pasFreinage) {
			    if (boutonX == '-') *freinage_x -= pasFreinage;
			    if (boutonX == '+') *freinage_x += pasFreinage;
			    if (boutonY == '-') *freinage_y -= pasFreinage;
			    if (boutonY == '+') *freinage_y += pasFreinage;
			}

			void mettreAjour(float t) {
			    Vx     += freinage_x * t;
			    Vy     += freinage_y * t;
			    Vtotal  = sqrt(Vx * Vx + Vy * Vy);

			    float Vx_ms = Vx / 3.6;
			    float Vy_ms = Vy / 3.6;

			    altitude = altitude + Vy_ms * t;
			    distance = distance + Vx_ms * t;
			}	
		```
		- Freinage x (acceleration)
			- bouton + / -
		- Freinage y (monte,descend)
			- bouton + / -
		- Son Action 
			- Freinage x 
				- accelerer l'avion 
				- avec animation de l'avion
				- Appeler le fonction freinage et mise a jour 
			- Freinage y
				- sert a monter/descent 
				- avec animation de l'avion
				- Appeler le fonction freinage et mise a jour
			- on ne peut plus cliquer si le valeur du freinage atteint sa limite

	- Les  compteurs 
			- Vitesse (mettre a jours)
				- Vtotal 
				- l'avion s'explose le le Vtotal< vitesse de Freinage
		- Altitude (mise a jour) 
		- Distance par rapport au piste (mise a jour)
	
	- Les Info Bar
		- Chronometre : 00:00:00 (demarre lors du lancement du projet)
		- Bouton pausse/play : qui va mettre en pausse tout 
		- Vitesse de decrochage
		- limite de freinage (x et y)
