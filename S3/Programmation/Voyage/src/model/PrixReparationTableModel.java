package model;

import model.ReparerPrix;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class PrixReparationTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Position (km)",
        "Date réparation",
        "Prix (Ar)"
    };

    private List<ReparerPrix> data;

    public PrixReparationTableModel(List<ReparerPrix> data) {
        this.data = data;
    }

    public void setData(List<ReparerPrix> data) {
        this.data = data;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        ReparerPrix rp = data.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> rp.getPositionKm();
            case 1 -> rp.getDateReparation();
            case 2 -> rp.getPrix();
            default -> null;
        };
    }
}
