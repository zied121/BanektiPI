package Service;

import entite.Reclamation;
import entite.Reponse;
import entite.User;
import util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class Reclamationservice implements IserviceReclamation<Reclamation> {

    private Connection cnx;


    public Reclamationservice() {
            try {
                cnx = DatabaseUtil.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



    public void insertPST(Reclamation Reclamation) {
        String requette="insert into reclamation(type_rec,description,statut,date_rec,id_user) " +
                "values (?, ?, ?, ?,?)";

        try  {
            PreparedStatement pst=cnx.prepareStatement(requette);
            pst.setString(1,Reclamation.getType_rec());
            pst.setString(2,Reclamation.getDescription());
            pst.setString(3,Reclamation.getStatut());
            pst.setDate(4,Date.valueOf(Reclamation.getDate_rec()));
            pst.setInt(5, Reclamation.getId_user());

            pst.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


   // @Override
    public void insert(Reclamation Reclamation) {
        String requette="insert into Reclamation(type_rec,description,statut,date_rec) " +
                "values ('"+Reclamation.getType_rec()+"','"+Reclamation.getDescription()+"''"+Reclamation.getStatut()+"',"+Reclamation.getDate_rec()+")";

        try {
            Statement ste = cnx.createStatement();
            ste.executeUpdate(requette);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Reclamation Reclamation) {
        String requete = "DELETE FROM Reclamation WHERE id_rec = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setInt(1, Reclamation.getId_rec());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Reclamation Reclamation) {
        String requete = "UPDATE Reclamation SET type_rec = ?, description = ?, statut = ?, date_rec = ? WHERE id_rec = ?";
        try (PreparedStatement pst = cnx.prepareStatement(requete)) {
            pst.setString(1, Reclamation.getType_rec());
            pst.setString(2, Reclamation.getDescription());
            pst.setString(3, Reclamation.getStatut());
            pst.setDate(4, Date.valueOf(Reclamation.getDate_rec()));
            pst.setInt(5, Reclamation.getId_rec());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reclamation> readAll(Reclamation reclamation) {
        List<Reclamation> list=new ArrayList<>();
        //String requette="select * from Reclamation where id_user="+reclamation.getId_user();
        //select r.* , COALESCE(rep.message, 'Not Treated') AS message from Reclamation r , Reponse rep where r.id_user="+reclamation.getId_user()+" and r.id_rec=rep.id_rec"
        String requette=" SELECT r.*, COALESCE(rep.message, 'Not Treated') AS message " +
                "FROM Reclamation r LEFT JOIN Reponse rep ON r.id_rec = rep.id_rec " +
                "WHERE r.id_user = " + reclamation.getId_user();
        try {
            Statement ste=cnx.createStatement();
            ResultSet rs=ste.executeQuery(requette);
            while (rs.next()){
                Reclamation Reclamation=new Reclamation();
                Reclamation.setId_rec(rs.getInt("id_rec"));
                Reclamation.setType_rec(rs.getString("type_rec"));
                Reclamation.setType_rec(rs.getString("description"));
                Reclamation.setStatut(rs.getString("statut"));
                Reclamation.setDate_rec(rs.getDate("date_rec").toLocalDate());

                Reponse reponse = new Reponse();
                reponse.setMessage(rs.getString("message"));
                Reclamation.setReponse(reponse);

                list.add(Reclamation);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<Reclamation> readAll() {
        return null;
    }

    @Override


    public Reclamation readbyid(int id) {
        String requete="select * from Reclamation where id="+id;
        Reclamation d=null;
        try {
            Statement ste=cnx.createStatement();
            ResultSet rs=ste.executeQuery(requete);
            User user = null;
            if (rs.next())// if 5tr mara un seul objet
            {
                d=new Reclamation(rs.getString(1),rs.getString(2),rs.getString(3),rs.getDate(4).toLocalDate());//, rs.getInt(5));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return d;

    }


}
