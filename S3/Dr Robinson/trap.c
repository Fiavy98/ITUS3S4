#include <stdio.h>
#include <stdlib.h>

float power(int x, float num);
float function(float x);
float getHauteur(int a, int b, int n);
float * diviser(int a, int b, int n);
float calculAirTrapeze(float base1, float base2, float hauteur);
float * getAllTrapeze(int a, int b, int n);
float integral(int a, int b, int n);


int main(){
	
	//Donnees
	int a = 3;
	int b = 4;
	int n = 10;
	
	//Calcul de la primitive
	float primitive = integral(a,b,n);
	
	//Affichage des resultats
	printf("la plage est [%d,%d] decoupe en %d parts\n", a, b, n);
	printf("Resultat de la primitive: %.16f", primitive);	
	return 0;
}

//fonction puissance 
float puissance(int x, float num){
	float result = 1;
	for(int i = 0; i < x; i++){
		result *= num;
	}
	
	return result;
}

//la fonction
float function(float x){
	return 1/( 8 - puissance(3, x));
}


//fonction pour avoir la hauteur
float getHauteur(int a, int b, int n){
	float h = (float)((b - a)) / n;
	return h;
}


// fonction pour diviser les bornes
float * diviser(int a, int b, int n){
	float * result = malloc((n + 1) * sizeof(float));
	float h = getHauteur(a,b,n);
	for(int i = 0; i <= n; i++){
		result[i] = a + (h * i);
		//printf("result[%d]: %f\n", i, result[i]);
	}
	return result;
}

//calculer l'air d'un trapeze
float calculAirTrapeze(float base1, float base2, float hauteur){
		return (((base1 + base2) * hauteur)/ 2);
}

//get tous les trapezes par rapport a n
float * getAllTrapeze(int a, int b, int n){
	float * trapezes = malloc((n + 1) * sizeof(float));
	float h = getHauteur(a, b ,n);
	float * division = diviser(a, b, n);
	for(int i = 0; i < n; i++){
		float b = function(division[i]);
		float B = function(division[i + 1]);
		trapezes[i] = calculAirTrapeze(b, B, h);
	}
	return trapezes;
}

//fonction qui calcul l'integral
float integral(int a, int b, int n){
	float result = 0.0;
	float * trap = getAllTrapeze(a,b,n);
	for(int i = 0; i < n; i++){
		result += trap[i];
	}
	return result;
}

