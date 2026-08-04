#include <stdio.h> 
#include <math.h> 

float f(float x,int N);
void affResult(float a,float b,int n,float aire);
float Simps(float a,float b,int n);
float simps_eps(float a, float b, float eps, int *n_final);

int main(){
   printf("Calcule de l'integralle par la Methode de Simpson\n");
   
///Données
	float a=0, b=1,	//les borne d'integration
	aire=123.3, //aire par defaut;
	aire_eps=1e-7;
	int n=30; 		//le nombre de sous intrv.
	float eps=1e-7;
	int n_eps;
	
///Traitements 
	aire=Simps(a,b,n);
	aire_eps=simps_eps(a,b,eps,&n_eps);
	
///Sortie de resultat
	printf("Calcule simple\n");
	affResult(a,b,n,aire);
	printf("Calcule avec epsilonne\n");
	affResult(a, b, n_eps, aire_eps);

    return 0;
}

float simps_eps(float a, float b, float eps, int *n_final){
    int n = 2; // nombre initial de sous-intervalles
    float aire1 = Simps(a, b, n);
    n *= 2;
    float aire2 = Simps(a, b, n);

    while(fabs(aire2 - aire1) > eps){
        aire1 = aire2;
        n *= 2;
        aire2 = Simps(a, b, n);
    }

    *n_final = n;  // retourne le nombre final de sous-intervalles
    return aire2;
}


float Simps(float a,float b,int n){
	int N=18;
    float h=(b-a)/n;   
///Trouver Sommef(xi)
	float  s1=0.0,
	x=a;
	for(int i=1; i<n; i++){
	    x+=h;
	    s1+=f(x,N);
	}

///Trouver Sommef(xi+h/2)
	float s2=0.0,
	xx=a+h/2;
	for(int i=0; i<n; i++){
		s2+=f(xx,N);
	    xx+=h;
	}
	
	float aire=(h/6)*(f(a,N)+2*s1+f(b,N)+4*s2);
	return aire;
	
}

void affResult(float a,float b,int n,float aire){
     printf("\tl'intervale  de f dans [%g; %g] est aire = %f\n",a,b,aire);
     printf("\tEn découpant de sous intervalle n=%d\n",n);
}

float f(float x,int N){
   return N/(0.5+x*x);
}

