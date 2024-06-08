package entite;

import java.time.LocalDate;


public class User {
    private int id;
    private String nom;
    private String prenom;
    private int age;
    private String mdp;
    private String CIN;
    private String role;
    private int nb_compte;
    private LocalDate date_creation;

    public User() {
    }

    public User(int id, String nom, String prenom, int age, String mdp, String CIN, String role, int nb_compte, LocalDate date_creation) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.mdp = mdp;
        this.CIN = CIN;
        this.role = role;
        this.nb_compte = nb_compte;
        this.date_creation = date_creation;
    }

    public User(String nom, String prenom, int age, String mdp, String CIN, String role, int nb_compte, LocalDate date_creation) {
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.mdp = mdp;
        this.CIN = CIN;
        this.role = role;
        this.nb_compte = nb_compte;
        this.date_creation = date_creation;
    }

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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNb_compte() {
        return nb_compte;
    }

    public void setNb_compte(int nb_compte) {
        this.nb_compte = nb_compte;
    }

    public LocalDate getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDate date_creation) {
        this.date_creation = date_creation;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", mdp='" + mdp + '\'' +
                ", CIN='" + CIN + '\'' +
                ", role='" + role + '\'' +
                ", nb_compte=" + nb_compte +
                ", date_creation=" + date_creation +
                '}';
    }
}
