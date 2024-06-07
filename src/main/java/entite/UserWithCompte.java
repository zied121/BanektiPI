package entite;

import java.sql.Date;
import java.sql.Timestamp;

public class UserWithCompte {
    private int userId;
    private String nom;
    private String prenom;
    private int age;
    private String mdp;
    private int cin;
    private String role;
    private int nbCompte;
    private String type;
    private int num;
    private int rib;
    private Timestamp dateOuverture;
    private Date dateValidite;
    private String statut;
    private float solde;
    private String devise;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
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
    public int getCin() {
        return cin;
    }
    public void setCin(int cin) {
        this.cin = cin;
    }
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public int getNbCompte() {
        return nbCompte;
    }
    public void setNbCompte(int nbCompte) {
        this.nbCompte = nbCompte;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getNum() {
        return num;
    }
    public void setNum(int num) {
        this.num = num;
    }
    public int getRib() {
        return rib;
    }
    public void setRib(int rib) {
        this.rib = rib;
    }
    public Timestamp getDateOuverture() {
        return dateOuverture;
    }
    public void setDateOuverture(Timestamp dateOuverture) {
        this.dateOuverture = dateOuverture;
    }
    public Date getDateValidite() {
        return dateValidite;
    }
    public void setDateValidite(Date dateValidite) {
        this.dateValidite = dateValidite;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public float getSolde() {
        return solde;
    }
    public void setSolde(float solde) {
        this.solde = solde;
    }
    public String getDevise() {
        return devise;
    }
    public void setDevise(String devise) {
        this.devise = devise;
    }
}
