#include <stdio.h>
#include <math.h>

/* Prototypes */
float f(float x);
float fprim(float x);
void affResult(float a, float b, float ksi, float eps, int count);
float dicho(float a, float b, float eps, int *count);
float fausse_posit(float a, float b, float eps, int *count);
float Newton(float a, float b, float eps, int *count);

int main() {
    int choix = -1;
    int count;
    float a = 0.1, b = 1.0;
    float eps = 1e-6;
    float ksi;

    while (choix != 0) {
        printf("\n================ MENU =================\n");
        printf("1 - Methode de Dichotomie\n");
        printf("2 - Methode de la Fausse Position\n");
        printf("3 - Methode de Newton\n");
        printf("0 - Quitter le programme\n");
        printf("Votre choix : ");
        scanf("%d", &choix);

        if (choix == 1) {
            count = 0;
            ksi = dicho(a, b, eps, &count);
            printf("\n--- Methode de Dichotomie ---\n");
            affResult(a, b, ksi, eps, count);
        }
        else if (choix == 2) {
            count = 0;
            ksi = fausse_posit(a, b, eps, &count);
            printf("\n--- Methode de la Fausse Position ---\n");
            affResult(a, b, ksi, eps, count);
        }
        else if (choix == 3) {
            count = 0;
            ksi = Newton(a, b, eps, &count);
            printf("\n--- Methode de Newton ---\n");
            affResult(a, b, ksi, eps, count);
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

/* ================= Methodes ================= */

float dicho(float a, float b, float eps, int *count) {
    float x;
    while ((b - a) > eps) {
        x = (a + b) / 2;
        (*count)++;

        if (f(a) * f(x) <= 0)
            b = x;
        else
            a = x;
    }
    return (a + b) / 2;
}

float fausse_posit(float a, float b, float eps, int *count) {
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

float Newton(float a, float b, float eps, int *count) {
    float x0 = a;
    float x1 = x0 - f(x0) / fprim(x0);

    while (fabs(x1 - x0) > eps) {
        x0 = x1;
        x1 = x0 - f(x0) / fprim(x0);
        (*count)++;
    }
    return x1;
}

/* ================= Utilitaires ================= */

void affResult(float a, float b, float ksi, float eps, int count) {
    printf("La solution dans [%g ; %g] est ksi = %g\n", a, b, ksi);
    printf("Precision eps = %g\n", eps);
    printf("Nombre d'iterations = %d\n", count);
}

float f(float x) {
    return x - 2 - log(x);
}

float fprim(float x) {
    return (x - 1) / x;
}
