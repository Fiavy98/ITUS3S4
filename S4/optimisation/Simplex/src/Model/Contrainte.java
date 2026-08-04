package src.Model;

public class Contrainte {

    private int idEquation;
    private String variable;
    private double coeff;
    private int idSymbole;
    private double value;

    public Contrainte(int idEquation, String variable,
                       double coeff, int idSymbole,
                       double value) {

        this.idEquation = idEquation;
        this.variable = variable;
        this.coeff = coeff;
        this.idSymbole = idSymbole;
        this.value = value;
    }

    public int getIdEquation() {
        return idEquation;
    }

    public String getVariable() {
        return variable;
    }

    public double getCoeff() {
        return coeff;
    }

    public int getidSymbole() {
        return idSymbole;
    }

    public double getValue() {
        return value;
    }

    public void setIdEquation(int idEquation) {
        this.idEquation = idEquation;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public void setCoeff(double coeff) {
        this.coeff = coeff;
    }

    public void setidSymbole(int idSymbole) {
        this.idSymbole = idSymbole;
    }

    public void setValue(double value) {
        this.value = value;
    }
}