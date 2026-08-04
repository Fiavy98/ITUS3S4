package ui; 

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import dao.RouteDAO;
import ui.MonPanel;
import model.Route;
public class Choix extends JPanel {

    private JComboBox<Route> comboBox;
    private JButton btnVoirRoute;
    private MonPanel parentPanel;

    public Choix(MonPanel parent) {
        this.parentPanel = parent;

        setLayout(new FlowLayout());

        comboBox = new JComboBox<>();
        loadRoutes();

        btnVoirRoute = new JButton("Voir Route");
        btnVoirRoute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Route selectedRoute = (Route) comboBox.getSelectedItem();
                if (selectedRoute != null) {
                    int idRoute = selectedRoute.getId();
                    // Appel de MonPanel pour changer de page
                    parentPanel.showGooPage(idRoute);
                }
            }
        });

        add(new JLabel("Sélectionnez une route :"));
        add(comboBox);
        add(btnVoirRoute);
    }

    private void loadRoutes() {
        Vector<String[]> routesData = RouteDAO.lsRoute();
        comboBox.removeAllItems();

        for (String[] r : routesData) {
            int id = Integer.parseInt(r[0]);
            String nom = r[1];
            Route route = new Route();
            route.setId(id);
            route.setRn(nom);
            comboBox.addItem(route);
        }
    }
}
