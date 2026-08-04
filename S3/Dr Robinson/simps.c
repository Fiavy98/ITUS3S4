//{}  <>
#include <stdio.h>
#include <math.h>
float f(float x);
void affResult(float a,float b,float aire, int n);
int main(){
	printf("calcul d'integrale de f entre a et b par la meth. des Simpson\n");
//Donnees
   float a=0, b=1, // les borne d'integration
   aire=123.33; // aire par defaut
   int n=5; //nb de sous intervalle
   
//Traitements 
   
//Sortie de resultat
   affResult();a
   return 0;
    
}

void affResult(float a,float b,float aire, int n){
   printf("\nL'intégralle de f dans [%g; %g] est de aire=%f\n",a,b,aire);
   printf("\t En decoupant de sous intérvale n=%d\n",n);
   
}

float f(float x){
  return exp(sin(5*x));
}



