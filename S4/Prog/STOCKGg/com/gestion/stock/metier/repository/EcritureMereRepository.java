package com.gestion.stock.metier.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.metier.entity.EcritureMere;
import com.gestion.stock.metier.entity.EcritureFils;

public class EcritureMereRepository {

    public Integer save(EcritureMere ecritureMere) throws SQLException {
        String sql = "INSERT INTO ecriture_mere (date_ecriture, libelle, journal, mouvement_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, ecritureMere.getDateEcriture());
            ps.setString(2, ecritureMere.getLibelle());
            ps.setString(3, ecritureMere.getJournal());
            if (ecritureMere.getMouvementId() != null) {
                ps.setInt(4, ecritureMere.getMouvementId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
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

    public Integer saveWithConnection(EcritureMere ecritureMere, Connection conn) throws SQLException {
        String sql = "INSERT INTO ecriture_mere (date_ecriture, libelle, journal, mouvement_id) VALUES (?, ?, ?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, ecritureMere.getDateEcriture());
            ps.setString(2, ecritureMere.getLibelle());
            ps.setString(3, ecritureMere.getJournal());
            if (ecritureMere.getMouvementId() != null) {
                ps.setInt(4, ecritureMere.getMouvementId());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return null;
    }

    public EcritureMere findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM ecriture_mere WHERE id = ?";
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

    public Optional<EcritureMere> findByMouvementId(Integer mouvementId) throws SQLException {
        String sql = "SELECT * FROM ecriture_mere WHERE mouvement_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mouvementId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<EcritureMere> findByDate(LocalDate date) throws SQLException {
        String sql = "SELECT * FROM ecriture_mere WHERE date_ecriture = ? ORDER BY id DESC";
        List<EcritureMere> ecritures = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ecritures.add(mapResultSet(rs));
                }
            }
        }
        return ecritures;
    }

    public List<EcritureMere> findAll() throws SQLException {
        String sql = "SELECT * FROM ecriture_mere ORDER BY date_ecriture DESC, id DESC";
        List<EcritureMere> ecritures = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ecritures.add(mapResultSet(rs));
                }
            }
        }
        return ecritures;
    }

    private EcritureMere mapResultSet(ResultSet rs) throws SQLException {
        EcritureMere em = new EcritureMere();
        em.setId(rs.getInt("id"));
        em.setDateEcriture(rs.getObject("date_ecriture", LocalDate.class));
        em.setLibelle(rs.getString("libelle"));
        em.setJournal(rs.getString("journal"));
        Object mouvId = rs.getObject("mouvement_id");
        if (mouvId != null) {
            em.setMouvementId((Integer) mouvId);
        }
        return em;
    }
}
