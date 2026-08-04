#include <stdio.h>
#include <stdlib.h>

int ReadData(char *filename, double x[], double y[]);
void AffResult(int N, double x[], double y[], double a, double b, double D);
void DescenteGradient(int N, double x[], double y[],double *a, double *b,double pas, int nbIter);
double erreurTotale(int N, double x[], double y[], double a, double b);
int main(){
	printf("Calcul d'Ajustement lineaire methode de descente du gradient\n");

///Donnees
	double x[20], y[20];	 // coordonnées des points
	int N;	// nombre de points
	double a=0.0,b=0.0;	// pente et ordonnée à l’origine
	double D=0.0;	// erreur totale
	double pas = 0.001;	// pas d’apprentissage
	int nbIter = 10000;	// nombre d’itérations
	
///Traitement
	N=ReadData("data.txt", x, y);
	DescenteGradient(N, x, y, &a, &b, pas, nbIter);
	D = erreurTotale(N, x, y, a, b);
	
	
///Sortie du resultat
	AffResult(N, x, y, a, b, D);

	return 0;
}

double erreurTotale(int N, double x[], double y[], double a, double b){
    double D = 0.0, y_droite = 0.0, erreur = 0.0;

    for(int i = 0; i < N; i++){
        y_droite = a * x[i] + b;
        erreur = y[i] - y_droite;
        D += erreur * erreur;
    }

    return D;
}

void DescenteGradient(int N, double x[], double y[],double *a, double *b,double pas, int nbIter){
    double dD_da, dD_db;
    double y_estime, erreur;

    for(int iter = 0; iter < nbIter; iter++) {

        dD_da = 0.0;
        dD_db = 0.0;

        for(int i = 0; i < N; i++) {
            y_estime = (*a) * x[i] + (*b);
            erreur   = y[i] - y_estime;

            dD_da += -2 * x[i] * erreur;
            dD_db += -2 * erreur;
        }

        *a = *a - pas * dD_da;
        *b = *b - pas * dD_db;
    }
}


void AffResult(int N, double x[], double y[], double a, double b, double D){
    printf("\nPoints :\n");
    for(int i = 0; i < N; i++){
        printf("x%d = %.3f , y%d = %.3f\n", i, x[i], i, y[i]);
    }

    printf("\nNombre de points N = %d\n", N);
    printf("Droite d'ajustement : y = %.5f x + %.5f\n", a, b);
    printf("Erreur totale D = %.5f\n", D);
}

int ReadData(char *filename, double x[], double y[]) {
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
