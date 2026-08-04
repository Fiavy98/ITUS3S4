#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* Prototypes */
void getDataf(char *fileName, float ***tA, float **tb, int *dim);
void displayMat(float **A, int dim);
void displayVect(char *message, float *x, int dim);

float *gaussp(float **A, float *b, int dim);
void triangp(float **A, float *b, int *L, int dim);
void solveTriang(float **A, float *b, float *x, int *L, int dim);

/* ===================== MAIN ===================== */
int main(){
    printf("Resolution des systemes lineaires par la methode de Gauss\n");

    float **A = NULL, *b = NULL, *x = NULL;
    int dim = 0;

    /* Lecture des données depuis le fichier */
    getDataf("DataMr.txt", &A, &b, &dim);

    printf("\nMatrice du probleme :\n");
    displayMat(A, dim);
    displayVect("Second membre :", b, dim);

    /* Résolution */
    x = gaussp(A, b, dim);

    /* Affichage solution */
    displayVect("Solution du systeme :", x, dim);

    /* Libération mémoire */
    for(int i = 0; i < dim; i++)
        free(A[i]);
    free(A);
    free(b);
    free(x);

    return 0;
}

/* ===================== LECTURE DES DONNEES ===================== */
void getDataf(char *fileName, float ***tA, float **tb, int *dim){
    FILE *pf = fopen(fileName, "r");
    if(pf == NULL){
        printf("Erreur ouverture fichier\n");
        exit(1);
    }

    fscanf(pf, "%d", dim);

    *tA = (float**)malloc((*dim) * sizeof(float*));
    for(int i = 0; i < *dim; i++){
        (*tA)[i] = (float*)malloc((*dim) * sizeof(float));
    }

    *tb = (float*)malloc((*dim) * sizeof(float));

    /* Lecture de la matrice A */
    for(int i = 0; i < *dim; i++){
        for(int j = 0; j < *dim; j++){
            fscanf(pf, "%f", &(*tA)[i][j]);
        }
    }

    /* Lecture du second membre b */
    for(int i = 0; i < *dim; i++){
        fscanf(pf, "%f", &(*tb)[i]);
    }

    fclose(pf);
}

/* ===================== AFFICHAGES ===================== */
void displayMat(float **A, int dim){
    for(int i = 0; i < dim; i++){
        for(int j = 0; j < dim; j++){
            printf("%8.2f ", A[i][j]);
        }
        printf("\n");
    }
}

void displayVect(char *message, float *x, int dim){
    printf("%s\n", message);
    for(int i = 0; i < dim; i++){
        printf("x%d = %8.4f\n", i+1, x[i]);
    }
}

/* ===================== GAUSS AVEC PIVOTAGE ===================== */
float *gaussp(float **A, float *b, int dim){
    float *x = (float*)malloc(dim * sizeof(float));
    int *L = (int*)malloc(dim * sizeof(int));

    /* Initialisation du vecteur de permutation */
    for(int i = 0; i < dim; i++)
        L[i] = i;

    /* Triangularisation */
    triangp(A, b, L, dim);

    /* Substitution arrière */
    solveTriang(A, b, x, L, dim);

    free(L);
    return x;
}

/* ===================== TRIANGULARISATION ===================== */
void triangp(float **A, float *b, int *L, int dim){
    int i, j, k, imax;
    float max, tmp;

    for(k = 0; k < dim - 1; k++){
        /* Recherche du pivot maximal */
        imax = k;
        max = fabs(A[L[k]][k]);

        for(i = k + 1; i < dim; i++){
            if(fabs(A[L[i]][k]) > max){
                max = fabs(A[L[i]][k]);
                imax = i;
            }
        }

        /* Permutation des lignes via L */
        if(imax != k){
            tmp = L[k];
            L[k] = L[imax];
            L[imax] = tmp;
        }

        /* Elimination */
        for(i = k + 1; i < dim; i++){
            float facteur = A[L[i]][k] / A[L[k]][k];
            A[L[i]][k] = 0.0;

            for(j = k + 1; j < dim; j++){
                A[L[i]][j] -= facteur * A[L[k]][j];
            }
            b[L[i]] -= facteur * b[L[k]];
        }
    }
}

/* ===================== SUBSTITUTION ARRIERE ===================== */
void solveTriang(float **A, float *b, float *x, int *L, int dim){
    for(int i = dim - 1; i >= 0; i--){
        float somme = 0.0;
        for(int j = i + 1; j < dim; j++){
            somme += A[L[i]][j] * x[j];
        }
        x[i] = (b[L[i]] - somme) / A[L[i]][i];
    }
}

