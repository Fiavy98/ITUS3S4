package ui;

import model.Employee;
import model.SalaryHistory;
import dao.EmployeeDAO;
import dao.SalaryHistoryDAO;
import dao.RubriqueDAO;
import dao.PayrollCalculator;
import javax.swing.border.TitledBorder;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class SalaryUpdatePanel extends JPanel {
    private JTextField empnoField;
    private JTextField newSalaryField;
    private JTextField prevEndDateField; 
    private JButton searchButton;
    private JButton updateButton;
    private JLabel currentInfoLabel;
    // Payslip form components
    private JComboBox<Employee> employeeCombo;
    private JTextField payslipSalaryField;
    private JTextField cnapsField;
    private JCheckBox cnapsIsPctChk;
    private JTextField bonusField;
    private JRadioButton modeFixeRadio;
    private JRadioButton modeCalculerRadio;
    private JButton insertPayslipButton;
    private JLabel payslipResultLabel;
    
    public SalaryUpdatePanel() {
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Initialisation des composants
        empnoField = new JTextField(15);
        newSalaryField = new JTextField(15);
    prevEndDateField = new JTextField(15);
        searchButton = new JButton("Rechercher");
        updateButton = new JButton("Mettre à jour le Salaire");
        currentInfoLabel = new JLabel(" ");
        
        // Positionnement des composants
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("ID Employé:"), gbc);
    add(currentInfoLabel, gbc);
        add(empnoField, gbc);
        gbc.gridx = 2;
        add(searchButton, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        add(currentInfoLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Nouveau Salaire:"), gbc);
        gbc.gridx = 1;
        add(newSalaryField, gbc);
    gbc.gridx = 0; gbc.gridy = 4;
    add(new JLabel("Date de fin précédente (yyyy-MM-dd):"), gbc);
    gbc.gridx = 1;
    add(prevEndDateField, gbc);
        gbc.gridx = 2;
        add(updateButton, gbc);
        
    // --- Payslip panel ---
    JPanel payslipPanel = new JPanel(new GridBagLayout());
    payslipPanel.setBorder(new TitledBorder("Insertion Bulletin de Paie"));
    GridBagConstraints pgbc = new GridBagConstraints();
    pgbc.insets = new Insets(4,4,4,4);
    pgbc.fill = GridBagConstraints.HORIZONTAL;

    employeeCombo = new JComboBox<>();
    payslipSalaryField = new JTextField(10);
    cnapsField = new JTextField(6);
    cnapsField.setText("1"); // default CNAPS percentage = 1%
    cnapsIsPctChk = new JCheckBox("%");
    cnapsIsPctChk.setSelected(true); // treat CNAPS as percentage by default
    bonusField = new JTextField(6);
    modeFixeRadio = new JRadioButton("Fixe");
    modeCalculerRadio = new JRadioButton("Calculer", true);
    ButtonGroup modeGroup = new ButtonGroup();
    modeGroup.add(modeFixeRadio);
    modeGroup.add(modeCalculerRadio);
    insertPayslipButton = new JButton("Insérer Bulletin");
    payslipResultLabel = new JLabel(" ");

    pgbc.gridx = 0; pgbc.gridy = 0;
    payslipPanel.add(new JLabel("Employé:"), pgbc);
    pgbc.gridx = 1; pgbc.gridy = 0; pgbc.gridwidth = 2;
    payslipPanel.add(employeeCombo, pgbc);

    pgbc.gridwidth = 1;
    pgbc.gridx = 0; pgbc.gridy = 1; payslipPanel.add(new JLabel("Salaire Brut:"), pgbc);
    pgbc.gridx = 1; pgbc.gridy = 1; payslipPanel.add(payslipSalaryField, pgbc);

    pgbc.gridx = 0; pgbc.gridy = 2; payslipPanel.add(new JLabel("CNAPS:"), pgbc);
    pgbc.gridx = 1; pgbc.gridy = 2; payslipPanel.add(cnapsField, pgbc);
    pgbc.gridx = 2; pgbc.gridy = 2; payslipPanel.add(cnapsIsPctChk, pgbc);

    // OSTI removed from rubrique/schema; UI does not capture OSTI.

    pgbc.gridx = 0; pgbc.gridy = 4; payslipPanel.add(new JLabel("Bonus:"), pgbc);
    pgbc.gridx = 1; pgbc.gridy = 4; payslipPanel.add(bonusField, pgbc);

    pgbc.gridx = 0; pgbc.gridy = 5; payslipPanel.add(new JLabel("Mode:"), pgbc);
    pgbc.gridx = 1; pgbc.gridy = 5; payslipPanel.add(modeFixeRadio, pgbc);
    pgbc.gridx = 2; pgbc.gridy = 5; payslipPanel.add(modeCalculerRadio, pgbc);

    pgbc.gridx = 1; pgbc.gridy = 6; payslipPanel.add(insertPayslipButton, pgbc);
    pgbc.gridx = 1; pgbc.gridy = 7; payslipPanel.add(payslipResultLabel, pgbc);

    gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 3;
    add(payslipPanel, gbc);
    gbc.gridwidth = 1;
        
        // Gestion des événements
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmployee();
            }
        });
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSalary();
            }
        });

        // load employees into combo box
        loadEmployees();

        // when selecting employee, fill salary field
        employeeCombo.addActionListener(e -> {
            Employee emp = (Employee) employeeCombo.getSelectedItem();
            if (emp != null) {
                payslipSalaryField.setText(String.valueOf(emp.getSal()));
            }
        });

        insertPayslipButton.addActionListener(ev -> insertPayslip());
    }

    private void loadEmployees() {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            java.util.List<Employee> list = dao.getAllEmployees();
            DefaultComboBoxModel<Employee> model = new DefaultComboBoxModel<>();
            for (Employee e : list) model.addElement(e);
            employeeCombo.setModel(model);
        } catch (Exception ex) {
            // ignore or show
            System.err.println("Erreur chargement employés: " + ex.getMessage());
        }
    }

    private void insertPayslip() {
        try {
            Employee emp = (Employee) employeeCombo.getSelectedItem();
            if (emp == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un employé", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int empno = emp.getEmpno();
            double salaireBrut = Double.parseDouble(payslipSalaryField.getText());
            double cnapsVal = Double.parseDouble(cnapsField.getText());
            boolean cnapsIsPct = cnapsIsPctChk.isSelected();
            double bonus = bonusField.getText().isEmpty() ? 0.0 : Double.parseDouble(bonusField.getText());
            String mode = modeFixeRadio.isSelected() ? "fixe" : "calculer";

            // Determine overtime hours for this employee for the current month (assumption: payslip is for current month)
            double totalOvertimeHours = 0.0;
            try {
                dao.OvertimeDAO otDao = new dao.OvertimeDAO();
                java.util.Calendar cal = java.util.Calendar.getInstance();
                int year = cal.get(java.util.Calendar.YEAR);
                int month = cal.get(java.util.Calendar.MONTH) + 1; // 1-based month
                java.util.List<model.OvertimeEntry> entries = otDao.getOvertimeForEmployee(empno, year, month);
                for (model.OvertimeEntry e : entries) totalOvertimeHours += e.getHours();
            } catch (Exception ex) {
                // If overtime lookup fails, default to 0 and continue
                System.err.println("Erreur lecture heures supp: " + ex.getMessage());
            }

            // Compute payslip including overtime (pass CNAPS raw value and whether it's a percentage)
            PayrollCalculator.PayrollResult res = PayrollCalculator.computePayslipWithOvertime(
                salaireBrut, cnapsVal, 0.0, cnapsIsPct, false, bonus, mode, totalOvertimeHours
            );

            // Persist via RubriqueDAO
            RubriqueDAO rubDao = new RubriqueDAO();
            boolean ok = rubDao.addPayslipAndHistory(empno, "Bulletin", null, salaireBrut, cnapsVal, bonus, "gain", mode, cnapsIsPct, totalOvertimeHours);

            if (ok) {
                payslipResultLabel.setText(String.format("Net: %.2f (CNAPS: %.2f, IRSA: %.2f)", res.netSalary, res.cnaps, res.irsa));
                JOptionPane.showMessageDialog(this, "Bulletin inséré. Salaire net: " + String.format("%.2f", res.netSalary), "Succès", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Erreur insertion bulletin", "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs numériques valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void searchEmployee() {
        try {
            int empno = Integer.parseInt(empnoField.getText());
            EmployeeDAO dao = new EmployeeDAO();
            Employee employee = dao.getEmployeeById(empno);
            
            if (employee != null) {
                currentInfoLabel.setText(String.format("Employé: %s - Poste: %s - Salaire actuel: %.2f", 
                    employee.getEname(), employee.getJob(), employee.getSal()));
                currentInfoLabel.setForeground(Color.BLUE);
            } else {
                currentInfoLabel.setText("Employé non trouvé");
                currentInfoLabel.setForeground(Color.RED);
            }
        } catch (Exception ex) {
            currentInfoLabel.setText("Erreur: " + ex.getMessage());
            currentInfoLabel.setForeground(Color.RED);
        }
    }
    
    private void updateSalary() {
        try {
            int empno = Integer.parseInt(empnoField.getText());
            double newSalary = Double.parseDouble(newSalaryField.getText());
            
            EmployeeDAO empDao = new EmployeeDAO();
            SalaryHistoryDAO histDao = new SalaryHistoryDAO();
            String prevEndStr = prevEndDateField.getText().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SalaryHistory newHistory = new SalaryHistory();
            newHistory.setEmpno(empno);
            newHistory.setSalary(newSalary);

            if (!prevEndStr.isEmpty()) {
                Date prevEndDate = sdf.parse(prevEndStr);
                histDao.updateCurrentSalaryEndDate(empno, prevEndDate);

                Calendar cal = Calendar.getInstance();
                cal.setTime(prevEndDate);
                cal.add(Calendar.DATE, 1);
                Date newStart = cal.getTime();
                newHistory.setStartDate(newStart);
                newHistory.setEndDate(null);

              
                histDao.addSalaryHistory(newHistory, false);
            } else {
                
                newHistory.setStartDate(new Date());
                newHistory.setEndDate(null);
                histDao.addSalaryHistory(newHistory); 
            }
            
        
            if (empDao.updateEmployeeSalary(empno, newSalary)) {
                JOptionPane.showMessageDialog(this, "Salaire mis à jour avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                searchEmployee(); 
                newSalaryField.setText("");
                prevEndDateField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du salaire", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}