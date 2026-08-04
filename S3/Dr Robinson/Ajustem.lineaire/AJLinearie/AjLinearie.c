#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void affResult(float a,float b,int nbInteract);
void getDataf(char *fileName, float ***tA, float **tb, int *dim);
int main(){
	printf("Ajustement lineaire a une droite par methode descente du gradient\n");
///Donnees
	float **A = NULL, *b = NULL;
    int dim = 0, nbInteract=0;

///Traitement
    /* Lecture des données depuis le fichier */
    getDataf("DataMr.txt", &A, &b, &dim);

///Sortie du resultat
	affResult(&A,&b,nbInteract);
	
	
	return 0;
}
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

void affResult(float a,float b,int nbInteract){
	printf("\ta=%f\n",a);
	printf("\tb=%f\n",b);
	printf("\tnombre d'interaction=%d\n",nbInteract);
}
