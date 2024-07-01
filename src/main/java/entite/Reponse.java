package entite;

import java.time.LocalDate;

public class Reponse {
    private int id_rep;
    private String message;
    private LocalDate date;
    private Reclamation reclamation;
    private User user;

    public Reponse() {
    }

    public Reponse(int id_rep, String message, LocalDate date, Reclamation reclamation, User user) {
        this.id_rep = id_rep;
        this.message = message;
        this.date = date;
        this.reclamation = reclamation;
        this.user = user;
    }

    public Reponse(int id_rep, String message, LocalDate date) {
        this.id_rep = id_rep;
        this.message = message;
        this.date = date;
    }
    public Reponse(String message, LocalDate date) {
        this.message = message;
        this.date = date;
    }

    public int getId_rep() {
        return id_rep;
    }

    public void setId_rep(int id_rep) {
        this.id_rep = id_rep;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getReclamation() {
        //return reclamation.getId_rec();
        if (this.reclamation == null) {
            return 1;
        } else {
            return this.reclamation.getId_rec();
        }


    }

    public int getUser() {
        //return user.getId();
        if (this.user == null) {
            return 1;
        } else {
            return this.user.getId();

        }}


}

