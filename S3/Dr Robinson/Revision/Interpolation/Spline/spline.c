#include <stdlib.h>
#include <stdio.h>

/* Prototypes */
void getData(char *filename, int *dim, float **xd, float **yd);
void gplot(float *xd, float *yd, int dim, float *s2, float *a, float *b,
           float xmin, float xmax);
void splineCoefs(float *xd, float *yd, int dim,
                 float **vs2, float **va, float **vb);

float *newVect(int dim);
void problem(char *message);
void solveS2(float lambda, float rho, float *Y, float *s2, int dim);
void tridiag(float *a, float *b, float *c, float *r, float *u, int n);

int main(void)
{
    int dim = 0;
    float *xd = NULL, *yd = NULL;
    float *s2 = NULL, *a = NULL, *b = NULL;
    float X, P;

    /* Lecture des données depuis pt.txt */
    getData("pt.txt", &dim, &xd, &yd);

    printf("Methode de l'interpolation Polynomiale de Spline Cubique\n\n");
    for(int i=0; i<dim; i++)
        printf("x%d=%f , y%d=%f\n", i, xd[i], i, yd[i]);
    printf("\nDimension N=%d\n", dim);

    /* Traitement spline */
    splineCoefs(xd, yd, dim, &s2, &a, &b);

    /* Estimation : X = xd[3] dans ton exemple (X=3) */
    X = xd[3];

    /* Trouver l'intervalle contenant X */
    int idx = 0;
    for(int i=0; i<dim-1; i++){
        if(X >= xd[i] && X <= xd[i+1]){
            idx = i;
            break;
        }
    }

    /* Interpolation spline cubique */
    float dx = X - xd[idx];
    P = yd[idx] + a[idx]*dx + s2[idx]*dx*dx/2.0 + b[idx]*dx*dx*dx;

    printf("Valeur a estimer X=%f\n", X);
    printf("Valeur interpolee p(%f)=%f\n", X, P);

    /* Tracé fichier spline.dat pour gnuplot */
    gplot(xd, yd, dim, s2, a, b, xd[0], xd[dim-1]);

    /* Libération mémoire */
    free(xd);
    free(yd);
    free(s2);
    free(a);
    free(b);

    return 0;
}

/* ===================== LECTURE FICHIER ===================== */

void getData(char *filename, int *dim, float **xd, float **yd)
{
    FILE *f = fopen(filename,"r");
    if(!f) problem("Erreur ouverture fichier pt.txt");

    fscanf(f,"%d", dim);  // première ligne = nombre de pts

    *xd = newVect(*dim);
    *yd = newVect(*dim);

    for(int i=0; i<*dim; i++){
        if(fscanf(f,"%f,%f",&(*xd)[i], &(*yd)[i]) != 2)
            problem("Erreur lecture données");
    }

    fclose(f);
}

/* ===================== SPLINE ===================== */

void splineCoefs(float *xd, float *yd, int dim,
                 float **vs2, float **va, float **vb)
{
    int i;
    float dx = xd[1] - xd[0];  // on suppose équidistant
    float lambda=1.0, rho=1.0;
    float *Y = newVect(dim-2);
    float *s2 = newVect(dim);
    float *a = newVect(dim-1);
    float *b = newVect(dim-1);

    s2[0]=0.0; s2[dim-1]=0.0;

    for(i=1;i<dim-1;i++){
        Y[i-1] = 6.0*((yd[i+1]-yd[i])/dx - (yd[i]-yd[i-1])/dx);
    }

    solveS2(lambda,rho,Y,s2+1,dim-2);

    for(i=0;i<dim-1;i++){
        a[i] = (yd[i+1]-yd[i])/dx - dx*(2*s2[i]+s2[i+1])/6.0;
        b[i] = (s2[i+1]-s2[i])/(6.0*dx);
    }

    *vs2 = s2;
    *va  = a;
    *vb  = b;

    free(Y);
}

/* ===================== TRACE ===================== */

void gplot(float *xd, float *yd, int dim, float *s2, float *a, float *b,
           float xmin, float xmax)
{
    FILE *f = fopen("spline.dat","w");
    if(!f) problem("Erreur fichier spline.dat");

    float x,S;
    for(int i=0;i<dim-1;i++){
        for(x=xd[i]; x<=xd[i+1]; x+=0.01){
            float dx = x - xd[i];
            S = yd[i] + a[i]*dx + s2[i]*dx*dx/2.0 + b[i]*dx*dx*dx;
            fprintf(f,"%f %f\n", x, S);
        }
    }
    fclose(f);

    printf("\nTracer avec gnuplot :\n");
    printf("plot 'spline.dat' with lines\n");
}

/* ===================== SYSTEME ===================== */

void solveS2(float lambda, float rho, float *Y, float *s2, int dim)
{
    float *A = newVect(dim);
    float *B = newVect(dim);
    float *C = newVect(dim);

    for(int i=0;i<dim;i++){
        A[i]=rho; B[i]=2.0; C[i]=lambda;
    }

    tridiag(A,B,C,Y,s2,dim);

    free(A); free(B); free(C);
}

/* ===================== TRIDIAGONAL ===================== */

void tridiag(float *a,float *b,float *c,float *r,float *u,int n)
{
    float *gam = newVect(n);
    float bet;
    int j;

    if(b[0]==0.0) problem("Division par 0");
    u[0]=r[0]/(bet=b[0]);

    for(j=1;j<n;j++){
        gam[j] = c[j-1]/bet;
        bet = b[j]-a[j]*gam[j];
        if(bet==0.0) problem("Division par 0");
        u[j]=(r[j]-a[j]*u[j-1])/bet;
    }

    for(j=n-2;j>=0;j--) u[j]-=gam[j+1]*u[j+1];

    free(gam);
}

/* ===================== UTILITAIRES ===================== */

float *newVect(int dim)
{
    float *v = malloc(dim*sizeof(float));
    if(!v) problem("Allocation impossible");
    return v;
}

void problem(char *message)
{
    printf("%s\n", message);
    exit(2);
}
