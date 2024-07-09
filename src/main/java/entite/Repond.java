package entite;

import java.time.LocalDate;

public class Repond {
    private int id;
    private String reponse;
    private LocalDate date;
    private int id_rec;

    private String TypeOperation;
    private Double MontantOperation;
    // Clé étrangère de la table Reclamation
    private Reclamation Reclamation;

    public Repond() {}

    public Repond(int id, String reponse, LocalDate date, int id_rec) {
        this.id = id;
        this.reponse = reponse;
        this.date = date;
        this.id_rec = id_rec;
    }

    public Repond(String reponse, LocalDate date, int id_rec) {
        this.reponse = reponse;
        this.date = date;
        this.id_rec = id_rec;
    }

    public Repond(String reponse, LocalDate date) {
        this.reponse = reponse;
        this.date = date;
    }

    public Repond(int id, String reponse, LocalDate date, int id_rec, Reclamation Reclamation) {
        this.id = id;
        this.reponse = reponse;
        this.date = date;
        this.id_rec = id_rec;
        this.Reclamation = Reclamation;
    }



    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public int getId_rec() { return id_rec; }
    public void setId_rec(int id_rec) { this.id_rec = id_rec; }
    public Reclamation getReclamation() { return Reclamation; }
    public void setReclamation(Reclamation Reclamation) { this.Reclamation = Reclamation; }

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
        return "Repond{" +
                "id=" + id +
                ", reponse='" + reponse + '\'' +
                ", date=" + date +
                ", id_rec=" + id_rec +
                '}';
    }



}
