package com.gestion.stock.metier.repository;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.Mouvement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MouvementRepository extends GenericRepository<Mouvement> {
	public MouvementRepository() {
		super(Mouvement.class);
	}
    
	@Override
	protected Mouvement mapResultSetToEntity(ResultSet rs) throws SQLException {
		Mouvement m = new Mouvement();
		m.setId(rs.getInt("id"));
		m.setArticleId(rs.getInt("article_id"));
		m.setDateMouvement(rs.getDate("date_mouvement").toLocalDate());
		m.setTypeMouvement(rs.getString("type_mouvement"));
		m.setQuantite(rs.getBigDecimal("quantite"));
		m.setPrixUnitaireCalcule(rs.getBigDecimal("prix_unitaire_calcule"));
		m.setValeurMouvement(rs.getBigDecimal("valeur_mouvement"));
		m.setStockQteApres(rs.getBigDecimal("stock_qte_apres"));
		m.setValeurStockApres(rs.getBigDecimal("valeur_stock_apres"));
		m.setCumpApres(rs.getBigDecimal("cump_apres"));
		m.setMethodeValorisation(rs.getString("methode_valorisation"));
		m.setSourceReference(rs.getString("source_reference"));
		m.setSourceType(rs.getString("source_type"));
		m.setSourceTiers(rs.getString("source_tiers"));
		m.setMotif(rs.getString("motif"));
		m.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
		return m;
	}
    
	@Override
	protected void setInsertParameters(PreparedStatement ps, Mouvement entity) throws SQLException {
		throw new UnsupportedOperationException("Non utilisé avec GenericRepository");
	}
    
	@Override
	protected void setUpdateParameters(PreparedStatement ps, Mouvement entity) throws SQLException {
		throw new UnsupportedOperationException("Non utilisé avec GenericRepository");
	}
    
	@Override
	protected String getTableName() { return "mouvement"; }
    
	@Override
	protected String getIdColumnName() { return "id"; }
    
	@Override
	protected List<ColumnDef> getColumns() {
		List<ColumnDef> cols = new ArrayList<>();
		cols.add(new ColumnDef("id", "id", "ID", Integer.class, false, false, false));
		cols.add(new ColumnDef("articleId", "article_id", "Article", Integer.class, true, true, true));
		cols.add(new ColumnDef("dateMouvement", "date_mouvement", "Date", LocalDate.class, true, true, true));
		cols.add(new ColumnDef("typeMouvement", "type_mouvement", "Type", String.class, true, true, true));
		cols.add(new ColumnDef("quantite", "quantite", "Quantité", java.math.BigDecimal.class, true, true, true));
		cols.add(new ColumnDef("prixUnitaireCalcule", "prix_unitaire_calcule", "PU", java.math.BigDecimal.class, false, true, true));
		cols.add(new ColumnDef("valeurMouvement", "valeur_mouvement", "Valeur", java.math.BigDecimal.class, false, true, true));
		cols.add(new ColumnDef("stockQteApres", "stock_qte_apres", "Stock après", java.math.BigDecimal.class, false, true, true));
		cols.add(new ColumnDef("valeurStockApres", "valeur_stock_apres", "Valeur stock", java.math.BigDecimal.class, false, true, true));
		cols.add(new ColumnDef("cumpApres", "cump_apres", "CUMP", java.math.BigDecimal.class, false, true, true));
		cols.add(new ColumnDef("methodeValorisation", "methode_valorisation", "Méthode", String.class, false, true, true));
		cols.add(new ColumnDef("sourceReference", "source_reference", "Référence", String.class, false, true, true));
		cols.add(new ColumnDef("sourceType", "source_type", "Type source", String.class, false, true, true));
		cols.add(new ColumnDef("sourceTiers", "source_tiers", "Tiers", String.class, false, true, true));
		cols.add(new ColumnDef("motif", "motif", "Motif", String.class, false, true, true));
		cols.add(new ColumnDef("createdAt", "created_at", "Créé le", java.time.LocalDateTime.class, false, false, false));
		return cols;
	}
    
	public Mouvement insert(Mouvement entity, Connection conn) throws SQLException {
		return super.insert(entity, conn);
	}
}
