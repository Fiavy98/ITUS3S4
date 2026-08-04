package db;

import java.sql.*;
import java.util.Vector;

public class From_MedOrdFile {  
    public static void insOrdFile(String idMedicament,String IdOrdonance,int idUser,int Nbjr,double quantite,String unite,double puUnite,double prix){
        String insert = "INSERT INTO MED_ORDONNANCE_FILLE(IDMEDICAMENT,IDORDONNANCE,MY_USER,NB_JOURS,QUANTITE,UNITE,PUUNITE,PRIX) VALUES(?,?,?,?,?,?,?,?)";
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
    
                ps.setString(1, idMedicament);
                ps.setString(2, IdOrdonance);
                ps.setInt(3, idUser);
                ps.setInt(4, Nbjr);
                ps.setDouble(5, quantite);
                ps.setString(6, unite);
                ps.setDouble(7, puUnite);
                ps.setDouble(8, prix);
    
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



    //Ls ord FILLE
    public static Vector<Vector<Object>> lsOrdonnanceFille() throws SQLException {
        String sql = "SELECT * FROM MED_ORDONNANCE_FILLE ";

        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getString("IDORDONNANCE"));
                row.add(rs.getString("IDMEDICAMENT"));
                row.add(rs.getDouble("QUANTITE"));
                row.add(rs.getString("UNITE"));
                row.add(rs.getDouble("PUUNITE"));
                row.add(rs.getDouble("PRIX"));

                data.add(row);
            }
        }
    
        return data;
    }
    

    public static Vector<String> ColoneOrdonnanceFille() {
        Vector<String> colNames = new Vector<>();
        colNames.add("ID");
        colNames.add("IDORDONNANCE");        
        colNames.add("IDMEDICAMENT");
        colNames.add("QUANTITE");
        colNames.add("UNITE");        
        colNames.add("PUUNITE");
        colNames.add("PRIX");
        return colNames;
    }



    

}

