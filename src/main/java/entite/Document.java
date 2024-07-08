package entite;

import java.time.LocalDate;

public class Document {
    private int id;
    private String reponse;
    private LocalDate date;
    private int id_dem;

    private String TypeOperation;
    private Double MontantOperation;
    // Clé étrangère de la table Demande
    private Demande demande;

    public Document() {}

    public Document(int id, String reponse, LocalDate date, int id_dem) {
        this.id = id;
        this.reponse = reponse;
        this.date = date;
        this.id_dem = id_dem;
    }

    public Document(String reponse, LocalDate date, int id_dem) {
        this.reponse = reponse;
        this.date = date;
        this.id_dem = id_dem;
    }

    public Document(String reponse, LocalDate date) {
        this.reponse = reponse;
        this.date = date;
    }

    public Document(int id, String reponse, LocalDate date, int idDem, Demande demande) {
        this.id = id;
        this.reponse = reponse;
        this.date = date;
        this.id_dem = idDem;
        this.demande = demande;
    }



    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getId_dem() { return id_dem; }
    public void setId_dem(int id_dem) { this.id_dem = id_dem; }
    public Demande getDemande() { return demande; }
    public void setDemande(Demande demande) { this.demande = demande; }

    public String getTypeOperation() {
        return TypeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        TypeOperation = typeOperation;
    }

    public Double getMontantOperation() {
        return MontantOperation;
    }

    public void setMontantOperation(Double montantOperation) {
        MontantOperation = montantOperation;
    }

    @Override
    public String toString() {
        return "Document{" +
                "id=" + id +
                ", reponse='" + reponse + '\'' +
                ", date=" + date +
                ", id_dem=" + id_dem +
                '}';
    }



}
