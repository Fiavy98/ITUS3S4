// \n {}
#include <stdio.h> 
#include <math.h> 

float f(float x,int N);
void affResult(float a,float b,int n,float aire);
float trapeze(float a,float b,int n);
int main(){
    printf("Calcul de approximation par Met. trapeze\n");
///Données
   float a=0, b=1, //Borne d'intégretion
   aire=123.4;//aire par defaut;
   int n=30;
///Traitements
   aire=trapeze(a,b,n);
///Affichage du résultat
   affResult(a,b,n,aire);

}

float trapeze(float a,float b,int n){
	float x=a,s=0;
	int N=18;
	float h=(b-a)/n;
	for(int i=1; i<n; i++){
		x+=h;
		s+=f(x,N); 
	}
	
	float aire=h/2*(f(a,N) + 2*s + f(b,N));
	return aire;
	
}

void affResult(float a,float b,int n,float aire){
   printf("\n l'intégrale de f dans [%g; %g] est aire = %f\n",a,b,aire);
   printf("\t en decoupent n=%d sous intervalles\n",n);
}

float f(float x,int N){
   return N/(0.5+x*x);
}

