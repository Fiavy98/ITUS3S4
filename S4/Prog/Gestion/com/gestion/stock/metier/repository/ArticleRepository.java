package com.gestion.stock.metier.repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.Article;

public class ArticleRepository extends GenericRepository<Article> {

    public ArticleRepository() {
        super(Article.class);
    }

    @Override
    protected Article mapResultSetToEntity(ResultSet rs) throws SQLException {
        Article article = new Article();
        article.setId(rs.getInt("id"));
        article.setCategorieId(rs.getObject("categorie_id") != null ? rs.getInt("categorie_id") : null);
        article.setCode(rs.getString("code"));
        article.setUniteMesure(rs.getString("unite_mesure"));
        article.setMethodeGestion(rs.getString("methode_gestion"));
        article.setStockActuel(rs.getBigDecimal("stock_actuel"));
        article.setValeurStockActuelle(rs.getBigDecimal("valeur_stock_actuelle"));
        article.setCumpActuel(rs.getBigDecimal("cump_actuel"));
        article.setStockReserve(rs.getBigDecimal("stock_reserve"));
        article.setActif(rs.getBoolean("actif"));
        article.setCategorieLibelle(rs.getString("categorie_libelle"));
        article.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        article.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        return article;
    }

    @Override
    protected void setInsertParameters(PreparedStatement ps, Article entity) throws SQLException {
        int index = 1;
        if (entity.getCategorieId() != null) {
            ps.setInt(index++, entity.getCategorieId());
        } else {
            ps.setNull(index++, Types.INTEGER);
        }
        ps.setString(index++, entity.getCode());
        ps.setString(index++, entity.getUniteMesure());
        ps.setString(index++, entity.getMethodeGestion());
        ps.setBigDecimal(index++, entity.getStockActuel());
        ps.setBigDecimal(index++, entity.getValeurStockActuelle());
        ps.setBigDecimal(index++, entity.getCumpActuel());
        ps.setBigDecimal(index++, entity.getStockReserve());
        ps.setBoolean(index++, entity.isActif());
    }

    @Override
    protected void setUpdateParameters(PreparedStatement ps, Article entity) throws SQLException {
        setInsertParameters(ps, entity);
        ps.setInt(10, entity.getId());
    }

    @Override
    protected String getTableName() {
        return "article";
    }

    @Override
    protected String getIdColumnName() {
        return "id";
    }

    @Override
    protected List<ColumnDef> getColumns() {
        List<ColumnDef> cols = new ArrayList<>();
        cols.add(new ColumnDef("id", "id", "ID", Integer.class, false, false, false));
        cols.add(new ColumnDef("categorieId", "categorie_id", "Catégorie", Integer.class, false, true, false));
        cols.add(new ColumnDef("categorieLibelle", "categorie_libelle", "Catégorie", String.class, false, false, true));
        cols.add(new ColumnDef("code", "code", "Code", String.class, true, true, true));
        cols.add(new ColumnDef("uniteMesure", "unite_mesure", "Unité", String.class, true, true, true));
        cols.add(new ColumnDef("methodeGestion", "methode_gestion", "Méthode", String.class, true, true, true));
        cols.add(new ColumnDef("stockReserve", "stock_reserve", "Stock réserve", BigDecimal.class, false, false, false));
        cols.add(new ColumnDef("actif", "actif", "Actif", Boolean.class, false, true, true));
        cols.add(new ColumnDef("stockActuel", "stock_actuel", "Stock actuel", BigDecimal.class, false, false, false));
        cols.add(new ColumnDef("valeurStockActuelle", "valeur_stock_actuelle", "Valeur stock", BigDecimal.class, false, false, false));
        cols.add(new ColumnDef("cumpActuel", "cump_actuel", "CUMP", BigDecimal.class, false, false, false));
        cols.add(new ColumnDef("createdAt", "created_at", "Créé le", LocalDateTime.class, false, false, false));
        cols.add(new ColumnDef("updatedAt", "updated_at", "Modifié le", LocalDateTime.class, false, false, false));
        return cols;
    }

    public List<ColumnDef> getColumnDefinitions() {
        return getColumns();
    }

    @Override
    public List<Article> findAll(Connection conn) throws SQLException {
        List<Article> list = new ArrayList<>();
        String sql = "SELECT a.*, c.libelle AS categorie_libelle "
                + "FROM article a "
                + "LEFT JOIN categorie c ON a.categorie_id = c.id "
                + "ORDER BY a.code";
        try (java.sql.Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToEntity(rs));
            }
        }
        return list;
    }

    @Override
    public Optional<Article> findById(int id, Connection conn) throws SQLException {
        String sql = "SELECT a.*, c.libelle AS categorie_libelle "
                + "FROM article a "
                + "LEFT JOIN categorie c ON a.categorie_id = c.id "
                + "WHERE a.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToEntity(rs));
                }
            }
        }
        return Optional.empty();
    }

    // Méthode spécifique pour mettre à jour le stock après mouvement
    public void updateStockAndCump(int articleId, BigDecimal newStock, BigDecimal newValue, BigDecimal newCump, Connection conn) throws SQLException {
        String sql = "UPDATE article SET stock_actuel = ?, valeur_stock_actuelle = ?, cump_actuel = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, newStock);
            ps.setBigDecimal(2, newValue);
            ps.setBigDecimal(3, newCump);
            ps.setInt(4, articleId);
            ps.executeUpdate();
        }
    }
}
