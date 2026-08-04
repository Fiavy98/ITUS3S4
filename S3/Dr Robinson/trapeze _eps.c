#include <stdio.h>
#include <math.h>
#include <locale.h>

float f(float x);
void dispResult(float a,float b,float aire, int n,float eps);
float trapeze(float a,float b,int n);
float trapezes(float a,float b,float eps);

int main(){
    printf("calcul d'integrale de f entre a et b par la meth. des trapezes\n");

///Les donnees 
    float a=0, b=1, //les bornes d'integration
    aire=675.874; //l'integrale a calculer
    int n=30; //Le nb de sous interv. de discretisation
    float eps=1e-6;
    
/// Traitements
    aire = trapeze(a,b,n);
    aire =trapezes(a,b,eps);
    
///Sortie de resultat
    dispResult(a,b,aire,n,eps);
    return 0;
}


float trapezes(float a, float b, float eps) {
    int n = 2; // nombre initial de sous-intervalles
    float aire1, aire2;

    aire1 = trapeze(a, b, n);  // premire approxim
    n *= 2;                    // on double le nb d’interv.
    aire2 = trapeze(a, b, n);  // deuxiem approx

    //continue tant que la difference est plus grande que epsilon
    while (fabs(aire2 - aire1) > eps) {
        aire1 = aire2;
        n *= 2;                // on raffine encore
        aire2 = trapeze(a, b, n);
    }

    printf("\nConvergence atteinte pour n = %d sous-intervalles.\n", n);
    return aire2;
}


float trapeze(float a,float b,int n){
	float aire=765.3, //L'integrale a calculer 
	h=(b-a)/n, //Pas de discretisation
	s=0, x=a;
	
	for(int i=1; i<n; i++){
	  x+=h;
	  s+=f(x);
	}
	
	aire=h/2.*(f(a) + 2*s + f(b));
	
	
	return aire;
}

void dispResult(float a,float b,float aire, int n,float eps){
   printf("\n l'intégrale de f dans [%g; %g] est aire = %f\n",a,b,aire);
   printf("\t en decoupent n=%d sous intervalles\n",n);
   printf("\t a epsilone n=%f\n",eps);
}

float f(float x){
	return exp(sin(5*x));
}


