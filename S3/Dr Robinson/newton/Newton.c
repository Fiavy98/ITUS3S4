// \n {}
#include <stdio.h> 
#include <math.h> 

float f(float x);
void affResult(float a,float b,float ksi,float eps);
float Newton(float a,float b,float eps);
float fprim(float x);

int main(){
	printf("Calcul de £ tel que f(£)= 0 Par la Meth. de Newton \n");
///Donnes 
	float a=0.1,b=1,	//Borne de l'interv. de separation
		  ksi=7654.788,	//La solut. a calcluler
		  eps=1e-6;		//La precission a atteindre

///Traitements 
	ksi=Newton(a,b,eps);
///Sortie de resultat)___
	affResult(a,b,ksi,eps);
	
	return 0;

}

float Newton(float a,float b,float eps){
	float xo=a;
	float x1;
	x1=xo-(f(xo)/fprim(xo));
		while((x1-xo)>eps){
			xo=x1;
			x1=xo-(f(xo)/fprim(xo));
		
	}
	
	return xo;
	
}

void affResult(float a,float b,float ksi,float eps){
   printf("\n La solution dans [%g; %g] est ksi=%g\n",a,b,ksi);
   printf("\ta eps = %f\n pres",eps);
}

float f(float x){
	return x-2-log(x);
}

float fprim(float x){
	return (x-1)/x;
}
