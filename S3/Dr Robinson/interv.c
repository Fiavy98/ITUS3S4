// \n {}  <
#include <stdio.h> 
#include <locale.h> 
float puissance(float nb,int puiss);
void affResult(float a,float b,int n,float aire);

int main(){
	printf("Calcule de l'intégralle par la Meth. de simpson \n");
	
///Donnees
	float a=1,	b=3;	//borne d'intégration
    
///Traitements

///Sortie du resultat
	//printf("%f puissance %d=%f",2.0,4,puissance(2.0,4)); test du puissance

	return 0;
}
void affResult(float a,float b,int n,float aire){
	printf("l'intervale de f dand [%g; %g] est de aire=%f\n",a,b,aire);
	printf("En découpant de sous intérvalle n=%d\n",n);
}

float puissance(float nb,int puiss){
	float val=1;
	for(int i=0; i<puiss; i++){
		val*=nb;
	}
	
	return val;
}

