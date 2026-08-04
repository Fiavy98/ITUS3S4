package model;

public class RouteVille {
    private int id;
    private int idRoute;
    private int idVille;
    private double positionKm;

    public RouteVille() {}

    public RouteVille(int id, int idRoute, int idVille, double positionKm) {
        this.id = id;
        this.idRoute = idRoute;
        this.idVille = idVille;
        this.positionKm = positionKm;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRoute() {
        return idRoute;
    }

    public void setIdRoute(int idRoute) {
        this.idRoute = idRoute;
    }

    public int getIdVille() {
        return idVille;
    }

    public void setIdVille(int idVille) {
        this.idVille = idVille;
    }

    public double getPositionKm() {
        return positionKm;
    }

    public void setPositionKm(double positionKm) {
        this.positionKm = positionKm;
    }
}
