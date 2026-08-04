#include <stdio.h> 
#include <math.h> 

int getData(char *file,float x[],float y[]);
void affResult(int N,float x[],float y[],float X,float p);
float Lagrange(int N, float x[], float y[], float X);
int main(){
	printf("Methode le l'interpolation Polynomiale de Lagrange\n");
///Donnees
	int N;	//Dimension
	float x[20], y[20];	//cordonnes de points
	float X=3,	//Valeur a estimer 
	p=0.0;	//Valeur interpoler
	
///Traitement
	N=getData("pt.txt",x,y);
	p=Lagrange(N,x,y,X);
	
///Resultat
	affResult(N,x,y,X,p);
	

	return 0;
}

float Lagrange(int N, float x[], float y[], float X){
	float p=0;
	for(int i=0;  i < N;i++ ){
		float Li=1;
		for(int j=0;  j < N; j++){
			if(j!=i){
				Li*=((X-x[j])/(x[i]-x[j]));
			}
		}
		
		p+=y[i]*Li;
		
	}
	return p;
}

void affResult(int N,float x[],float y[],float X,float p){
	printf("\n");
	for(int i=0;  i < N;i++ ){
		printf("x%d=%f , y%d=%f\n",i,x[i],i,y[i]);
	}
	
	printf("\nDimension N=%d\n",N);
	
	printf("Valeur a estimer X=%f\n",X);
	
	printf("Valeur interpolee p(%f)=%f\n",X,p);
	
	
}

int getData(char *file,float x[],float y[]){
	FILE *f = fopen(file,"r");
	if(f==NULL){
		printf("Erreur : impossible d'ouvrir le fichier %s\n", file);	
		return -1;
	}
	
	int N;
	if(fscanf(f,"%d",&N)!=1){
		printf("Erreur lecture N\n");
		fclose(f);
		return -1;
	}
	
	for(int i=0;  i < N;i++ ){
		if(fscanf(f,"%f,%f",&x[i],&y[i])!=2){
			printf("Erreur lecture point %d\n", i);
			fclose(f);
			return -1;
		}
	}
	fclose(f);
	return N;
}
