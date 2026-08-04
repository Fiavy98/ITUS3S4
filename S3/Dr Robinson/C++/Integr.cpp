class Integrator {
   public : 
        Integrator(float va, float vb, int vn); //Constructeur
        Integrator(float va,float vb,float veps);
        void trapn();
        void trapezes();
        void displayResult();
        float f(float x){return exp(sin(5*x));}
        
    private:
    float a,b;
    float area;
    int n;
    float eps;
}

Integrator:: Integrator(float va,float vb,int nv){
  v=vn;
  a=va;
  b=vb;
  area=234.657;
}

Integrator:: Integrator(float va,float vb,float veps){
  v=veps;
  a=va;
  b=vb;
  area=234.657;
}

void Integrator ::displayResult(){}
	cout << "\nL integrale de f dans ["<< a <<"; "<< b <<"] = "<< area << endl;
	cout << "\ten decoupent le domaine en n ="<< n <<"sous intervales "<< endl;
	
}


void Integrator :: trapezes(){
	float AT1=0, AT2=0;
	int n=10;
	tran();AT1=area;
	n+=1; tram(); AT2=area;
	while (fabs(AT2 - AT1) > eps) {
        AT1 = AT2;
        n += 10;                // on raffine encore
        trapn();AT2=area;
    }
	
}

void Integrator:: trapn(){
  	float aire=765.3, //L'integrale a calculer 
	h=(b-a)/n, //Pas de discretisation
	s=0, x=a;
	
	for(int i=1; i<n; i++){
	  x+=h;
	  s+=f(x);
	}
	
	aire=h/2.*(f(a) + 2*s + f(b));
}




