package ui;
import javax.swing.*;
import java.awt.*;

public class MonPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel panelContainer; // déclarée ici

    public MonPanel() {
        // 1️⃣ Initialisation obligatoire avant d'utiliser panelContainer
        cardLayout = new CardLayout();
        panelContainer = new JPanel(cardLayout); // <-- NE PAS OUBLIER

        // 2️⃣ Créer les panels/pages
        Choix choixPanel = new Choix(this);
        Demarrer detailsPanel = new Demarrer();

        // 3️⃣ Ajouter les panels au panelContainer
        panelContainer.add(choixPanel, "CHOIX");
        panelContainer.add(detailsPanel, "DETAILS");

        // 4️⃣ Ajouter le panelContainer à MonPanel
        setLayout(new BorderLayout());
        add(panelContainer, BorderLayout.CENTER);

        // 5️⃣ Afficher la page par défaut
        cardLayout.show(panelContainer, "CHOIX");
    }

    public void showGooPage(int idRoute) {
        Demarrer detailsPanel = (Demarrer) panelContainer.getComponent(1);
        detailsPanel.updateDetails(idRoute);
        cardLayout.show(panelContainer, "DETAILS");
    }

    
}