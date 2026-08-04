package affichage;

import data.*;
import objet.*;
import acte.*;
import javax.swing.*;
import java.awt.*;

public class MonPanel extends JPanel {
    // Composants Ajoutout
    JComboBox<Faritany> lsFaritany;
    JComboBox<Faritra> lsFaritra;
    JComboBox<Distrika> lsDistrika;
    JComboBox<Bv> lsBV;
    JComboBox<Candidat> lsCandidat;
    JTextField vote;
    JButton bottAj;

    // Composants affichage
    JComboBox<Faritany> affFaritany;
    JComboBox<Faritra> affFaritra;
    JComboBox<Distrika> affDistrika;
    JComboBox<Bv> affBV;
    JButton botAff;

public MonPanel() {
    setLayout(new BorderLayout());
    JPanel resultat = new JPanel();
    JPanel AjVote = new JPanel(new GridLayout(18,5,9,9));
    JPanel affVote = new JPanel(new GridLayout(18,5,7,7));

    TablePanel table= new TablePanel();        
    resultat.add(new JLabel("Resultat au vote : "));
    resultat.add(table);

    // --- Déclaration des ComboBox
    lsFaritany = new JComboBox<>();
    lsFaritra = new JComboBox<>();
    lsDistrika = new JComboBox<>();
    lsBV = new JComboBox<>();
    lsCandidat = new JComboBox<>();
    vote = new JTextField();
    bottAj = new JButton("Ajouter");

    // --- Layout Ajout de vote
    AjVote.setBorder(BorderFactory.createTitledBorder("Ajoute de vote"));
    AjVote.add(new JLabel("Faritany : "));
    AjVote.add(lsFaritany);
    AjVote.add(new JLabel("Faritra : "));
    AjVote.add(lsFaritra);
    AjVote.add(new JLabel("Distrika : "));
    AjVote.add(lsDistrika);
    AjVote.add(new JLabel("Bureau de vote : "));
    AjVote.add(lsBV);
    AjVote.add(new JLabel("Candidat : "));
    AjVote.add(lsCandidat);
    AjVote.add(new JLabel("Nb Vote : "));
    AjVote.add(vote);
    AjVote.add(new JLabel()); // case vide
    AjVote.add(bottAj);

    Data data = new Data();
    for(Faritany f : data.LsFaritany()){
        lsFaritany.addItem(f);
    }

     Stocker stock=new Stocker();
   ActionList acte = new ActionList(lsFaritany,lsFaritra,lsDistrika,lsBV,lsCandidat);
   bottAj.addActionListener(new ActionInsert(lsFaritany,lsFaritra,lsDistrika,lsBV,lsCandidat,vote,bottAj,stock));
        // Affichage
        affFaritany = new JComboBox<>();
        affFaritra = new JComboBox<>();
        affDistrika = new JComboBox<>();
        affBV = new JComboBox<>();
        botAff = new JButton("Afficher");

        affVote.setBorder(BorderFactory.createTitledBorder("Afficher les votes"));
        affVote.add(new JLabel("Faritany : "));
        affVote.add(affFaritany);
        affVote.add(new JLabel("Faritra : "));
        affVote.add(affFaritra);
        affVote.add(new JLabel("Distrika : "));
        affVote.add(affDistrika);
        affVote.add(new JLabel("Bureau de vote : "));
        affVote.add(affBV);
        affVote.add(new JLabel());
        affVote.add(botAff);

        Data dataaff = new Data();
        
        for(Faritany f : dataaff.LsFaritany()){
            affFaritany.addItem(f);
        }
          ActionAff Aff = new ActionAff(affFaritany,affFaritra,affDistrika,affBV);
         botAff.addActionListener(new ActionAfficher(affFaritany,affFaritra,affDistrika,affBV,table));

        // Ajouter dans le panel principal
        add(resultat, BorderLayout.CENTER);
        add(AjVote, BorderLayout.WEST);
        add(affVote, BorderLayout.EAST);
    }
}
