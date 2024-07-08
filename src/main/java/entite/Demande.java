package entite;

public class Demande {
    private String type;
    private String description;
    private String statut;
    private String date;

    private int idUser;

    // Constructor, getters, and setters
    public Demande(String type, String description, String statut, String date,  int idUser) {
        this.type = type;
        this.description = description;
        this.statut = statut;
        this.date = date;

        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}

