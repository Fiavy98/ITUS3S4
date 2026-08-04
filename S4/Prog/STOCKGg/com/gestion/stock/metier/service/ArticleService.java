package com.gestion.stock.metier.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.gestion.stock.config.DatabaseConfig;
import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.service.GenericService;
import com.gestion.stock.exception.BusinessException;
import com.gestion.stock.metier.entity.Article;
import com.gestion.stock.metier.repository.ArticleRepository;

public class ArticleService extends GenericService<Article> {
    private final ArticleRepository articleRepository;
    
    public ArticleService() {
        super(new ArticleRepository());
        this.articleRepository = (ArticleRepository) this.repository;
    }
    
    public List<ColumnDef> getColumnDefinitions() {
        return articleRepository.getColumnDefinitions();
    }

    @Override
    public void deleteById(int id) throws BusinessException {
        Connection conn = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM mouvement WHERE article_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM lot WHERE article_id = ?")) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            articleRepository.deleteById(id, conn);
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new BusinessException("Erreur lors de la suppression", e);
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }
    
    @Override
    protected void validateBeforeSave(Article entity) throws BusinessException {
        if (entity.getCode() != null) {
            entity.setCode(entity.getCode().trim());
        }
        if (entity.getUniteMesure() != null) {
            entity.setUniteMesure(entity.getUniteMesure().trim());
        }
        if (entity.getCode() == null || entity.getCode().trim().isEmpty()) {
            throw new BusinessException("Le code est obligatoire");
        }
        if (entity.getUniteMesure() == null || entity.getUniteMesure().trim().isEmpty()) {
            entity.setUniteMesure("unité");
        }
        String methode = entity.getMethodeGestion() != null
                ? entity.getMethodeGestion().trim().toUpperCase()
                : "CUMP";
        if (!methode.equals("FIFO") && !methode.equals("LIFO") && !methode.equals("CUMP")) {
            throw new BusinessException("Méthode de gestion invalide: " + methode);
        }
        entity.setMethodeGestion(methode);
    }
}
