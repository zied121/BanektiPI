package entite;

public class Assurance {
    private Integer id;
    private String idUser;
    private String type;
    private String dateDebut;
    private String dateFin;
    private String document;
    private String image;

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(String id) {
        this.id = Integer.valueOf(id);
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return null;
    }

    public void setStatus(String newStatus) {
    }
}
