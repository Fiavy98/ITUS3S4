#include <stdio.h>
#include <math.h>
#include <stdlib.h>

/* Normalise un vecteur x -> y = x / ||x|| */
void normaliser(double *x, double *y, int n);

/* Multiplie la matrice A par le vecteur y -> x_new = A * y */
void produitMatVec(double **A, double *y, double *x_new, int n);

/* Calcule le produit scalaire <y, x> */
double produitScalaire(double *y, double *x, int n);

/* Calcule la norme ||x|| */
double norme(double *x, int n);

/* Algorithme principal des puissances iterees */
double puissanceIteree(double **A, double *x0, double *vectPropre,
                       int n, double epsilon, int maxIter, int *nbIter);

/* Affiche les resultats */
void displayResult(double valPropre, double *vectPropre,
                   int n, int nbIter, double epsilon);

/* Affiche une matrice */
void afficherMatrice(double **A, int n);

/* Affiche un vecteur */
void afficherVecteur(double *v, int n, const char *nom);

/* Alloue une matrice n x n */
double **allouerMatrice(int n);

/* Libere une matrice n x n */
void libererMatrice(double **A, int n);

int main() {
    int n;          /* Taille de la matrice */
    double epsilon; /* Critere d'arret */
    int maxIter;    /* Nombre max d'iterations */
    int nbIter;     /* Nombre d'iterations effectuees */

    /* ---- Initialisation des donnees ---- */
    n       = 5;
    epsilon = 1e-6;
    maxIter = 1000;

    /* Allocation dynamique */
    double **A       = allouerMatrice(n);
    double *x0       = (double *)malloc(n * sizeof(double));
    double *vectPropre = (double *)malloc(n * sizeof(double));

    /* Matrice A (exemple : matrice symetrique 3x3) */
    A[0][0] = 2.0;  A[0][1] = -1.0;  A[0][2] = 0.0;  A[0][3] = 0.0;   A[0][4] = 0.0;
    A[1][0] = -1.0;  A[1][1] = 2.0;  A[1][2] = -1.0; A[1][3] = 0.0;   A[1][4] = 0.0;
    A[2][0] = 0.0;  A[2][1] = -1.0;  A[2][2] = 2.0;  A[2][3] = -1.0;  A[2][4] = 0.0;
    A[3][0] = 0.0;  A[3][1] = 0.0;  A[3][2] = -1.0;  A[3][3] = 2.0;   A[3][4] = -1.0;
    A[4][0] = 0.0;  A[4][1] = 0.0;  A[4][2] = 0.0;   A[4][3] = -1.0;  A[4][4] = 2.0;

    /* Vecteur initial x0 quelconque (non nul) */
    x0[0] = 1.0;
    x0[1] = 1.0;
    x0[2] = 1.0;

    printf("\n");
    printf("  METHODE DES PUISSANCES ITEREES\n");
    printf("\n");
    printf("Matrice A :\n");
    afficherMatrice(A, n);
    afficherVecteur(x0, n, "Vecteur initial x0");
    printf("Epsilon    = %.2e\n", epsilon);
    printf("Max iter.  = %d\n\n", maxIter);

    /* ---- Resolution : appel de l'algorithme ---- */
    double valPropre = puissanceIteree(A, x0, vectPropre,
                                        n, epsilon, maxIter, &nbIter);

    /* ---- Affichage des resultats ---- */
    displayResult(valPropre, vectPropre, n, nbIter, epsilon);

    /* Liberation de la memoire */
    libererMatrice(A, n);
    free(x0);
    free(vectPropre);

    return 0;
}

/* ---- Norme euclidienne d'un vecteur ---- */
double norme(double *x, int n) {
    double s = 0.0;
    for (int i = 0; i < n; i++)
        s += x[i] * x[i];
    return sqrt(s);
}

