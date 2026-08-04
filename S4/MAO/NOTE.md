M:read_matrix("mat.txt",space);
mes:read_nested_list("mesures.txt",comma);
read_list("mesures.txt",comma);
read_list("mat.txt",space);
plot2d([discrete,mes],[gnuplot_preamble,"reset"])
for i:0 thru 10 do (x[i]:i-3, y[i]: x[i]^2 - 2.5);
for i:0 thru 10 do print("y[",i,"]=",y[i])
d:makelist([x[i],y[i]],i,0,10);
write_data(d,"data.txt",comma);
printfile("data.txt")

Graphique avec maxima
plot2d([sin(x),cos(x)],[x,-%pi,2*%pi],[y,-1.5,1.5],[xlabel,"time"],[ylabel,"valeurs"],[title,"Graphiques"]);
f(x):= if x<1 then x-3/2 else (2*x^2-1)/(x^2-3);
plot2d([f(x),x=sqrt(3),2],[x,-2,5],[y,-4.,7.],[color,blue,red,red],[legend,"y=f(x)","x=sqrt(3)","y=2"]);
**wxplot2d([x^3+y^3=0.5,x^3+y^3=-0.5],[x,-1.5,1.5],[y,-1.5,1.5]);**
