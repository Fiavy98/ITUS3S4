package db;

import java.sql.*;
import java.util.Vector;

public class From_MedOrdFile {  
    public static void insOrdFile(String idMedicament,String IdOrdonance,int idUser,int Nbjr,int quantite){
        String insert = "INSERT INTO MED_ORDONNANCE_FILLE(IDMEDICAMENT,IDORDONNANCE,MY_USER,NB_JOURS,QUANTITE) VALUES(?,?,?,?,?)";
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
    
                ps.setString(1, idMedicament);
                ps.setString(2, IdOrdonance);
                ps.setInt(3, idUser);
                ps.setInt(4, Nbjr);
                ps.setInt(5, quantite);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ Ordonnance Fille insérée !");
                } else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }


    //VIeW
    public static Vector<Vector<Object>> lsOrdonnanceInterneFait() throws SQLException {
        String sql = "SELECT * FROM MED_ORDONNANCE_LIBELLE ORDER BY DATY";
    
        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getDate("DATY"));
                row.add(rs.getString("NB_JOURS"));
                row.add(rs.getString("OBSERVATION"));
                row.add(rs.getString("MEDECIN"));
                data.add(row);
            }
        }
    
        return data;
    }
    

    public static Vector<String> ColoneOrdonnance() {
        Vector<String> colNames = new Vector<>();
        colNames.add("IDORDONNANCE");
        colNames.add("DATE");
        colNames.add("NB_JOURS");
        colNames.add("OBSERVATION");        
        colNames.add("MEDECIN");
        return colNames;
    }



    

}