/* ---- Normalisation : y = x / ||x|| ---- */
void normaliser(double *x, double *y, int n) {
    double norm = norme(x, n);
    if (norm < 1e-15) {
        fprintf(stderr, "Erreur : vecteur nul, normalisation impossible.\n");
        exit(EXIT_FAILURE);
    }
    for (int i = 0; i < n; i++)
        y[i] = x[i] / norm;
}

/* ---- Produit matrice-vecteur : x_new = A * y ---- */
void produitMatVec(double **A, double *y, double *x_new, int n) {
    for (int i = 0; i < n; i++) {
        x_new[i] = 0.0;
        for (int j = 0; j < n; j++)
            x_new[i] += A[i][j] * y[j];
    }
}

/* ---- Produit scalaire : <y, x> ---- */
double produitScalaire(double *y, double *x, int n) {
    double s = 0.0;
    for (int i = 0; i < n; i++)
        s += y[i] * x[i];
    return s;
}

/* ---- Algorithme des puissances iterees ---- */
double puissanceIteree(double **A, double *x0, double *vectPropre,
                       int n, double epsilon, int maxIter, int *nbIter) {

    double *x  = (double *)malloc(n * sizeof(double));
    double *y  = (double *)malloc(n * sizeof(double));
    double *xNew = (double *)malloc(n * sizeof(double));

    double lambda_old = 0.0;
    double lambda_new = 0.0;

    /* Copie de x0 dans x */
    for (int i = 0; i < n; i++)
        x[i] = x0[i];

    *nbIter = 0;

    for (int k = 0; k < maxIter; k++) {

        /* Etape 1 : y_k = x_k / ||x_k|| */
        normaliser(x, y, n);

        /* Etape 2 : x_{k+1} = A * y_k */
        produitMatVec(A, y, xNew, n);

        /* Etape 3 : lambda = <y_k | x_{k+1}> = quotient de Rayleigh */
        lambda_new = produitScalaire(y, xNew, n);

        (*nbIter)++;

        /* Etape 4 : Test d'arret */
        if (fabs(lambda_new - lambda_old) < epsilon) {
            break;
        }

        /* Mise a jour pour l'iteration suivante */
        lambda_old = lambda_new;
        for (int i = 0; i < n; i++)
            x[i] = xNew[i];
    }

    /* Le vecteur propre approxime est y_k final */
    normaliser(xNew, vectPropre, n);

    free(x);
    free(y);
    free(xNew);

    return lambda_new;
}

/* ---- Affichage des resultats ---- */
void displayResult(double valPropre, double *vectPropre,
                   int n, int nbIter, double epsilon) {
    printf("\n");
    printf("  RESULTATS\n");
    printf("\n");
    printf("Nombre d'iterations     : %d\n", nbIter);
    printf("Critere d'arret epsilon : %.2e\n", epsilon);
    printf("Valeur propre dominante : lambda_1 = %.8f\n\n", valPropre);
    afficherVecteur(vectPropre, n, "Vecteur propre associe y");
    printf("\n");
}

/* ---- Affichage d'une matrice ---- */
void afficherMatrice(double **A, int n) {
    for (int i = 0; i < n; i++) {
        printf("  [ ");
        for (int j = 0; j < n; j++)
            printf("%8.4f ", A[i][j]);
        printf("]\n");
    }
    printf("\n");
}

/* ---- Affichage d'un vecteur ---- */
void afficherVecteur(double *v, int n, const char *nom) {
    printf("%s :\n   ", nom);
    for (int i = 0; i < n; i++)
        printf("%10.6f ", v[i]);
    printf("\n\n");
}

/* ---- Allocation d'une matrice n x n ---- */
double **allouerMatrice(int n) {
    double **A = (double **)malloc(n * sizeof(double *));
    for (int i = 0; i < n; i++)
        A[i] = (double *)malloc(n * sizeof(double));
    return A;
}

/* ---- Liberation d'une matrice n x n ---- */
void libererMatrice(double **A, int n) {
    for (int i = 0; i < n; i++)
        free(A[i]);
    free(A);
}
