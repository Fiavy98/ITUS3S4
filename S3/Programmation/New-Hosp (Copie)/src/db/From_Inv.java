package db;

import java.sql.*;
import java.util.Vector;

import javax.swing.JOptionPane;

public class From_Inv {
    public static String insInv(java.util.Date dt,String idMangasin){
        String insert = "INSERT INTO INVENTAIRE(DATY,IDMAGASIN) VALUES(?,?)";

        String id=null;
    
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert,new String[]{"ID"})) {
    
                ps.setDate(1, new java.sql.Date(dt.getTime()));
                ps.setString(2, idMangasin);
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ inventaire insérée !");

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
    

    //INVENTAIRE_FILLE
    public static void insInvFile(String idInventaire,String idMedicaments,int quantite){
        String insert = "INSERT INTO INVENTAIREFILLE(IDINVENTAIRE,IDPRODUIT,QUANTITE) VALUES(?,?,?)";
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
    
                ps.setString(1, idInventaire);
                ps.setString(2, idMedicaments);
                ps.setInt(3, quantite);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ inventaire Fille insérée !");
                } else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }

    //QUANTITE
    public static void MoinsQuantite(int quantite, String idInventaire, String idMedicaments) {
        String select = "SELECT QUANTITE FROM INVENTAIREFILLE WHERE IDINVENTAIRE = ? AND IDPRODUIT = ?";
        String update = "UPDATE INVENTAIREFILLE SET QUANTITE = ? WHERE IDINVENTAIRE = ? AND IDPRODUIT = ?";
    
        try (Connection conn = OracleConnection.getConnection()) {
    
            int qteActuelle = 0;
    
            //QT before
            try (PreparedStatement ps = conn.prepareStatement(select)) {
                ps.setString(1, idInventaire);
                ps.setString(2, idMedicaments);
    
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    qteActuelle = rs.getInt(1);
                } else {
                    JOptionPane.showMessageDialog(null,
                        "❌ Produit introuvable dans l'inventaire !",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
    
            //If not  dispo
            if (qteActuelle <= 0) {
                JOptionPane.showMessageDialog(null,
                    "❌ Ce médicament n'est pas disponible !",
                    "Stock épuisé",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            int qteFinale = qteActuelle - quantite;
    
            if (qteFinale < 0) qteFinale = 0; 
    
            // mise a jr
            try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                ps2.setInt(1, qteFinale);
                ps2.setString(2, idInventaire);
                ps2.setString(3, idMedicaments);
    
                ps2.executeUpdate();
            }
    
            if (qteFinale == 0) {
                JOptionPane.showMessageDialog(null,
                    "⚠ Stock épuisé après cette opération.",
                    "Attention",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null,
                    "✔ Quantité modifiée ! (reste : " + qteFinale + ")",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "❌ Erreur SQL : " + e.getMessage(),
                "Erreur Base de données",
                JOptionPane.ERROR_MESSAGE);
        }
    }


    //View
        public static Vector<Vector<Object>> lsInv() throws SQLException {
        String sql = "SELECT * FROM INVENTAIREFILLELIB";
        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getString("IDINVENTAIRE"));
                row.add(rs.getString("IDPRODUITLIB"));
                row.add(rs.getInt("QUANTITETHEORIQUE"));
                row.add(rs.getInt("QUANTITE"));
                data.add(row);
            }
        }
    
        return data;
    }
    

    public static Vector<String> ColoneInv() {
        Vector<String> colNames = new Vector<>();
        colNames.add("ID");
        colNames.add("ID INVENTAIRE");
        colNames.add("PRODUITS");
        colNames.add("QUANTITETHEORIQUE");        
        colNames.add("QUANTITE");
        return colNames;
    }



}

