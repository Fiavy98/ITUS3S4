package com.gestion.stock.metier.repository;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.MouvementLotSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MouvementLotSourceRepository extends GenericRepository<MouvementLotSource> {
	public MouvementLotSourceRepository() {
		super(MouvementLotSource.class);
	}
    
	@Override
	protected MouvementLotSource mapResultSetToEntity(ResultSet rs) throws SQLException {
		MouvementLotSource m = new MouvementLotSource();
		m.setId(rs.getInt("id"));
		m.setMouvementId(rs.getInt("mouvement_id"));
		m.setLotId(rs.getObject("lot_id") != null ? rs.getInt("lot_id") : null);
		m.setQuantitePrelevee(rs.getBigDecimal("quantite_prelevee"));
		m.setPrixUnitaireLot(rs.getBigDecimal("prix_unitaire_lot"));
		m.setSourceLotReference(rs.getString("source_lot_reference"));
		m.setSourceLotType(rs.getString("source_lot_type"));
		m.setOrdreConsommation(rs.getObject("ordre_consommation") != null ? rs.getInt("ordre_consommation") : null);
		m.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
		return m;
	}
    
	@Override
	protected void setInsertParameters(PreparedStatement ps, MouvementLotSource entity) throws SQLException {
		throw new UnsupportedOperationException("Non utilisé avec GenericRepository");
	}
    
	@Override
	protected void setUpdateParameters(PreparedStatement ps, MouvementLotSource entity) throws SQLException {
		throw new UnsupportedOperationException("Non utilisé avec GenericRepository");
	}
    
	@Override
	protected String getTableName() { return "mouvement_lot_source"; }
    
	@Override
	protected String getIdColumnName() { return "id"; }
    
	@Override
	protected List<ColumnDef> getColumns() {
		List<ColumnDef> cols = new ArrayList<>();
		cols.add(new ColumnDef("id", "id", "ID", Integer.class, false, false, false));
		cols.add(new ColumnDef("mouvementId", "mouvement_id", "Mouvement", Integer.class, true, true, true));
		cols.add(new ColumnDef("lotId", "lot_id", "Lot", Integer.class, false, true, true));
		cols.add(new ColumnDef("quantitePrelevee", "quantite_prelevee", "Qté prélevée", java.math.BigDecimal.class, true, true, true));
		cols.add(new ColumnDef("prixUnitaireLot", "prix_unitaire_lot", "PU lot", java.math.BigDecimal.class, true, true, true));
		cols.add(new ColumnDef("sourceLotReference", "source_lot_reference", "Réf source lot", String.class, false, true, true));
		cols.add(new ColumnDef("sourceLotType", "source_lot_type", "Type source lot", String.class, false, true, true));
		cols.add(new ColumnDef("ordreConsommation", "ordre_consommation", "Ordre", Integer.class, false, true, true));
		cols.add(new ColumnDef("createdAt", "created_at", "Créé le", java.time.LocalDateTime.class, false, false, false));
		return cols;
	}
    
	public MouvementLotSource insert(MouvementLotSource entity, Connection conn) throws SQLException {
		return super.insert(entity, conn);
	}
}
