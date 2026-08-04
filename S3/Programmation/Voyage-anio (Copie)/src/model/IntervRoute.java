package model;

public class IntervRoute {

    private int id;
    private String nomIntervalle;
    private String rn;
    private double departKm;
    private double arriveKm;
    private double pluie;

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nomIntervalle; }
    public void setNom(String nomIntervalle) { this.nomIntervalle = nomIntervalle; }   
    
    public String getRn() { return rn; }
    public void setRn(String rn) { this.rn = rn; }

    public double getDepartKm() { return departKm; }
    public void setDepartKm(double departKm) { this.departKm = departKm; }

    public double getArriveKm() { return arriveKm; }
    public void setArriveKm(double arriveKm) { this.arriveKm = arriveKm; }

    public double getPluie() { return pluie; }
    public void setPluie(double pluie) { this.pluie = pluie; }
}
