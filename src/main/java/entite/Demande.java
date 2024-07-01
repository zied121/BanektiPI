package entite;

import java.time.LocalDate;

public class Demande {
    private int id;
    private String type;
    private String statut;
    private String description;
    private int id_user; // Clé étrangère de la table User
    private LocalDate date;

    public Demande() {
    }

    public Demande(int id, String type, String statut, String description, int id_user, LocalDate date) {
        this.id = id;
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.id_user = id_user;
        this.date = date;
    }

    public Demande(String type, String statut, String description, int id_user, LocalDate date) {
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.id_user = id_user;
        this.date = date;
    }

    public Demande(String type, String statut, String description, LocalDate date) {
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.date = date;
    }

    public Demande(String type, String statut, String description) {
        this.type = type;
        this.statut = statut;
        this.description = description;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                ", description='" + description + '\'' +
                ", id_user=" + id_user +
                ", date=" + date +
                '}';
    }
}
