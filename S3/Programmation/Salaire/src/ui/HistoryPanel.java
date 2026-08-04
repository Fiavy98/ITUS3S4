package ui;

import model.SalaryHistory;
import model.Employee;
import dao.SalaryHistoryDAO;
import dao.EmployeeDAO;
import dao.PayrollCalculator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class HistoryPanel extends JPanel {
    private JTextField dateField;
    private JButton filterButton;
    private JButton showAllButton;
    private JTable historyTable;
    private DefaultTableModel tableModel;
    
    public HistoryPanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        
        // Panel de filtrage
        JPanel filterPanel = new JPanel(new FlowLayout());
        filterPanel.add(new JLabel("Date de filtrage (yyyy-MM-dd):"));
        
        dateField = new JTextField(10);
        dateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        filterPanel.add(dateField);
        
        filterButton = new JButton("Filtrer par Date");
        filterPanel.add(filterButton);
        
        showAllButton = new JButton("Afficher Historique Complet");
        filterPanel.add(showAllButton);
        
        add(filterPanel, BorderLayout.NORTH);
        
    String[] columnNames = {"ID Employé", "Nom", "Salaire", "Salaire Net", "Date Début", "Date Fin"};
        tableModel = new DefaultTableModel(columnNames, 0);
        historyTable = new JTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(historyTable);
        add(scrollPane, BorderLayout.CENTER);
        
       
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterHistoryByDate();
            }
        });
        
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showCompleteHistory();
            }
        });
        
        filterHistoryByDate();
    }
    
    private void filterHistoryByDate() {
        try {
            Date filterDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
            SalaryHistoryDAO dao = new SalaryHistoryDAO();
            List<SalaryHistory> history = dao.getSalaryHistoryByDate(filterDate);
            
            updateTable(history, false);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showCompleteHistory() {
        try {
            // Pour l'historique complet, on peut utiliser une date très éloignée dans le futur
            Date futureDate = new SimpleDateFormat("yyyy-MM-dd").parse("2099-12-31");
            SalaryHistoryDAO dao = new SalaryHistoryDAO();
            List<SalaryHistory> history = dao.getSalaryHistoryByDate(futureDate);
            
            updateTable(history, true);
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTable(List<SalaryHistory> history, boolean showAll) {
        // Vider le tableau
        tableModel.setRowCount(0);
        
        // Remplir avec les nouvelles données
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (!showAll) {
                // Only show rows present in the provided history list
                for (SalaryHistory sh : history) {
                    double salary = sh.getSalary();
                    double net = sh.getNetSalary() > 0.0 ? sh.getNetSalary() : PayrollCalculator.computePayslip(salary, 1.0, 0.0, true, false, 0.0, "calculer").netSalary;
                    Object[] row = {
                        sh.getEmpno(),
                        sh.getEname(),
                        String.format("%.2f", salary),
                        String.format("%.2f", net),
                        sh.getStartDate() != null ? dateFormat.format(sh.getStartDate()) : "-",
                        sh.getEndDate() != null ? dateFormat.format(sh.getEndDate()) : "En cours"
                    };
                    tableModel.addRow(row);
                }
            } else {
                // Show all employees (existing behavior): include computed net for those without history
                // Map history by empno for quick lookup
                java.util.Map<Integer, SalaryHistory> map = new java.util.HashMap<>();
                for (SalaryHistory sh : history) map.put(sh.getEmpno(), sh);

                EmployeeDAO empDao = new EmployeeDAO();
                java.util.List<Employee> all = empDao.getAllEmployees();

                for (Employee e : all) {
                    SalaryHistory sh = map.get(e.getEmpno());
                    double salary = (sh != null) ? sh.getSalary() : e.getSal();

                    double net = 0.0;
                    if (sh != null && sh.getNetSalary() > 0.0) {
                        net = sh.getNetSalary();
                    } else {
                        PayrollCalculator.PayrollResult res = PayrollCalculator.computePayslip(salary, 1.0, 0.0, true, false, 0.0, "calculer");
                        net = res.netSalary;
                    }

                    Object[] row = {
                        e.getEmpno(),
                        e.getEname(),
                        String.format("%.2f", salary),
                        String.format("%.2f", net),
                        (sh != null && sh.getStartDate() != null) ? dateFormat.format(sh.getStartDate()) : "-",
                        (sh != null && sh.getEndDate() != null) ? dateFormat.format(sh.getEndDate()) : (sh != null ? "En cours" : "-")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur mise à jour tableau: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}