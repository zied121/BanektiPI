package entite;

import java.util.Date;

public class Operation {
    private Long id;
    private String typeOp;
    private Double montant;
    private Long idCompte;
    private Date dateOp;
        private Long idCompteDestination;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(Long idCompte) {
        this.idCompte = idCompte;
    }

    public Date getDateOp() {
        return dateOp;
    }

    public void setDateOp(Date dateOp) {
        this.dateOp = dateOp;
    }

    public Long getIdCompteDestination() {
        return idCompteDestination;
    }

    public void setIdCompteDestination(Long idCompteDestination) {
        this.idCompteDestination = idCompteDestination;
    }
}
