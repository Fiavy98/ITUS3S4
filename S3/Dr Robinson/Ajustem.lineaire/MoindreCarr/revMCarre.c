#include <stdio.h>
#include <stdlib.h>

int getData(char *fille,float x[],float y[]);
void affResult(int N,float x[],float y[],float a,float b,float D);
void getCoeff(int N ,float x[],float y[],float *a,float *b);
float Distance(int N,float x[],float y[],float a, float b);
int main(){
	printf(" Ajustement lineaire a une droite par la methode du moindre carre\n");
	
///Donenes
	float x[20],y[20];	//Cordonnes de points
	int N;	//Nombre de points
	float a=0.0, b=0.0,	//Le coeffichient a chercher
	D=0.0; //Disatance total
	
///Traitement
	N=getData("data.txt",x,y);
	getCoeff(N ,x,y,&a,&b);
	D=Distance(N,x,y,a,b);
///Sortie du resultat 
	
	affResult(N,x,y,a,b,D);
	
	return 0;
}

float Distance(int N,float x[],float y[],float a, float b){
	 float D = 0.0, y_droite = 0.0, erreur = 0.0;

    for(int i = 0; i < N; i++){
        y_droite = a * x[i] + b;
        erreur = y[i] - y_droite;
        D += erreur * erreur;
    }

	
	return D;
}

void getCoeff(int N ,float x[],float y[],float *a,float *b){
	float sumx=0.0,	sumy=0.0,	sumxy=0.0, sumx2=0.0;
	
	for(int i=0;  i < N;i++ ){
		sumx+=x[i];
		sumy+=y[i];
		sumxy+=x[i]*y[i];
		sumx2+=x[i]*x[i];
	}
	
	*a = ((N * sumxy) - (sumx * sumy)) / ((N * sumx2) - (sumx * sumx));
	*b = (sumy - (*a * sumx)) / N;
	
	
}

void affResult(int N,float x[],float y[],float a,float b,float D){
	printf("\n");
		for(int i=0;  i < N;i++ ){
		printf("x%d=%f , y%d=%f\n",i,x[i],i,y[i]);
	}
	
	printf("\nDimenion N=%d\n",N);
	
	printf("La Droite y=%fx + %f\n",a,b);
	
	printf("La distance Total D=%f\n",D);
	
}

int getData(char *fille,float x[],float y[]){
	FILE *f = fopen(fille,"r");
	
	if(f==NULL){
		printf("Nono\n");	
		return -1;
	}
	
	int N;
	fscanf(f,"%d",&N);
	
	for(int i=0;  i < N;i++ ){
		fscanf(f,"%f,%f",&x[i],&y[i]);
	}
	
	return N;
	
	
}
