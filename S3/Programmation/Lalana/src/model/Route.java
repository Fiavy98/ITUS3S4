package model;

public class Route {
    private int id;
    private String rn;
    private int idVilleDepart;
    private int idVilleArrive;
    private double longueurKm;

    public Route(int id, String rn, int idVilleDepart, int idVilleArrive, double longueurKm) {
        this.id = id;
        this.rn = rn;
        this.idVilleDepart = idVilleDepart;
        this.idVilleArrive = idVilleArrive;
        this.longueurKm = longueurKm;
    }

    public int getId() { return id; }
    public String getRn() { return rn; }
    public int getIdVilleDepart() { return idVilleDepart; }
    public int getIdVilleArrive() { return idVilleArrive; }
    public double getLongueurKm() { return longueurKm; }

    @Override
    public String toString() {
        return rn + " (" + idVilleDepart + " -> " + idVilleArrive + ", " + longueurKm + " km)";
    }
}
