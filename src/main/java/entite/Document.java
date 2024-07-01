package entite;

import java.time.LocalDate;

public class Document {
    private int id;
    private String reponse;
    private LocalDate date;
    private Demande demande; // Relation avec la demande

    public Document(int id, String reponse, LocalDate date, Demande demande) {
        this.id = id;
        this.reponse = reponse;
        this.date = date;
        this.demande = demande;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Demande getDemande() {
        return demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }
}
