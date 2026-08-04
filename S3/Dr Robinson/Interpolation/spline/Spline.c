#include <stdio.h>
#include <stdlib.h>

int getData(char *file, float x[], float y[]);
void affResult(int N, float x[], float y[], float X, float S);
float splineInterp(int N, float x[], float y[], float X);

int main() {
    printf("Interpolation par la methode des splines cubiques\n");

///Donness
    float x[20], y[20];   // Coordonnées des points
    int N;                // Nombre de points
    float X = 3;          // Valeur à estimer
    float S = 0.0;        // Valeur interpolée

///Traitement
    // Lecture des points
    N = getData("pt.txt", x, y);
    if(N <= 0){
        printf("Erreur dans la lecture des points\n");
        return 1;
    }

    // Interpolation par splines
    S = splineInterp(N, x, y, X);

///Sortie du resultat
    affResult(N, x, y, X, S);

    return 0;
}


float splineInterp(int N, float x[], float y[], float X) {
    float h[20], a[20], b[20], c[20], d[20];
    float alpha[20], l[20], mu[20], z[20];
    
    // Étape 1 : calculer h[i] et a[i]
    for(int i=0; i<N-1; i++){
        h[i] = x[i+1] - x[i];
        a[i] = y[i];
    }
    a[N-1] = y[N-1];

    // Étape 2 : construire le système tridiagonal
    for(int i=1; i<N-1; i++){
        alpha[i] = (3.0/h[i])*(a[i+1]-a[i]) - (3.0/h[i-1])*(a[i]-a[i-1]);
    }

    // Étape 3 : résoudre le système (méthode de Thomas)
    l[0] = 1.0; mu[0] = 0.0; z[0] = 0.0;
    for(int i=1; i<N-1; i++){
        l[i] = 2*(x[i+1]-x[i-1]) - h[i-1]*mu[i-1];
        mu[i] = h[i]/l[i];
        z[i] = (alpha[i] - h[i-1]*z[i-1])/l[i];
    }
    l[N-1] = 1.0; z[N-1] = 0.0; c[N-1] = 0.0;

    for(int j=N-2; j>=0; j--){
        c[j] = z[j] - mu[j]*c[j+1];
        b[j] = (a[j+1]-a[j])/h[j] - h[j]*(c[j+1]+2*c[j])/3.0;
        d[j] = (c[j+1]-c[j])/(3.0*h[j]);
    }

    // Étape 4 : trouver l'intervalle contenant X
    int i = N-2; // par défaut dernier intervalle
    for(int j=0; j<N-1; j++){
        if(X >= x[j] && X <= x[j+1]){
            i = j;
            break;
        }
    }

    // Étape 5 : calculer S(X)
    float dx = X - x[i];
    float S = a[i] + b[i]*dx + c[i]*dx*dx + d[i]*dx*dx*dx;

    return S;
}


void affResult(int N, float x[], float y[], float X, float S){
    printf("\n");
    for(int i=0; i<N; i++){
        printf("x%d=%f , y%d=%f\n", i, x[i], i, y[i]);
    }
    
    printf("\nDimension N=%d\n", N);
    printf("Valeur a estimer X=%f\n", X);
    printf("Valeur interpolee S(%f)=%f\n", X, S);
}


int getData(char *file, float x[], float y[]){
    FILE *f = fopen(file,"r");
    if(f==NULL){
        printf("Erreur : impossible d'ouvrir le fichier %s\n", file);	
        return -1;
    }
    
    int N;
    if(fscanf(f,"%d",&N)!=1){
        printf("Erreur lecture N\n");
        fclose(f);
        return -1;
    }
    
    for(int i=0; i<N; i++){
        if(fscanf(f,"%f,%f",&x[i],&y[i])!=2){
            printf("Erreur lecture point %d\n", i);
            fclose(f);
            return -1;
        }
    }
    
    fclose(f);
    return N;
}
