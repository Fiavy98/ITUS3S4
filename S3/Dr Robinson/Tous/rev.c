#include <stdio.h>
#include <math.h>

float f(float x);
float fprim(float x);
void affResult(float a,float b,float ksi,float eps,int count);

float dicho(float a,float b,float eps,int *count);
float fausseP(float a,float b,float eps,int *count);
float newton(float a,float b,float eps,int *count);

int main(){
	printf("Recherre d'une rachine\n");
	
///Donne
	float a=0.1, b=1; //borne de l'interv.
	float ksi=123.00; //solution a calculer
	float eps=1e-6;
	int count;
	int choix=-1;
	
	while(choix !=0){
		printf("\n\n===========MENU==========\n");
		printf("1-Mathode de dichrotomir\n");
		printf("2-Methode de Fausse possition\n");
		printf("3-Methode de Newton\n");
		printf("0-Pour quiter le programme\n");
		
		printf("Votre choix :");
		scanf("%d",&choix);	
		
		printf("\n");
		
		if (choix==1){
			printf("1-Mathode de dichrotomir\n");
			count=0;
			ksi=dicho(a,b,eps,&count);
			affResult(a,b,ksi,eps,count);
		}
		else if (choix==2){
			printf("2-Methode de Fausse possition\n");
			count=0;
			ksi=fausseP(a,b,eps,&count);
			affResult(a,b,ksi,eps,count);
		}
		else if (choix==3) {
			printf("3-Methode de Newton\n");
			count=0;
			ksi=newton(a,b,eps,&count);
			affResult(a,b,ksi,eps,count);
		}
		 else if (choix == 0) {
            printf("\nFin du programme.\n");
        }
		else {
            printf("\nChoix invalide, veuillez recommencer.\n");
        }
		
	}
	
	return 0;
}

float newton(float a,float b,float eps,int *count){
	float xo=a;
	float x1=xo-f(xo)/fprim(xo);
	
	while((x1-xo)>eps){
		xo=x1;
		
		x1=xo - f(xo)/fprim(xo);
		
		(*count)++;
	}
	
	return xo;
}

float fausseP(float a, float b, float eps, int *count) {
    float x = a;
    while (fabs(f(x)) > eps) {
        x = a - f(a) * (b - a) / (f(b) - f(a));
        (*count)++;

        if (f(a) * f(x) <= 0)
            b = x;
        else
            a = x;
    }
    return x;
}
  

float dicho(float a,float b,float eps,int *count){
	float x;
	while((b-a)>eps){
		x=(a+b)/2;
		(*count)++;
		if(f(a)*f(x)<=0){
			b=x;
		}else{
			a=x;
		}
	}
	
	return (a+b)/2;
}

void affResult(float a,float b,float ksi,float eps,int count){
	printf("\t la solution dans [%g,%g] est ksi=%f\n",a,b,ksi);
	printf("\t avec pressicion eps=%f\n",eps);
	printf("\t nombre d'interaction = %d",count); 
}

float fprim(float x) {
    return (x - 1) / x;
}

float f(float x) {
    return x - 2 - log(x);
}
