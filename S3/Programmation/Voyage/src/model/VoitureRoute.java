package model;

import java.time.LocalDateTime;

public class VoitureRoute {
    private int id;
    private Voiture voiture;   // objet Voiture
    private String codeRoute;
    private Status status;     // objet Status
    private LocalDateTime duree;

    public VoitureRoute() {}

    public VoitureRoute(int id, Voiture voiture, String codeRoute, Status status, LocalDateTime duree) {
        this.id = id;
        this.voiture = voiture;
        this.codeRoute = codeRoute;
        this.status = status;
        this.duree = duree;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Voiture getVoiture() { return voiture; }
    public void setVoiture(Voiture voiture) { this.voiture = voiture; }

    public String getCodeRoute() { return codeRoute; }
    public void setCodeRoute(String codeRoute) { this.codeRoute = codeRoute; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getDuree() { return duree; }
    public void setDuree(LocalDateTime duree) { this.duree = duree; }
}
