package entite;

public class Credit {
    private String id;



    private String iduser;
    private String idCompte;
        private String typeCredit;
        private double montant;
        private String status;
        private String echeancier;
        private String document;

    public Credit(String id, String idCompte, String typeCredit, double montant, String status, String echeancier, String document) {
        this.id = id;
        this.idCompte = idCompte;
        this.typeCredit = typeCredit;
        this.montant = montant;
        this.status = status;
        this.echeancier = echeancier;
        this.document = document;
    }

    public Credit() {

    }

    public Credit(String userId, String typeCredit, double montant, String statut, String echeancier, String document) {
    }

    // Getters and setters
    public String getIduser() {
        return iduser;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(String idCompte) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEcheancier() {
        return echeancier;
    }

    public void setEcheancier(String echeancier) {
        this.echeancier = echeancier;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Credit{" +
                "id='" + id + '\'' +
                ", idCompte='" + idCompte + '\'' +
                ", typeCredit='" + typeCredit + '\'' +
                ", montant=" + montant +
                ", statut='" + status + '\'' +
                ", echeancier='" + echeancier + '\'' +
                ", document='" + document + '\'' +
                '}';
    }
}
