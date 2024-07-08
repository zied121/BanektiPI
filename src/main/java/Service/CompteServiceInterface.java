package Service;

import entite.Compte;

import java.sql.SQLException;
import java.util.List;

public interface CompteServiceInterface {
    List<Compte> getComptesByUserId(int userId) throws SQLException;
    void createCompte(Compte compte) throws SQLException;
    void updateCompte(Compte compte) throws SQLException;
    void deleteCompte(int compteId, int userId) throws SQLException;
    boolean hasCourantAccount(int userId) throws SQLException;
}
