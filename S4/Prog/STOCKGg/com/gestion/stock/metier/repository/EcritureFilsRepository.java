package com.gestion.stock.metier.repository;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.metier.entity.EcritureFils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EcritureFilsRepository {
    
    public Integer save(EcritureFils ecritureFils) throws SQLException {
        String sql = "INSERT INTO ecriture_fils (ecriture_mere_id, numero_compte, libelle, debit, credit, mouvement_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, ecritureFils.getEcritureMere().getId());
            ps.setString(2, ecritureFils.getNumeroCompte());
            ps.setString(3, ecritureFils.getLibelle());
            ps.setBigDecimal(4, ecritureFils.getDebit());
            ps.setBigDecimal(5, ecritureFils.getCredit());
            if (ecritureFils.getMouvementId() != null) {
                ps.setInt(6, ecritureFils.getMouvementId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    public Integer saveWithConnection(EcritureFils ecritureFils, Connection conn) throws SQLException {
        String sql = "INSERT INTO ecriture_fils (ecriture_mere_id, numero_compte, libelle, debit, credit, mouvement_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ecritureFils.getEcritureMere().getId());
            ps.setString(2, ecritureFils.getNumeroCompte());
            ps.setString(3, ecritureFils.getLibelle());
            ps.setBigDecimal(4, ecritureFils.getDebit());
            ps.setBigDecimal(5, ecritureFils.getCredit());
            if (ecritureFils.getMouvementId() != null) {
                ps.setInt(6, ecritureFils.getMouvementId());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    public EcritureFils findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM ecriture_fils WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }
        return null;
    }
    
    public List<EcritureFils> findByEcritureMereId(Integer ecritureMereId) throws SQLException {
        String sql = "SELECT * FROM ecriture_fils WHERE ecriture_mere_id = ? ORDER BY id";
        List<EcritureFils> result = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ecritureMereId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSet(rs));
                }
            }
        }
        return result;
    }
    
    public List<EcritureFils> findByEcritureMereIdWithConnection(Integer ecritureMereId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM ecriture_fils WHERE ecriture_mere_id = ? ORDER BY id";
        List<EcritureFils> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ecritureMereId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSet(rs));
                }
            }
        }
        return result;
    }
    
    public List<EcritureFils> findByMouvementId(Integer mouvementId) throws SQLException {
        String sql = "SELECT * FROM ecriture_fils WHERE mouvement_id = ? ORDER BY id";
        List<EcritureFils> result = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mouvementId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapResultSet(rs));
                }
            }
        }
        return result;
    }

    private EcritureFils mapResultSet(ResultSet rs) throws SQLException {
        EcritureFils ef = new EcritureFils();
        ef.setId(rs.getInt("id"));
        ef.setNumeroCompte(rs.getString("numero_compte"));
        ef.setLibelle(rs.getString("libelle"));
        ef.setDebit(rs.getBigDecimal("debit"));
        ef.setCredit(rs.getBigDecimal("credit"));
        Object mouvId = rs.getObject("mouvement_id");
        if (mouvId != null) {
            ef.setMouvementId((Integer) mouvId);
        }
        return ef;
    }
}
