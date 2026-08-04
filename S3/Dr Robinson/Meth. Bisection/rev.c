#include <stdio.h>
#include <stdlib.h>
#include <math.h>

float f(float x);
void affResult(float a,float b, float ksi, float eps);
float dicho(float a,float b,float eps);
int main(){
	printf("calcul de £ tel que f(£)=0 par la methode de bissection\n");
///Donnes
	float a=0.1, b=1;	//borne de l'interv
	float ksi=123;	//solution a calculer
	float eps=1e-6;	//precission a atteindre
///Traitements
	ksi=dicho(a,b,eps);
///Sortie du resultat
	affResult(a,b,ksi,eps);
	return 0;

}
float dicho(float a,float b,float eps){
	float ksi=321.00;
	float x;
	while((b-a) > eps){
		x=(a+b)/2;
		
		if(f(a)*f(x)<=0){
			b=x;
		}else{
			a=x;
		}
	}
	return ksi=(a+b)/2;
}


void affResult(float a,float b, float ksi, float eps){
	printf("\tLa solution dans [%g,%g] est ksi=%f\n",a,b,ksi);
	printf("\tAvec precission £=%f",eps);
}

float f(float x){
	return x-2-log(x);
}
