package dao;

import models.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import db.ConnectionDB;

public class ProduitDAO{

    public List<Produit> getAll() throws Exception {

        List<Produit> liste = new ArrayList<>();
        
        String sql = "SELECT * FROM produits ORDER BY nom";
        
        Connection conn = ConnectionDB.getConnection();
        
        PreparedStatement ps = conn.prepareStatement(sql);
        
        ResultSet rs = ps.executeQuery();
        
        while(rs.next()){
        
            Produit p = new Produit();
        
            p.setId(rs.getInt("id"));
            p.setNom(rs.getString("nom"));
        
            p.setMethodeValuation(
                MethodeValuation.valueOf(
                    rs.getString("methode_valuation")
                )
            );
        
            p.setPrixVenteDefaut(
                rs.getDouble("prix_vente_defaut")
            );
        
            p.setUnite(rs.getString("unite"));
        
            liste.add(p);
        }
    
        rs.close();
        ps.close();
        conn.close();
    
        return liste;
    }

    public void insert(Produit p) throws Exception {
        String sql = """
            INSERT INTO produits
            (nom, methode_valuation, prix_vente_defaut, unite)
            VALUES (?, ?::methode_valuation_enum, ?, ?)
            """;

        Connection conn = ConnectionDB.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, p.getNom());
        ps.setString(2, p.getMethodeValuation().name());
        ps.setDouble(3, p.getPrixVenteDefaut());
        ps.setString(4, p.getUnite());

        ps.executeUpdate();

        ps.close();
        conn.close();
    }

    public Produit getPV(int id) throws Exception {
        String sql = "SELECT * FROM produits WHERE id = ?";
        Connection conn = ConnectionDB.getConnection();

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);

        ResultSet rs = ps.executeQuery();

        Produit p = null;

        if(rs.next()){

            p = new Produit();

            p.setId(rs.getInt("id"));
            p.setNom(rs.getString("nom"));

            p.setPrixVenteDefaut(
                rs.getDouble("prix_vente_defaut")
            );

            p.setUnite(rs.getString("unite"));

            p.setMethodeValuation(
                MethodeValuation.valueOf(
                    rs.getString("methode_valuation")
                )
            );
        }

        rs.close();
        ps.close();
        conn.close();

        return p;

    }
}