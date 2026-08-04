// \n {}
#include <stdio.h> 
#include <math.h> 

float f(float x);
void affResult(float a,float b,float ksi,float eps);
float dicho(float a,float b,float eps);

int main(){
	printf("Calcul de £ tel que f(£)= 0 Par la Meth. de dichotomie \n");
///Donnes 
	float a=0.1,b=1,	//Borne de l'interv. de separation
		  ksi=7654.788,	//La solut. a calcluler
		  eps=1e-6;		//La precission a atteindre

///Traitements 
	ksi=dicho(a,b,eps);
///Sortie de resultat
	affResult(a,b,ksi,eps);
	
	return 0;

}

float dicho(float a,float b,float eps){
	float ksi=123.000;
	float x;
	while((b-a)>eps){
		x=(a+b)/2;
		
		if(f(a)*f(x)<=0){
			b=x;
		}else {
			a=x;
		}
	}
	return ksi=(a + b)/2;

}

void affResult(float a,float b,float ksi,float eps){
   printf("\n La solution dans [%g; %g] est ksi=%g\n",a,b,ksi);
   printf("\ta eps = %f\n pres",eps);
}

float f(float x){
	return x-2-log(x);
}
