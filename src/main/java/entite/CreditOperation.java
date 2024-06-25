package entite;

import java.sql.Date;

public class CreditOperation {
    private int idCompte;
    private String typeCredit;
    private double montant;
    private Date dateDebut;
    private Date dateFin;
    private double tauxInteret;
    private String statut;
    private String modePaiement;

    public CreditOperation(int idCompte, String typeCredit, double montant, Date dateDebut, Date dateFin, double tauxInteret, String statut, String modePaiement) {
        this.idCompte = idCompte;
        this.typeCredit = typeCredit;
        this.montant = montant;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tauxInteret = tauxInteret;
        this.statut = statut;
        this.modePaiement = modePaiement;
    }

    // Getters and setters
    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(int idCompte) {
        this.idCompte = idCompte;
    }

    public String getTypeCredit() {
        return typeCredit;
    }

    public void setTypeCredit(String typeCredit) {
        this.typeCredit = typeCredit;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getTauxInteret() {
        return tauxInteret;
    }

    public void setTauxInteret(double tauxInteret) {
        this.tauxInteret = tauxInteret;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getModePaiement() {
        return modePaiement;
    }

    public void setModePaiement(String modePaiement) {
        this.modePaiement = modePaiement;
    }
}
