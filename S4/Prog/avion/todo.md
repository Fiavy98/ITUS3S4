- Mettre en place le composant 
    - 3 vitesse 
        - aiguille 
        -  faire bouger l'aiguille
        - afficher le valeur du chronometre 
    - compteur 
        - altitude 
        - distance par rapport au piste 
    - info Bar 
        - V de decrochage 
        - limite x et y
    - taille roplane (Faire action lors du bouton) : 
        - vue avant
        - gauche 
        - droite 
        - arriere 
        - en base 
        - dans la piste 
        - le ciel bouge (nuage en general) il bouge aussi si on fait (+/-) le vy

- Faire le fonctionnalites 
    - saisir l'information de l'avion avant de commancer la partie 
        - vitesse : x,y
            - vx : vitesse acceleration
            - vy : sert a remonte / descend
        - altitude (a0)
        - distance par rapport au piste(negatif vers 0)
        - limite freinage (x,y)
        - vitesse de decrochage 
        - distance piste

        - inserer ces parametre  dans txt
    
    - faire le freinage 
        - vx et vy bouge
    - vrai calcul de 
        - vitesse : x , y total
        - altitude (Piste si=0)
        - distance par rapport au piste (0 si dans l'intervale de la piste)
        - on ne peut rien faire si le freinage atteint la limite
    - l'avion s'explose quand la vitesse est inferieur au vitesse de decrochage 
    - atteint le piste si da distance par rapport au piste est 0 (avion dans l'intervale du piste)
    - tout est pausse si on clic sur le bouton pausse

- le vrai calcul (ajouter dans le programme c++)
```c
#include <stdio.h>
#include <math.h>
#include <stdlib.h>

float Vx         = 200.0;
float Vy         = 0.0;
float Vtotal     = 0.0;
float freinage_x = 0.0;
float freinage_y = -36.0;
float altitude   = 1000.0;
float distance   = -50000.0;
float t          = 0.016;
float pasFreinage = 36.0;
float decrochage  = 100.0;

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
            printf("MIPOAKA !\n");
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