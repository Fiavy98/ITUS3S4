package model;

import java.util.Date;

public class ReparerPrix {

    private double positionKm;
    private double prix;
    private Date dateReparation;

    public double getPositionKm() {
        return positionKm;
    }

    public void setPositionKm(double positionKm) {
        this.positionKm = positionKm;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public Date getDateReparation() {
        return dateReparation;
    }

    public void setDateReparation(Date dateReparation) {
        this.dateReparation = dateReparation;
    }
}
