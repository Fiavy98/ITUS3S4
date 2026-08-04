package com.gestion.stock.metier.repository;

import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.repository.GenericRepository;
import com.gestion.stock.metier.entity.Categorie;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CategorieRepository extends GenericRepository<Categorie> {
	public CategorieRepository() {
		super(Categorie.class);
	}
    
	@Override
	protected Categorie mapResultSetToEntity(ResultSet rs) throws SQLException {
		Categorie c = new Categorie();
		c.setId(rs.getInt("id"));
		c.setModuleId(rs.getObject("module_id") != null ? rs.getInt("module_id") : null);
		c.setCode(rs.getString("code"));
		c.setLibelle(rs.getString("libelle"));
		c.setCategorieParentId(rs.getObject("categorie_parent_id") != null ? rs.getInt("categorie_parent_id") : null);
		c.setDescription(rs.getString("description"));
		c.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
		return c;
	}
    
	@Override
	protected void setInsertParameters(java.sql.PreparedStatement ps, Categorie entity) throws SQLException {
		throw new UnsupportedOperationException("Non utilisé avec GenericRepository");
	}
    
	@Override
	protected void setUpdateParameters(java.sql.PreparedStatement ps, Categorie entity) throws SQLException {
		throw new UnsupportedOperationException("Non utilisé avec GenericRepository");
	}
    
	@Override
	protected String getTableName() { return "categorie"; }
    
	@Override
	protected String getIdColumnName() { return "id"; }
    
	@Override
	protected List<ColumnDef> getColumns() {
		List<ColumnDef> cols = new ArrayList<>();
		cols.add(new ColumnDef("id", "id", "ID", Integer.class, false, false, false));
		cols.add(new ColumnDef("moduleId", "module_id", "Module", Integer.class, false, true, true));
		cols.add(new ColumnDef("code", "code", "Code", String.class, true, true, true));
		cols.add(new ColumnDef("libelle", "libelle", "Libellé", String.class, true, true, true));
		cols.add(new ColumnDef("categorieParentId", "categorie_parent_id", "Catégorie parent", Integer.class, false, true, true));
		cols.add(new ColumnDef("description", "description", "Description", String.class, false, true, true));
		cols.add(new ColumnDef("createdAt", "created_at", "Créé le", java.time.LocalDateTime.class, false, false, false));
		return cols;
	}
}
