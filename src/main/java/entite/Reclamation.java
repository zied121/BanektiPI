package entite;

import java.time.LocalDate;

public class Reclamation {
    private int id;
    private String type;
    private String statut;
    private String description;
    private int id_user; // Clé étrangère de la table User
    private LocalDate date;
    private String reponse;
    private Repond Repond;
    private String rib;
    private String nom;
    private String prenom;
    private int nb_compte;
    private double solde;
    private String statut_compte;
    private String type_op;
    private double montant;
    private LocalDate date_op;
    private String email;


    public Reclamation() {}

    public Reclamation(int id, String type, String statut, String description, int id_user, LocalDate date) {
        this.id = id;
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.id_user = id_user;
        this.date = date;
    }

    public Reclamation(String type, String statut, String description, int id_user, LocalDate date) {
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.id_user = id_user;
        this.date = date;
    }

    public Reclamation(String type, String statut, String description, LocalDate date) {
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.date = date;
    }

    public Reclamation(String type, String statut, String description) {
        this.type = type;
        this.statut = statut;
        this.description = description;
    }

    public Reclamation(String type, String statut, String description, LocalDate date, int id_user) {
        this.type = type;
        this.statut = statut;
        this.description = description;
        this.date = date;
        this.id_user = id_user;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public int getNb_compte() {
        return nb_compte;
    }

    public void setNb_compte(int nb_compte) {
        this.nb_compte = nb_compte;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public String getStatut_compte() {
        return statut_compte;
    }

    public void setStatut_compte(String statut_compte) {
        this.statut_compte = statut_compte;
    }

    public String getType_op() {
        return type_op;
    }

    public void setType_op(String type_op) {
        this.type_op = type_op;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
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

    public LocalDate getDate_op() {
        return date_op;
    }

    public void setDate_op(LocalDate date_op) {
        this.date_op = date_op;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Repond getRepond() {
        return Repond;
    }

    public void setRepond(Repond Repond) {
        this.Repond = Repond;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Reclamation{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", statut='" + statut + '\'' +
                ", description='" + description + '\'' +
                ", id_user=" + id_user +
                ", date=" + date +
                '}';
    }
}
