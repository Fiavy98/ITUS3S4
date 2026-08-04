#include <stdio.h>
#include <math.h>
//{}

float f(float x);
void affResult(float a,float b,float aire,int n);
float trapeze(float a,float b,int n);
int main(){
  printf("Calcul de l'integralle par le meth. trapeze\n");
  
  /// Donnes
  float a=0,b=1, // le borne d'integration
  aire=675.874;  // l'integration a calculer 
  int n=30; //nb de sous intervalle
  
  //Traitements
   aire=trapeze(a,b,n);
  
  //Sortie du resultat
  affResult(a,b,aire,n);
  
  return 0;
}

float trapeze(float a,float b,int n){
	//dones 
    float h=(b-a)/n,
    x=a,s=0, 
    aire=77.3;
    
    for(int i=1;i<n; i++){
	  x+=h;
	  s+=f(x);
	}    
  
    aire = h/2.0*(f(a)+2*s+f(b));
    return aire;
}

void affResult(float a,float b,float aire,int n){
    printf("\nL'integralle f dans [%g; %g] est de aire aire = %f\n",a,b,aire);
    printf("\n decoupant de sous intervale n=%d\n",n);
}

float f(float x){
    return exp(sin(5*x));
}
