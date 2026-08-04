// \n {}
#include <stdio.h> 

int main(){
  printf("Opening a pipe to gnuplot\n");
  FILE *gp = popen("gnuplot -persist","w");
  
  if(gp) { //if gnuplot is found
		fprintf(gp,"set term wxt size 800,600\n");
		fprintf(gp,"set title 'Measurements vs sin(x)'\n");
		fprintf(gp,"set xlabel 'Time (s)'\n");
		fprintf(gp,"set ylabel 'Values'\n");
		fprintf(gp,"set style data linespoints\n");
		fprintf(gp,"plot 'huhu.txt',sin(x)\n");
		fflush(gp);
		pclose(gp);
		
  }else printf("gnuplot not found....\n");
  
  return 0;
}
