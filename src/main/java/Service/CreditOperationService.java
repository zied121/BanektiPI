package Service;

import entite.CreditOperation;
import util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreditOperationService {

    public boolean validateCreditOperation(CreditOperation creditOperation) {
        if (creditOperation.getMontant() <= 0) {
            return false;
        }
        return true;
    }

    public void processCreditOperation(CreditOperation creditOperation) {
        String query = "INSERT INTO credit (id_compte, type_credit, montant, date_debut, date_fin, taux_interet, statut, mode_paiement) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, creditOperation.getIdCompte());
            pstmt.setString(2, creditOperation.getTypeCredit());
            pstmt.setDouble(3, creditOperation.getMontant());
            pstmt.setDate(4, creditOperation.getDateDebut());
            pstmt.setDate(5, creditOperation.getDateFin());
            pstmt.setDouble(6, creditOperation.getTauxInteret());
            pstmt.setString(7, creditOperation.getStatut());
            pstmt.setString(8, creditOperation.getModePaiement());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
