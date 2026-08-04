package db;

import java.sql.*;
import java.util.Vector;

public class From_unite {
    public static Vector<String[]> lsUniteParMedicament(String idMed) {
        String sql = "SELECT eq.ID AS IDEQ,un.ID IDUNIT, un.DESCE AS NAME, eq.QTE, eq.PV " +
                     "FROM AS_EQUIVALENCE eq " +
                     "JOIN AS_UNITE un ON eq.IDUNITE = un.ID " +
                     "WHERE eq.IDPRODUIT = ?";
    
        Vector<String[]> listeUnites = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, idMed);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                String idEquivalence = rs.getString("IDEQ");
                String desc = rs.getString("NAME");
                String qte = String.valueOf(rs.getInt("QTE"));
                String pv = String.valueOf(rs.getDouble("PV"));
                String idUnit = rs.getString("IDUNIT");
    
                listeUnites.add(new String[]{idEquivalence, desc, qte, pv,idUnit});
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }   
        return listeUnites;
    }


    public static Double getPUunite(String idMed,String idUnit){
        double PV =0.0;
        String query = "SELECT * FROM AS_EQUIVALENCE WHERE IDPRODUIT=? AND IDUNITE=?";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
    
            ps.setString(1, idMed);
            ps.setString(2, idUnit);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PV = rs.getDouble("PV");
                } else {
                    System.out.println("⚠️Tsy mety e ");
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return PV;
    }


    public static double calculPrix(String idProduit, String idUnite, int qte) {
        double pv = From_unite.getPUunite(idProduit, idUnite); 
        return qte * pv;
    }
    


        
}
