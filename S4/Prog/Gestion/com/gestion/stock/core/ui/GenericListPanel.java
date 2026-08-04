package com.gestion.stock.core.ui;

import com.gestion.stock.core.entity.BaseEntity;
import com.gestion.stock.core.entity.ColumnDef;
import com.gestion.stock.core.service.GenericService;
import com.gestion.stock.exception.BusinessException;
import com.gestion.stock.util.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class GenericListPanel<T extends BaseEntity> extends JPanel {
	private final GenericService<T> service;
	private final List<ColumnDef> columns;
	private final Class<T> entityClass;
	private final Supplier<T> newEntitySupplier;
	private JTable table;
	private GenericTableModel<T> tableModel;
    
	public GenericListPanel(GenericService<T> service, List<ColumnDef> columns, Class<T> entityClass, Supplier<T> newEntitySupplier) {
		this.service = service;
		this.columns = columns;
		this.entityClass = entityClass;
		this.newEntitySupplier = newEntitySupplier;
		setLayout(new BorderLayout(5, 5));
		initToolbar();
		initTable();
		loadData();
	}
    
	private void initToolbar() {
		JToolBar toolBar = new JToolBar();
		JButton btnNew = new JButton("Nouveau");
		JButton btnEdit = new JButton("Modifier");
		JButton btnDelete = new JButton("Supprimer");
		JButton btnRefresh = new JButton("Rafraîchir");
        
		btnNew.addActionListener(e -> showForm(null));
		btnEdit.addActionListener(e -> {
			int row = table.getSelectedRow();
			if (row >= 0) showForm(tableModel.getEntityAt(row));
		});
		btnDelete.addActionListener(e -> deleteSelected());
		btnRefresh.addActionListener(e -> loadData());
        
		toolBar.add(btnNew);
		toolBar.add(btnEdit);
		toolBar.add(btnDelete);
		toolBar.add(btnRefresh);
		add(toolBar, BorderLayout.NORTH);
	}
    
	private void initTable() {
		tableModel = new GenericTableModel<>(List.of(), columns, entityClass);
		table = new JTable(tableModel);
		add(new JScrollPane(table), BorderLayout.CENTER);
	}
    
	private void loadData() {
		try {
			tableModel.setData(service.findAll());
		} catch (BusinessException e) {
			SwingUtils.showError(this, "Erreur chargement", e);
		}
	}
    
	private void showForm(T entity) {
		if (entity == null) {
			entity = newEntitySupplier.get();
		}
		GenericFormPanel<T> formPanel = new GenericFormPanel<>(entity, columns, entityClass);
		int result = JOptionPane.showConfirmDialog(this, formPanel, "Fiche", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION && formPanel.validateForm()) {
			formPanel.updateEntityFromForm();
			try {
				service.save(entity);
				loadData();
			} catch (BusinessException e) {
				SwingUtils.showError(this, "Erreur sauvegarde", e);
			}
		}
	}
    
	private void deleteSelected() {
		int row = table.getSelectedRow();
		if (row < 0) return;
		T entity = tableModel.getEntityAt(row);
		if (JOptionPane.showConfirmDialog(this, "Supprimer l'élément ?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			try {
				service.deleteById(entity.getId());
				loadData();
			} catch (BusinessException e) {
				SwingUtils.showError(this, "Erreur suppression", e);
			}
		}
	}
}
