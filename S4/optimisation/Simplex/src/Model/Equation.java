package src.Model;

public class Equation {

    private int id;
    private int nbVariable;
    private double[] coeff;

    public Equation(int id,int nbVariable, double[] coeff) {
        this.id = id;
        this.nbVariable = nbVariable;
        this.coeff = coeff;
    }

    public int getId() {
        return id;
    }

    public int getNbVariable() {
        return nbVariable;
    }

    public double[] getCoeff() {
        return coeff;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setNbVariable(int nbVariable) {
        this.nbVariable = nbVariable;
    }

    public void setCoeff(double[] coeff) {
        this.coeff = coeff;
    }
}