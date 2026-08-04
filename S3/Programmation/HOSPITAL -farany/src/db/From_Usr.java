package db;

import java.sql.*;

public class From_Usr {
    public static void insUser(String nom,String prenom,String mdp,String poste){
        String insert="INSERT INTO Usr(name,prenom,mdp,id_poste) VALUES (?,?,?,?)";

        try (Connection conn=OracleConnection.getConnection()){
            try(PreparedStatement ps=conn.prepareStatement(insert)){
                ps.setString(1, nom);
                ps.setString(2, prenom);
                ps.setString(3, mdp);
                ps.setString(4, poste);
                
                int lignes = ps.executeUpdate();
    
                if (lignes > 0) {
                    System.out.println("✅ User insere!");
                } else {
                    System.out.println("⚠️ Aucun enregistrement insere !");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
        }
    }


    public static int verifyUser(String nom, String mdp) {
        String sql = "SELECT * FROM Usr WHERE name = ? AND mdp = ?";
        try (Connection conn = OracleConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, nom);
            ps.setString(2, mdp);
    
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✔️ Utilisateur trouvé !");
                    int id=rs.getInt("ID");
                    return id;
                } else {
                    System.out.println("❌ Utilisateur non trouvé.");
                    return -1;
                }
            }
    
        } catch (SQLException e) {
            System.out.println("❌ Erreur SQL : " + e.getMessage());
            return -1;
        }
    }
    

}

