package entite;

import java.time.LocalDate;

public class Document {
private int id;
private String nom;
private String type;
private String url;
private LocalDate date;
    private int id_user;


    public Document(int id, String nom, String type, String url, LocalDate date, int id_user) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.url = url;
        this.date = date;
        this.id_user = id_user;
    }

    public Document(String nom, String type, String url, LocalDate date, int id_user) {
        this.nom = nom;
        this.type = type;
        this.url = url;
        this.date = date;
        this.id_user = id_user;
    }

    public Document(String nom, String type, String url,LocalDate date) {
        this.nom = nom;
        this.type = type;
        this.url = url;
        this.date = date;

    }

    public Document(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", date=" + date +
                ", user=" + id_user +
                '}';
    }
}
