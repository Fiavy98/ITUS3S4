package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

        String sqlSelectEtat = "SELECT NVL(RESTE,0) AS RESTE FROM ETATSTOCK " +
                               "WHERE IDMEDICAMENT=? AND IDMAGASIN=(SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?)";
    
        String sqlUpdateEtat = "UPDATE ETATSTOCK SET ENTREE = ENTREE + ?, SORTIE = SORTIE + ?, " +
                               "RESTE = RESTE + ? - ?, DATY = SYSDATE " +
                               "WHERE IDMEDICAMENT=? AND IDMAGASIN=(SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?)";
    
        String sqlInsertEtat = "INSERT INTO ETATSTOCK(IDMEDICAMENT, IDMAGASIN, ENTREE, SORTIE, RESTE, DATY) " +
                               "SELECT ?, (SELECT IDMAGASIN FROM MVTSTOCK WHERE ID=?), ?, ?, ?, SYSDATE FROM dual";
    
        String sqlInsertFille = "INSERT INTO MVTSTOCKFILLE(IDMVTSTOCK, IDPRODUIT, ENTREE, SORTIE, RESTE) " +
                                "VALUES (?, ?, ?, ?, ?)";
    
        try (Connection conn = OracleConnection.getConnection()) {
            // 1) Lire le reste global
            double resteAvant = 0;
            try (PreparedStatement ps = conn.prepareStatement(sqlSelectEtat)) {
                ps.setString(1, idProduit);
                ps.setString(2, idMvt);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) resteAvant = rs.getDouble("RESTE");
            }
    
            // 2) Nouveau reste
            double resteApres = resteAvant + entree - sortie;
            if (resteApres < 0) {
                System.out.println("❌ Stock insuffisant !");
                JOptionPane.showMessageDialog(null, "❌ Stock insuffisant !");
                conn.rollback();
                return null;
            }
    
            // 3) Mise à jour ETATSTOCK
            int updated;
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdateEtat)) {
                ps.setDouble(1, entree);
                ps.setDouble(2, sortie);
                ps.setDouble(3, entree);
                ps.setDouble(4, sortie);
                ps.setString(5, idProduit);
                ps.setString(6, idMvt);
                updated = ps.executeUpdate();
            }
    
            // 4) Si ETATSTOCK n’existe pas → créer
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
    
            // 5) Insérer mouvement fille (sans ID)
            try (PreparedStatement ps = conn.prepareStatement(sqlInsertFille)) {
                ps.setString(1, idMvt);
                ps.setString(2, idProduit);
                ps.setDouble(3, entree);
                ps.setDouble(4, sortie);
                ps.setDouble(5, resteApres);
                ps.executeUpdate();
            }
    
            System.out.println("✔ mouvement + mise à jour ETATSTOCK OK");
    
            return "OK"; // ou retourner rien
    
        } catch (Exception e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
            return null;
        }
    }
        
    
    
}
