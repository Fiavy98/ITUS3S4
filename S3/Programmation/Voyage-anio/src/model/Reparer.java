package model;


import java.util.Date;

public class Reparer {

    private int id;
    private int idTrou;
    private int idRoute;
    private int idTypeRoute;
    private Date dateReparation;
    private String description;

    public Reparer() {}

    public Reparer(int id, int idTrou, int idRoute, int idTypeRoute, Date dateReparation, String description) {
        this.id = id;
        this.idTrou = idTrou;
        this.idRoute = idRoute;
        this.idTypeRoute = idTypeRoute;
        this.dateReparation = dateReparation;
        this.description = description;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdTrou() { return idTrou; }
    public void setIdTrou(int idTrou) { this.idTrou = idTrou; }

    public int getIdRoute() { return idRoute; }
    public void setIdRoute(int idRoute) { this.idRoute = idRoute; }

    public int getIdTypeRoute() { return idTypeRoute; }
    public void setIdTypeRoute(int idTypeRoute) { this.idTypeRoute = idTypeRoute; }

    public Date getDateReparation() { return dateReparation; }
    public void setDateReparation(Date dateReparation) { this.dateReparation = dateReparation; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
