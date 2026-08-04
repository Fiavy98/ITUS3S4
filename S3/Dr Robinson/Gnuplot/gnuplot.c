#include <stdio.h>
#include <math.h>

int main() {
    printf("Opening a pipe to gnuplot\n");
    FILE *gp = popen("gnuplot -persist", "w");

    float xmax = 2, xmin = 3;
    int dim = 100;
    float x = 0, h = 0;

    if (gp) {

        // Envoi des données
        h = (xmax - xmin) / (dim - 1);

        fprintf(gp, "$data << EOF\n");
        x = xmin - h;

        for (int i = 1; i < dim; i++) {
            x += h;
            fprintf(gp, "%f %f %f\n", x, cos(2*x), x - 2 - log(x));
        }

        fprintf(gp, "EOF\n");


        // ----------------------------------------------------
        //                Plots commands
        // ----------------------------------------------------

        fprintf(gp, "set term wxt size 800,600\n");
        fprintf(gp, "set title 'Comparaison de fonctions'\n");
        fprintf(gp, "set xlabel 'x'\n");
        fprintf(gp, "set ylabel 'Valeurs'\n");
        fprintf(gp, "set grid\n");
        fprintf(gp, "set key left top\n");
        fprintf(gp, "set style data linespoints\n");

        fprintf(gp,
            "plot "
            "$data using 1:2 title 'cos(2x)' with lines lw 2, "
            "$data using 1:3 title 'x - 2 - log(x)' with lines lw 2\n"
        );

        fflush(gp);
        pclose(gp);

    } else {
        printf("gnuplot not found...\n");
    }

    return 0;
}
