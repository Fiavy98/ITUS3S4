package com.gestion.stock.metier.repository;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.Lot;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LotRepository extends GenericRepository<Lot> {
    
    public LotRepository() {
        super(Lot.class);
    }
    
    @Override
    protected Lot mapResultSetToEntity(ResultSet rs) throws SQLException {
        Lot l = new Lot();
        l.setId(rs.getInt("id"));
        l.setArticleId(rs.getInt("article_id"));
        l.setMouvementEntreeId(rs.getObject("mouvement_entree_id") != null ? rs.getInt("mouvement_entree_id") : null);
        l.setDateEntree(rs.getDate("date_entree").toLocalDate());
        l.setQuantiteInitiale(rs.getBigDecimal("quantite_initiale"));
        l.setQuantiteRestante(rs.getBigDecimal("quantite_restante"));
        l.setPrixUnitaire(rs.getBigDecimal("prix_unitaire"));
        l.setEpuise(rs.getBoolean("epuise"));
        l.setSourceReference(rs.getString("source_reference"));
        l.setSourceType(rs.getString("source_type"));
        l.setSourceDocument(rs.getString("source_document"));
        l.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        return l;
    }
    
    @Override
    protected void setInsertParameters(PreparedStatement ps, Lot entity) throws SQLException {
        int idx = 1;
        ps.setInt(idx++, entity.getArticleId());
        if (entity.getMouvementEntreeId() != null) ps.setInt(idx++, entity.getMouvementEntreeId());
        else ps.setNull(idx++, Types.INTEGER);
        ps.setDate(idx++, Date.valueOf(entity.getDateEntree()));
        ps.setBigDecimal(idx++, entity.getQuantiteInitiale());
        ps.setBigDecimal(idx++, entity.getQuantiteRestante());
        ps.setBigDecimal(idx++, entity.getPrixUnitaire());
        ps.setBoolean(idx++, entity.isEpuise());
        ps.setString(idx++, entity.getSourceReference());
        ps.setString(idx++, entity.getSourceType());
        ps.setString(idx++, entity.getSourceDocument());
    }
    
    @Override
    protected void setUpdateParameters(PreparedStatement ps, Lot entity) throws SQLException {
        int idx = 1;
        ps.setInt(idx++, entity.getArticleId());
        if (entity.getMouvementEntreeId() != null) ps.setInt(idx++, entity.getMouvementEntreeId());
        else ps.setNull(idx++, Types.INTEGER);
        ps.setDate(idx++, Date.valueOf(entity.getDateEntree()));
        ps.setBigDecimal(idx++, entity.getQuantiteInitiale());
        ps.setBigDecimal(idx++, entity.getQuantiteRestante());
        ps.setBigDecimal(idx++, entity.getPrixUnitaire());
        ps.setBoolean(idx++, entity.isEpuise());
        ps.setString(idx++, entity.getSourceReference());
        ps.setString(idx++, entity.getSourceType());
        ps.setString(idx++, entity.getSourceDocument());
        ps.setInt(idx++, entity.getId());
    }
    
    @Override
    protected String getTableName() { return "lot"; }
    @Override
    protected String getIdColumnName() { return "id"; }
    
    @Override
    protected List<ColumnDef> getColumns() {
        List<ColumnDef> cols = new ArrayList<>();
        cols.add(new ColumnDef("id", "id", "ID", Integer.class, false, false, false));
        cols.add(new ColumnDef("articleId", "article_id", "Article", Integer.class, true, true, true));
        cols.add(new ColumnDef("mouvementEntreeId", "mouvement_entree_id", "Mouvement entrée", Integer.class, false, true, false));
        cols.add(new ColumnDef("dateEntree", "date_entree", "Date entrée", LocalDate.class, true, true, true));
        cols.add(new ColumnDef("quantiteInitiale", "quantite_initiale", "Qté initiale", BigDecimal.class, true, true, true));
        cols.add(new ColumnDef("quantiteRestante", "quantite_restante", "Qté restante", BigDecimal.class, true, true, true));
        cols.add(new ColumnDef("prixUnitaire", "prix_unitaire", "PU", BigDecimal.class, true, true, true));
        cols.add(new ColumnDef("sourceReference", "source_reference", "Réf source", String.class, false, true, true));
        cols.add(new ColumnDef("sourceType", "source_type", "Type source", String.class, false, true, false));
        cols.add(new ColumnDef("sourceDocument", "source_document", "Document source", String.class, false, true, false));
        cols.add(new ColumnDef("createdAt", "created_at", "Créé le", java.time.LocalDateTime.class, false, false, false));
        return cols;
    }
    
    // Récupérer les lots actifs d'un article pour FIFO/LIFO
    public List<Lot> findActiveLotsByArticle(int articleId, String order) throws SQLException {
        try (Connection conn = getConnection()) {
            return findActiveLotsByArticle(articleId, order, conn);
        }
    }
    
    public List<Lot> findActiveLotsByArticle(int articleId, String order, Connection conn) throws SQLException {
        String sql = "SELECT * FROM lot WHERE article_id = ? AND epuise = false ORDER BY " + order;
        List<Lot> lots = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, articleId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lots.add(mapResultSetToEntity(rs));
                }
            }
        }
        return lots;
    }
    
    public void updateQuantiteRestante(int lotId, BigDecimal newQuantiteRestante, boolean epuise, Connection conn) throws SQLException {
        String sql = "UPDATE lot SET quantite_restante = ?, epuise = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newQuantiteRestante);
            ps.setBoolean(2, epuise);
            ps.setInt(3, lotId);
            ps.executeUpdate();
        }
    }
    
    private Connection getConnection() throws SQLException {
        return com.gestion.stock.config.DatabaseConfig.getConnection();
    }
}