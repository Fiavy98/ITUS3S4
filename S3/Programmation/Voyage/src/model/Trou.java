package model;

public class Trou {
    private int id;
    private String nom;
    private int idRoute;
    private double positionKm;
    private double profondeur;
    private double surface;

    public Trou() {}

    public Trou(int id,String nom,int idRoute, double positionKm, double profondeur, double surface) {
        this.id = id;
        this.nom=nom;
        this.idRoute = idRoute;
        this.positionKm = positionKm;
        this.profondeur = profondeur;
        this.surface = surface;
    }

    // --- Getters et setters ---
    public int getId() { return id; }
    public void setNom(String nom) { this.nom = nom; }

    public String getNom() { return nom; }
    public void setId(int id) { this.id = id; }

    public int getIdRoute() { return idRoute; }
    public void setIdRoute(int idRoute) { this.idRoute = idRoute; }

    public double getPositionKm() { return positionKm; }
    public void setPositionKm(double positionKm) { this.positionKm = positionKm; }

    public double getProfondeur() { return profondeur; }
    public void setProfondeur(double profondeur) { this.profondeur = profondeur; }

    public double getSurface() { return surface; }
    public void setSurface(double surface) { this.surface = surface; }
}
