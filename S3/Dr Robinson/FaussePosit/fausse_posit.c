// \n {}
#include <stdio.h> 
#include <math.h> 
float f(float x);
void Result(float a,float b,float eps,float ksi);
float fausse_posit(float a,float b,float eps);

int main() {
	printf("Calcul de £ tel que f(£)= 0 Par la Meth. de la fausse position\n");
///Donnes
	float a=0.1,b=1,	//Borne de l'interv. de separation
	ksi=7654.788,	//La solut. a calcluler
	eps=1e-6;		//La precission a atteindre
///traitements
	ksi = fausse_posit(a,b,eps);
///Sortie du resumtat
	Result(a,b,eps,ksi);
	
	return 0;
}
float fausse_posit(float a,float b,float eps){
	float ksi=123.00,
	x =a - f(a) * (b - a) / (f(b) - f(a));
	while(fabs(f(x))>eps){
		if(f(a)*f(x)<=0){
			b=x;
		}else {
			a=x;
		}
		
		ksi=x = a - f(a) * (b - a) / (f(b) - f(a));
	}
	
	return ksi;
}

void Result(float a,float b,float eps,float ksi){
	printf("La solution dans  [%g; %g]  est ksi=%f\n",a,b,ksi);
	printf("\tavec eps=%f\n",eps);
}

float f(float x){
	return x-2-log(x);
}
