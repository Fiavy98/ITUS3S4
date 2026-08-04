#include <stdio.h>
#include <math.h>

float puissance(float phi,int puis);
void affResult(float phi,int puis,float result);
int main(){
	printf("Calcul du puissance d'un nombre phi\n");
//Donnee 
   float phi=(sqrt(5)-1)/2;
   int puis=50;
   float result=2.2;
//Traitements
    result = puissance(phi,puis);
//Sortie du Resultat	
 affResult(phi,puis,result);
}

void affResult(float phi,int puis,float result){
	printf("\tphi = %f\n ",phi);
	printf("\tN  = %d\n ",puis);
    printf("\tphi puissance N = %f\n ",result);
}

float puissance(float phi,int puis){
	float val=1;
	for(int i=0;i<puis; i++){
      val*=phi;
	}   
	
	return val; 
}


