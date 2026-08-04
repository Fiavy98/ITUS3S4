package db;

import java.sql.*;
import java.util.Vector;

import javax.swing.JOptionPane;

public class From_Stock {
    public static String insMvtSock_enter(java.util.Date dt, String idMagasin){
        String sqlInsert = "INSERT INTO MVTSTOCK(DATY, IDMAGASIN, IDTYPEMVSTOCK) VALUES (?, ?,'TPMVST000001')";
        String id = null;

        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert,new String[]{"ID"})) {
    
                ps.setDate(1, new java.sql.Date(dt.getTime()));
                ps.setString(2, idMagasin);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ mvt insérée !");

                        // Recupere id
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getString(1);
                        }
                    }
                }else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
           }
        }catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return id;
    }

    public static String insMvtSock_sortie(java.util.Date dt, String idMagasin){
        String sqlInsert = "INSERT INTO MVTSTOCK(DATY, IDMAGASIN, IDTYPEMVSTOCK) VALUES (?, ?,'TPMVST000022')";
        String id = null;
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert,new String[]{"ID"})) {
    
                ps.setDate(1, new java.sql.Date(dt.getTime()));
                ps.setString(2, idMagasin);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ mvt insérée !");

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getString(1);
                        }
                    }
                }else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
           }
        }catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return id;
    }

    public static String insMvtSock_inventaire(java.util.Date dt, String idMagasin){
        String sqlInsert = "INSERT INTO MVTSTOCK(DATY, IDMAGASIN, IDTYPEMVSTOCK) VALUES (?, ?,'TPMVST000023')";
        String id = null;
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sqlInsert,new String[]{"ID"})) {
    
                ps.setDate(1, new java.sql.Date(dt.getTime()));
                ps.setString(2, idMagasin);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ mvt insérée !");

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            id = rs.getString(1);
                        }
                    }
                }else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
           }
        }catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return id;
    }
    


    public static String insMvtStockFille(String idMvt, String idProduit, double entree, double sortie) {

        String SelectEtat = "SELECT NVL(RESTE,0) AS RESTE FROM ETATSTOCK " +
                               "WHERE IDMEDICAMENT=? AND IDMAGASIN=(SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?)";
    
        String UpdateEtat = "UPDATE ETATSTOCK SET ENTREE = ENTREE + ?, SORTIE = SORTIE + ?, " +
                               "RESTE = RESTE + ? - ?, DATY = SYSDATE " +
                               "WHERE IDMEDICAMENT=? AND IDMAGASIN=(SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?)";
    
        String sqlInsertEtat = "INSERT INTO ETATSTOCK(IDMEDICAMENT, IDMAGASIN, ENTREE, SORTIE, RESTE, DATY) " +
                               "SELECT ?, (SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?), ?, ?, ?, SYSDATE FROM dual";
    
        String insertFille = "INSERT INTO MVTSTOCKFILLE(IDMVTSTOCK, IDPRODUIT, ENTREE, SORTIE, RESTE) " +
                                "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = OracleConnection.getConnection()) {
            
            double resteAvant = 0;
            try (PreparedStatement ps = conn.prepareStatement(SelectEtat)) {
                ps.setString(1, idProduit);
                ps.setString(2, idMvt);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) resteAvant = rs.getDouble("RESTE");
            }

            double resteApres = resteAvant + entree - sortie;
            if (resteApres < 0) {
                System.out.println("❌ Stock insuffisant !");
                JOptionPane.showMessageDialog(null, "❌ Stock insuffisant !");
                conn.rollback();
                return null;
            }
    
            int updated;
            try (PreparedStatement ps = conn.prepareStatement(UpdateEtat)) {
                ps.setDouble(1, entree);
                ps.setDouble(2, sortie);
                ps.setDouble(3, entree);
                ps.setDouble(4, sortie);
                ps.setString(5, idProduit);
                ps.setString(6, idMvt);
                updated = ps.executeUpdate();
            }
    
            if (updated == 0) {
                try (PreparedStatement ps = conn.prepareStatement(sqlInsertEtat)) {
                    ps.setString(1, idProduit);
                    ps.setString(2, idMvt);
                    ps.setDouble(3, entree);
                    ps.setDouble(4, sortie);
                    ps.setDouble(5, resteApres);
                    ps.executeUpdate();
                }
            }
    
            try (PreparedStatement ps = conn.prepareStatement(insertFille)) {
                ps.setString(1, idMvt);
                ps.setString(2, idProduit);
                ps.setDouble(3, entree);
                ps.setDouble(4, sortie);
                ps.setDouble(5, resteApres);
                ps.executeUpdate();
            }
    
            System.out.println("✔ mouvement + mise à jour ETATSTOCK OK");
    
            return "OK"; 
    
        } catch (Exception e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
            return null;
        }
    }


    public static Vector<Vector<Object>> lsEtatStk() throws SQLException {
        String sql = "SELECT * FROM ETATSTOCK ORDER BY DATY ";
    
        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getDate("DATY"));
                row.add(rs.getString("IDMEDICAMENT"));
                row.add(rs.getString("IDMAGASIN"));
                row.add(rs.getDouble("ENTREE"));
                row.add(rs.getDouble("SORTIE"));
                row.add(rs.getDouble("RESTE"));

                data.add(row);
            }
        }
    
        return data;
    }
    

    public static Vector<String> ColoneEtatStk() {
        Vector<String> colNames = new Vector<>();
        colNames.add("ID");
        colNames.add("DATY");
        colNames.add("IDPRODUIT");
        colNames.add("ENTREE");        
        colNames.add("SORTIE");
        colNames.add("RESTE");
        return colNames;
    }


    //===============RETOURNER=======================
    public static String retourner(String idOrdonnance, String idMedicament, double qteRetour) {

        String SelectOrd = 
            "SELECT QUANTITE FROM MED_ORDONNANCE_FILLE " +
            "WHERE IDMEDICAMENT=? AND IDORDONNANCE=?";
    
        String UpdateOrd = 
            "UPDATE MED_ORDONNANCE_FILLE SET QUANTITE = QUANTITE - ? " +
            "WHERE IDMEDICAMENT=? AND IDORDONNANCE=?";
    
        String SelectEtat =
            "SELECT NVL(RESTE,0) AS RESTE FROM ETATSTOCK " +
            "WHERE IDMEDICAMENT=? AND IDMAGASIN=(" +
            "  SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?" +
            ")";
    
        String UpdateEtat =
            "UPDATE ETATSTOCK SET ENTREE = ENTREE + ?, RESTE = RESTE + ?, DATY = SYSDATE " +
            "WHERE IDMEDICAMENT=? AND IDMAGASIN=(" +
            "  SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?" +
            ")";
        Connection conn = null;
    
        try {
            conn = OracleConnection.getConnection();
            conn.setAutoCommit(false);
    
            double qteOrd = 0;
            try (PreparedStatement ps = conn.prepareStatement(SelectOrd)) {
                ps.setString(1, idMedicament);
                ps.setString(2, idOrdonnance);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    qteOrd = rs.getDouble("QUANTITE");
                } else {
                    JOptionPane.showMessageDialog(null, "Médicament introuvable dans l’ordonnance !");
                    return null;
                }
            }
    
            if (qteOrd < qteRetour) {
                JOptionPane.showMessageDialog(null, "Quantité retournée > quantité délivrée !");
                return null;
            }
    
            try (PreparedStatement ps = conn.prepareStatement(UpdateOrd)) {
                ps.setDouble(1, qteRetour);
                ps.setString(2, idMedicament);
                ps.setString(3, idOrdonnance);
                ps.executeUpdate();
            }
    
            // 3) Lire état stock
            double resteAvant = 0;
            try (PreparedStatement ps = conn.prepareStatement(SelectEtat)) {
                ps.setString(1, idMedicament);
                ps.setString(2, idOrdonnance);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) resteAvant = rs.getDouble("RESTE");
            }
    
            double resteApres = resteAvant + qteRetour;
    
            try (PreparedStatement ps = conn.prepareStatement(UpdateEtat)) {
                ps.setDouble(1, qteRetour);    
                ps.setDouble(2, qteRetour);     
                ps.setString(3, idMedicament);
                ps.setString(4, idOrdonnance);
                ps.executeUpdate();
            }
    
            conn.commit();
            return "OK";
    
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (Exception ex) {}
            System.out.println("Erreur : " + e.getMessage());
            return null;
        }
    }
    
    
}
