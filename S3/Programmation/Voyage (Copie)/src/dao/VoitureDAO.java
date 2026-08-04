package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.Statement;
import connection.*;
import model.Voiture;

public class VoitureDAO {

         public static Vector<Voiture> lsVoiture() {
        Vector<Voiture> voitures = new Vector<>();
        String sql = "SELECT v.id, v.id_type, v.vMax, v.vMin, v.logueur, v.largeur, t.nom as type_nom " +
                     "FROM voiture v JOIN type t ON v.id_type = t.id";

        try (Connection conn = PostgresConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Voiture v = new Voiture();
                v.setId(rs.getInt("id"));
                v.setIdType(rs.getInt("id_type")); 
                v.setVMax(rs.getDouble("vMax"));
                v.setVMin(rs.getDouble("vMin"));
                v.setLogueur(rs.getDouble("logueur"));
                v.setLargeur(rs.getDouble("largeur"));
                v.setNomType(rs.getString("type_nom")); // attribut pour afficher dans JComboBox
                voitures.add(v);
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL lsVoiture : " + e.getMessage());
        }

        return voitures;
    }

    public static Vector<String> CollisteVoiture() {
        Vector<String> colNames = new Vector<>();
        colNames.add("ID");
        colNames.add("IMAGE");
        colNames.add("NOM");
        colNames.add("VITESSE MAX");
        colNames.add("VITESSE MIN");        
        colNames.add("LONGUEUR");
        colNames.add("LARGEUR");
        colNames.add("ACTION");
        return colNames;
    }


    public static Vector<Object> voitureChoisit(int id_voiture) throws SQLException {
          String sql = "SELECT v.id as ID, v.img, t.nom" +
                 "FROM voiture v " +
                 "JOIN type t ON v.id_type = t.id " +
                 "WHERE v.id = ?"; 

            Vector<Object> voiture = new Vector<>();

        try (Connection conn = PostgresConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, id_voiture);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    voiture.add(rs.getInt("ID"));
                    String imgName = rs.getString("img");
                    voiture.add(ImageUtils.loadImage(imgName, 80, 60)); 
                    voiture.add(rs.getString("NOM"));
                    voiture.add(rs.getDouble("vMax"));
                    voiture.add(rs.getDouble("vMin"));
                    voiture.add(rs.getDouble("logueur"));
                    voiture.add(rs.getDouble("largeur"));

                } else {
                    System.out.println("Auccun voiture");
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return voiture;
    }

        public static Voiture getVoitureById(int idVoiture) {
        String sql = "SELECT v.id, v.id_type, v.vmax, v.vmin, v.logueur, v.largeur, t.nom AS nom_type " +
                     "FROM voiture v " +
                     "JOIN type t ON v.id_type = t.id " +
                     "WHERE v.id = ?";

        try (Connection conn = PostgresConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVoiture);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Voiture v = new Voiture();
                v.setId(rs.getInt("id"));
                v.setIdType(rs.getInt("id_type"));
                v.setVMax(rs.getDouble("vmax"));
                v.setVMin(rs.getDouble("vmin"));
                v.setLogueur(rs.getDouble("logueur"));
                v.setLargeur(rs.getDouble("largeur"));
                v.setNomType(rs.getString("nom_type"));
                return v;
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL getVoitureById : " + e.getMessage());
        }

        return null; // si aucune voiture trouvée
    }

}
