package entite;

import java.time.LocalDate;
// add read by status
public class Reclamation {
    private int id_rec;
    private String type_rec;
    private String description;
    private String statut;

    private LocalDate date_rec;
    //private int id_user;
    private User user;
    private Reponse reponse;
    public Reclamation() {
    }
    public Reclamation(int id_rec, String type_rec, String description, String statut, LocalDate date_rec, User id_user) {
        this.id_rec = id_rec;
        this.type_rec = type_rec;
        this.description = description;
        this.statut = statut;
        this.date_rec = date_rec;
        this.user = id_user;
    }

    public Reclamation(String type_rec, String description, String statut, LocalDate date_rec, int id_user) {
        this.type_rec = type_rec;
        this.description = description;
        this.statut = statut;
        this.date_rec = date_rec;
        //   this.id_user = id_user;
    }


    public Reclamation(String type_rec, String description, String statut, LocalDate date_rec) {
        this.type_rec = type_rec;
        this.description = description;
        this.statut = statut;
        this.date_rec = date_rec;
    }

    public Reclamation(String sujetRec, String typeRec) {
        this.type_rec = typeRec;
        this.description = sujetRec;
    }

    public int getId_rec() {
        return id_rec;
    }

    public void setId_rec(int id_rec) {
        this.id_rec = id_rec;
    }

    public String getType_rec() {
        return type_rec;
    }

    public void setType_rec(String type_rec) {
        this.type_rec = type_rec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDate getDate_rec() {
        return date_rec;
    }

    public void setDate_rec(LocalDate date_rec) {
        this.date_rec = date_rec;
    }

    public int getId_user() {
//        return user.getId();
        if (this.user == null) {
            return 1;
        } else {
            return this.user.getId();

        }}


//    public int getId_user() {
//        return id_user;
//    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id_rec=" + id_rec +
                ", type_rec='" + type_rec + '\'' +
                ", description='" + description + '\'' +
                ", statut=" + statut +
                ", date_rec='" + date_rec + '\'' +
                ", id_user=" + user +
                '}';
    }

    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse(Reponse reponse) {
        this.reponse=reponse;
    }
}
