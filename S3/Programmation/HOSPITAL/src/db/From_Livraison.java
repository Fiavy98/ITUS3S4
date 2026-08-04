package db;

import java.sql.*;
import java.util.Vector;

public class From_Livraison {
    //LIVRAISONINTERNE
    public static String insLivrInt(String idForn,java.util.Date dt){
        String insert = "INSERT INTO LIVRAISONINTERNE(IDFOURNISSEUR,DATY) VALUES(?,?)";

        String id=null;
    
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert,new String[]{"ID"})) {
    
                ps.setString(1, idForn);
                ps.setDate(2, new java.sql.Date(dt.getTime()));
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ livraison inserer!");
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


    //LIVRAISONINTERNEFILLE
    public static void insLivrIntFll(String idLivr,String idMedicaments,int quantite){
        String insert = "INSERT INTO LIVRAISONINTERNEFILLE(IDMERE,IDINGREDIENT,QTE) VALUES(?,?,?)";
        try (Connection conn = OracleConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(insert)) {
    
                ps.setString(1, idLivr);
                ps.setString(2, idMedicaments);
                ps.setInt(3, quantite);
    
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✔ livraison Fille insérée !");
                } else {
                    System.out.println("⚠ Aucun enregistrement inséré !");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }

    //Select Fourniseur
        public static Vector<String> lsFourniseur(){
        String sql="SELECT * FROM FOURNISSEUR";
        Vector<String> noms = new Vector<>();

        try(Connection conn=OracleConnection.getConnection()){
            PreparedStatement ps=conn.prepareStatement(sql);
            ResultSet rs=ps.executeQuery();

            while (rs.next()) {
                noms.add(rs.getString("NOM"));
            }

            
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }

        return noms;
    }

    public static String geIdForn(String form_nom) {
        String idFourn = null;
        String sql = "SELECT ID FROM FOURNISSEUR WHERE NOM = ?";
    
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, form_nom);    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    idFourn = rs.getString("ID"); 
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    
        return idFourn;  
    }

    //View
    public static Vector<Vector<Object>> lsLivraison() throws SQLException {
        String sql = "SELECT liv.ID,frn.NOM AS FORNIS,liv.DATY FROM LIVRAISONINTERNE liv JOIN FOURNISSEUR frn ON liv.IDFOURNISSEUR=frn.ID ORDER BY DATY";
    
        Vector<Vector<Object>> data = new Vector<>();
    
        try (Connection conn = OracleConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
    
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("ID"));
                row.add(rs.getString("FORNIS"));
                row.add(rs.getString("DATY"));
                data.add(row);
            }
        }
    
        return data;
    }
    

    public static Vector<String> ColoneLivraison() {
        Vector<String> colNames = new Vector<>();
        colNames.add("ID");
        colNames.add("FORNISEUR");
        colNames.add("DATY");
        return colNames;
    }


}
