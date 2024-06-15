package entite;

import java.sql.Date;
import java.sql.Timestamp;

public class Compte {
    private int id;
    private String type;
    private int num;
    private long rib;
    private Timestamp dateOuverture;
    private Date dateValidite;
    private String statut;
    private float solde;
    private String devise;
    private int userId;

    // Getters and Setters
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getRib() {
        return rib;
    }

    public void setRib(long rib) {
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
