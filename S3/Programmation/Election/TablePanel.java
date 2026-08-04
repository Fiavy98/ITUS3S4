package affichage;
import objet.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel {
    JTable resultat;
    DefaultTableModel model;
    JScrollPane table;
    java.util.List<Object[]> votesBuffer = new java.util.ArrayList<>();

    public TablePanel(){
    //    setLayout(new BorderLayout());
        JPanel result = new JPanel();

       String[] colonne={"Numero","Candidat","Faritany","Faritra","Distrika","BV","Vote"};
       model = new DefaultTableModel(colonne,0);
       resultat = new JTable(model);
       table = new JScrollPane(resultat);

       result.add(table);

       add(result);
        
    }

public void AjoutVote(int num, String c,Faritany faritany, Faritra faritra,Distrika d, Bv b, Vote vote){
    boolean trouve = false;
     
    for(int i=0; i < model.getRowCount(); i++){
        int numExiste = (int) model.getValueAt(i, 0); //collonne 0 = cuve
        String CandidatExiste=(String) model.getValueAt(i,1);

        if(numExiste.equals(num) && CandidatExiste.equals(c)){
            int new_vote=(int) model.getValueAt(i,6) + vote;
            model.setValueAt(new_vote,i, 6);

            trouve=true;
            break;
        }
    }

    if(!trouve){
        model.addRow(new Object[]{num,c,faritany,faritra,d,b,vote});
    }

   }

   public void afficherVotes(Faritany faritany, Faritra faritra,Distrika d, Bv b) {

    // vider model model.setRowCount(0);
    model.setRowCount(0);

    for(Object[] row : votesBuffer) {
        Faritany affFaritany = (Faritany) row[2];
        Faritra affFaritra =  (Faritra) row[3];
        Distrika affDistrika = (Distrika) row[4];
        Bv affBv = (Bv) row[5];
         
        if(affFaritany.equals(faritany) && affFaritra.equals(faritra) && affDistrika.equals(d) && affBv.equals(b)){
            model.addRow(row);
        }

    }
    
}2021-12-02

}   
