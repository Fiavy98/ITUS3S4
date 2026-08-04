#include <stdio.h>
#include <math.h>

// Déclaration des fonctions
float f(float x);
void affResult(float a,float b,int n,float aire);
float Simps(float a,float b,int n);
float simps_eps(float a, float b, float eps, int *n_final);

int main(){
    printf("Calcul de l'integrale de f entre a et b par la methode de Simpson \n");

 /// Données
    float a = 0, b = 1;
    int n = 30;
    
    float eps = 1e-6;
    int n_final;
    
/// Calcul simple avec n sous-intervalles
    float aire_simple = Simps(a, b, n);

/// Calcul avec raffinement adaptatif
    float aire_eps = simps_eps(a, b, eps, &n_final);

/// Affichage
    printf("Calcul simple :\n");
    affResult(a, b, n, aire_simple);
    printf("Calcul avec precision eps = %g :\n", eps);
    affResult(a, b, n_final, aire_eps);

    return 0;
}

/// Raffinement adaptatif avec eps
float simps_eps(float a, float b, float eps, int *n_final){
    int n = 2;  // nombre initial de sous-intervalles
    float aire1 = Simps(a, b, n);
    n *= 2;
    float aire2 = Simps(a, b, n);

    while(fabs(aire2 - aire1) > eps){
        aire1 = aire2;
        n *= 2;
        aire2 = Simps(a, b, n);
    }

    *n_final = n;
    printf("\nConvergence atteinte pour n = %d sous-intervalles.\n", n);
    return aire2;
}

float Simps(float a, float b, int n){
    float h = (b - a) / n;
    float s1 = 0.0;  // somme f(x_i)
    float s2 = 0.0;  // somme f(x_i + h/2)

    // Somme f(x_i) pour i=1..n-1
    for(int i = 1; i < n; i++){
        float xi = a + i*h;
        s1 += f(xi);
    }

    // Somme f(x_i + h/2) pour i=0..n-1
    for(int i = 0; i < n; i++){
        float xx = a + i*h + h/2.0;
        s2 += f(xx);
    }

    float I = (h / 6.0) * ( f(a) + 2*s1 + f(b) + 4*s2 );

    return I;
}


/// Affichage des résultats
void affResult(float a,float b,int n,float aire){
    printf("\tIntervalle [%g, %g] : aire = %f\n", a, b, aire);
    printf("\tNombre de sous-intervalles = %d\n", n);
}

/// Fonction f(x)
float f(float x){
    return exp(sin(5*x));
}

