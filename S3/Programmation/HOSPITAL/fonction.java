import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

public static double getQteByProduitUnite(String idProduit, String idUnite) throws Exception {
        try(Connection con = Connexion.getConnection()) {
            return getQteByProduitUnite(idProduit , idUnite , con);
        } catch (Exception e){
            throw e;
        }
    }

    public static double getQteByProduitUnite(String idProduit , String idUnite , Connection con) throws Exception {
        double qte = 0.0;
        String sql = "SELECT QTE FROM AS_EQUIVALENCE WHERE IDPRODUIT = ? AND IDUNITE = ?";
        try{
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idProduit);
            ps.setString(2, idUnite);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                qte = rs.getDouble("QTE");
            }

            rs.close();
            ps.close();
        } catch (Exception e){
            throw e;
        }

        return qte;
    }

    public static double calcQte(double quantite , double equi){
        double retour = 0;
        int tmp = (int) quantite;
        retour = tmp * equi;
        if(quantite - tmp != 0){
            double temp = quantite - tmp;
            temp = equi * temp;
            tmp = (int) temp;
            retour += tmp;
            if(temp - tmp != 0){
                retour += 1;
            }
        }
        return retour;
    }
    public static double calPrix(double quantite , String idProduit, String unite) throws Exception{
        try(Connection con = Connexion.getConnection()){
            return calPrix(quantite, idProduit, unite ,con);
        }
    }


    public static double calPrix(double quantite , String idProduit, String unite, Connection con) throws Exception {
        String sql = "SELECT PU FROM AS_EQUIVALENCE WHERE IDPRODUIT = ? AND IDUNITE = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, idProduit);
        ps.setString(2, unite);

        ResultSet rs = ps.executeQuery();
        double pu = 0;

        if(rs.next()){
            pu = rs.getDouble("PU");
        }

        rs.close();
        ps.close();

        return quantite * pu;
    }



    public static Unite[] getUniteEquivalence(String idProduit) throws Exception{
        try (Connection con = Connexion.getConnection()) {
            return getUniteEquivalence(idProduit, con);
        } catch (SQLException e) {
            throw e;
        }
    }

    public static Unite[] getUniteEquivalence(String idProduit, Connection con) throws Exception{
        List<Unite> list = new ArrayList<>();
        String sql = "SELECT u.ID, u.VAL, u.DESCE " +
                     "FROM AS_UNITE u " +
                     "JOIN AS_EQUIVALENCE e ON u.ID = e.IDUNITE " +
                     "WHERE e.IDPRODUIT = ? " +
                     "ORDER BY e.ID";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idProduit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Unite(
                    rs.getString("ID"),
                    rs.getString("VAL"),
                    rs.getString("DESCE")
                ));
            }
        } catch (SQLException e) {
            throw e;
        }
        return list.toArray(new Unite[list.size()]);
    }