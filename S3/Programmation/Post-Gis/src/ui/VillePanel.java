package ui;

import dao.VilleDAO;
import model.Ville;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class VillePanel extends JPanel {

    public VillePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        String[] cols = {"Ville", "Population", "Longitude", "Latitude"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);
        JTable table = new JTable(model);

        try {
            VilleDAO dao = new VilleDAO();
            List<Ville> villes = dao.getAllVilles();

            for (Ville v : villes) {
                model.addRow(new Object[]{
                    v.getNom(),
                    v.getPopulation(),
                    v.getLongitude(),
                    v.getLatitude()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        add(new JScrollPane(table));
    }
}
