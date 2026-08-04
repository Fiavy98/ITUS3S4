package model;

public class Route {
    private int id;
    private String rn;
    private int idVilleDepart;
    private int idVilleArriver;
    private double longueurKm;

    public Route() {}

    public Route(int id, String rn, int idVilleDepart, int idVilleArriver, double longueurKm) {
        this.id = id;
        this.rn = rn;
        this.idVilleDepart = idVilleDepart;
        this.idVilleArriver = idVilleArriver;
        this.longueurKm = longueurKm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public int getIdVilleDepart() {
        return idVilleDepart;
    }

    public void setIdVilleDepart(int idVilleDepart) {
        this.idVilleDepart = idVilleDepart;
    }

    public int getIdVilleArriver() {
        return idVilleArriver;
    }

    public void setIdVilleArriver(int idVilleArriver) {
        this.idVilleArriver = idVilleArriver;
    }

    public double getLongueurKm() {
        return longueurKm;
    }

    public void setLongueurKm(double longueurKm) {
        this.longueurKm = longueurKm;
    }

        @Override
    public String toString() {
        return rn; // Affiche uniquement le nom de la route dans le JComboBox
    }
}
