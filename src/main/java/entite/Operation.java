package entite;

import java.util.Date;

public class Operation {
    private int id;
    private String typeOp;
    private Double montant;
    private int idCompte;
    private Date dateOp;
    private Long RIbCompteDestination;

    // Getters and Setters
    public  int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(String typeOp) {
        this.typeOp = typeOp;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public int getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Integer idCompte) {
        this.idCompte = idCompte;
    }

    public Date getDateOp() {
        return dateOp;
    }

    public void setDateOp(Date dateOp) {
        this.dateOp = dateOp;
    }

    public Long getRibCompteDestination() {
        return RIbCompteDestination;
    }

    public void setRibCompteDestination(Long idCompteDestination) {
        this.RIbCompteDestination = idCompteDestination;
    }
}
