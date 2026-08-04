#include <stdio.h>
#include <stdlib.h>

// ---------- Prototypes ----------
int ReadData(const char *filename, double x[], double y[]);
void moindreCarre(int N, double x[], double y[], double *a, double *b);
double erreurTotale(int N, double x[], double y[], double a, double b);
void AffResult(int N, double x[], double y[], double a, double b, double D);

// ---------- MAIN ----------
int main() {
    printf("Calcul de l'ajustement lineaire par la methode des moindres carres\n");

    double x[100], y[100], a = 0.0, b = 0.0, D = 0.0;
    int N;

    // Lecture des donnees
    N = ReadData("data.txt", x, y);
    if(N <= 0) return 1;

    // Calcul de la droite d'ajustement
    moindreCarre(N, x, y, &a, &b);

    // Calcul de l'erreur totale
    D = erreurTotale(N, x, y, a, b);

    // Affichage des resultats
    AffResult(N, x, y, a, b, D);

    return 0;
}

// ---------- Lecture des donnees ----------
int ReadData(const char *filename, double x[], double y[]) {
    FILE *f = fopen(filename, "r");
    if(f == NULL) {
        printf("Erreur : impossible d'ouvrir le fichier %s\n", filename);
        return -1;
    }

    int N;
    fscanf(f, "%d", &N); // Lire le nombre de points

    for(int i = 0; i < N; i++){
        fscanf(f, "%lf,%lf", &x[i], &y[i]);
    }

    fclose(f);
    return N;
}

// ---------- Methode des moindres carres ----------
void moindreCarre(int N, double x[], double y[], double *a, double *b){
    double sumx = 0.0, sumy = 0.0, sumxy = 0.0, sumx2 = 0.0;

    for(int i = 0; i < N; i++){
        sumx  += x[i];
        sumy  += y[i];
        sumxy += x[i] * y[i];
        sumx2 += x[i] * x[i];
    }

    *a = ((N * sumxy) - (sumx * sumy)) / ((N * sumx2) - (sumx * sumx));
    *b = (sumy - (*a * sumx)) / N;
}

// ---------- Calcul de l'erreur totale ----------
double erreurTotale(int N, double x[], double y[], double a, double b){
    double D = 0.0, y_droite = 0.0, erreur = 0.0;

    for(int i = 0; i < N; i++){
        y_droite = a * x[i] + b;
        erreur = y[i] - y_droite;
        D += erreur * erreur;
    }

    return D;
}

// ---------- Affichage ----------
void AffResult(int N, double x[], double y[], double a, double b, double D){
    printf("\nPoints :\n");
    for(int i = 0; i < N; i++){
        printf("x%d = %.3f , y%d = %.3f\n", i, x[i], i, y[i]);
    }

    printf("\nNombre de points N = %d\n", N);
    printf("Droite d'ajustement : y = %.5f x + %.5f\n", a, b);
    printf("Erreur totale D = %.5f\n", D);
}
