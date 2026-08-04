#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void getData(int *dim, float **x, float **y);
void splineCoefs(float *x, float *y, int n,float **s2, float **a, float **b);
void solveS2(float *Y, float *s2, int n);
void tridiag(float *a, float *b, float *c, float *r, float *u, int n);
void exportSpline(float *x, float *y, int n,float *s2, float *a, float *b);

float *newVect(int n);
void problem(char *msg);

int main(void){
    int n;
    float *x=NULL, *y=NULL;
    float *s2=NULL, *a=NULL, *b=NULL;

///Get data
    getData(&n, &x, &y);

///Traitement
    splineCoefs(x, y, n, &s2, &a, &b);
    
 ///Sortie de resultat 

    exportSpline(x, y, n, s2, a, b);

    printf("Fichier spline.dat genere (pour gnuplot)\n");

    return 0;
}


void getData(int *dim, float **x, float **y){
    int i;
    FILE *f = fopen("spline.txt","r");
    if(!f) problem("Impossible d'ouvrir pt.dat");

    fscanf(f,"%d",dim);
    *x = newVect(*dim);
    *y = newVect(*dim);

    for(i=0;i<*dim;i++)
        fscanf(f,"%f %f",&(*x)[i],&(*y)[i]);

    fclose(f);
}

void splineCoefs(float *x, float *y, int n,
                 float **vs2, float **va, float **vb){

    int i;
    float dx = x[1]-x[0];

    float *s2 = newVect(n);
    float *a  = newVect(n-1);
    float *b  = newVect(n-1);
    float *Y  = newVect(n-2);

    /* Conditions naturelles */
    s2[0] = 0.0;
    s2[n-1] = 0.0;

    /* Second membre */
    for(i=1;i<n-1;i++){
        Y[i-1] = 6*(y[i+1] - 2*y[i] + y[i-1])/(dx*dx);
    }

    /* Résolution système tridiagonal */
    solveS2(Y, &s2[1], n-2);

    /* Coefficients a et b */
    for(i=0;i<n-1;i++){
        a[i] = (y[i+1]-y[i])/dx - dx*(2*s2[i]+s2[i+1])/6;
        b[i] = y[i];
    }

    *vs2 = s2;
    *va  = a;
    *vb  = b;

    free(Y);
}

/* ===== Résolution tridiagonale ===== */
void solveS2(float *Y, float *s2, int n){
    int i;
    float *A=newVect(n), *B=newVect(n), *C=newVect(n);

    for(i=0;i<n;i++){
        A[i]=1.0;
        B[i]=4.0;
        C[i]=1.0;
    }

    tridiag(A,B,C,Y,s2,n);

    free(A); free(B); free(C);
}
/* Tridiagonal matrix solver.  Nonzero diagonals of the matrix are
   represented by arrays a, b, and c (see Numerical Recipes).
   All arrays start at 0
 [b_0 c_0                  ] [ u_0 ]   [ r_0 ]
 [a_1 b_1 c_1              ] [ u_1 ]   [ r_1 ]
 [          ...            ] [ ... ] = [ ... ]
 [        a_n-2 b_n-2 c_n-2] [u_n_2]   [r_n-2]
 [              a_n-1 b_n-1] [u_n-1]   [r_n-1]
 */
 
void tridiag(float *a, float *b, float *c,
             float *r, float *u, int n){
    int i;
    float bet;
    float *gam=newVect(n);

    if(b[0]==0) problem("Division par zero");

    u[0]=r[0]/(bet=b[0]);

    for(i=1;i<n;i++){
        gam[i]=c[i-1]/bet;
        bet=b[i]-a[i]*gam[i];
        if(bet==0) problem("Division par zero");
        u[i]=(r[i]-a[i]*u[i-1])/bet;
    }

    for(i=n-2;i>=0;i--)
        u[i]-=gam[i+1]*u[i+1];

    free(gam);
}

void exportSpline(float *x, float *y, int n,
                  float *s2, float *a, float *b){

    FILE *f = fopen("spline.dat","w");
    int i,j;
    float xx,dx;

    for(i=0;i<n-1;i++){
        dx=(x[i+1]-x[i])/50.0;
        for(j=0;j<=50;j++){
            xx=x[i]+j*dx;
            fprintf(f,"%f %f\n",
                xx,
                b[i]
                + a[i]*(xx-x[i])
                + s2[i]*pow(xx-x[i],2)/2
                + (s2[i+1]-s2[i])*pow(xx-x[i],3)
                  /(6*(x[i+1]-x[i]))
            );
        }
    }
    fclose(f);
}

float *newVect(int n){
    float *v=(float*)malloc(n*sizeof(float));
    if(!v) problem("Allocation impossible");
    return v;
}

void problem(char *msg){
    printf("Erreur : %s\n",msg);
    exit(1);
}
