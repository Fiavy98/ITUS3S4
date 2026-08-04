// \n {}
#include <stdio.h> 
#include <math.h> 
float f(float x);
void Result(float a,float b,float eps,float seq);
float	secante(float a,float b,float eps);

int main() {
	printf("Calcul de £ tel que f(£)= 0 Par la Meth. de la fausse position\n");
///Donnes
	float a=0.1,b=1,	//Borne de l'interv. de separation
	seq=7654.788,	//La solut. a calcluler
	eps=1e-6;		//La precission a atteindre
///traitements
	seq = secante(a,b,eps);
///Sortie du resumtat
	Result(a,b,eps,seq);
	
	return 0;
}
float secante(float a,float b,float eps){
	float x =(f(b)-f(a))/(b-a);
	while(fabs(f(x))>eps){
		if(f(a)*f(x)<=0){
			b=x;
		}else {
			a=x;
		}
		
		x=(f(b)-f(a))/(b-a);
	}
	
	return x;
}

void Result(float a,float b,float eps,float seq){
	printf("La solution dans  [%g; %g]  est seq=%f\n",a,b,seq);
	printf("\tavec eps=%f\n",eps);
}

float f(float x){
	return x-2-log(x);
}
