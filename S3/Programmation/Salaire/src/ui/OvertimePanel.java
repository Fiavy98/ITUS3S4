package ui;

import dao.EmployeeDAO;
import dao.OvertimeDAO;
import model.Employee;
import model.OvertimeEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class OvertimePanel extends JPanel {
    private JComboBox<Employee> employeeCombo;
    private JComboBox<String> periodTypeCombo;
    private JComboBox<String> weekStartCombo;
    private JComboBox<Integer> monthCombo;
    private JComboBox<Integer> yearCombo;
    private JTextField hoursField;
    private JButton saveButton;

    public OvertimePanel() {
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        employeeCombo = new JComboBox<>();
        periodTypeCombo = new JComboBox<>(new String[]{"WEEKLY","MONTHLY","YEARLY"});
        weekStartCombo = new JComboBox<>();
        monthCombo = new JComboBox<>();
        yearCombo = new JComboBox<>();
        hoursField = new JTextField(8);
        saveButton = new JButton("Enregistrer Heures Sup");

        // Fill months and years
        for (int m=1; m<=12; m++) monthCombo.addItem(m);
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int y=thisYear-2; y<=thisYear+2; y++) yearCombo.addItem(y);

        gbc.gridx=0; gbc.gridy=0; add(new JLabel("Employé:"), gbc);
        gbc.gridx=1; gbc.gridy=0; add(employeeCombo, gbc);

        gbc.gridx=0; gbc.gridy=1; add(new JLabel("Type Période:"), gbc);
        gbc.gridx=1; gbc.gridy=1; add(periodTypeCombo, gbc);

        gbc.gridx=0; gbc.gridy=2; add(new JLabel("Semaine (début):"), gbc);
        gbc.gridx=1; gbc.gridy=2; add(weekStartCombo, gbc);

        gbc.gridx=0; gbc.gridy=3; add(new JLabel("Mois:"), gbc);
        gbc.gridx=1; gbc.gridy=3; add(monthCombo, gbc);

        gbc.gridx=0; gbc.gridy=4; add(new JLabel("Année:"), gbc);
        gbc.gridx=1; gbc.gridy=4; add(yearCombo, gbc);

        gbc.gridx=0; gbc.gridy=5; add(new JLabel("Heures:"), gbc);
        gbc.gridx=1; gbc.gridy=5; add(hoursField, gbc);

        gbc.gridx=1; gbc.gridy=6; add(saveButton, gbc);

        // Load employees
        loadEmployees();

        // When month/year changes, update week starts
        monthCombo.addActionListener(e -> refreshWeekStarts());
        yearCombo.addActionListener(e -> refreshWeekStarts());
        periodTypeCombo.addActionListener(e -> updateVisibility());

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveOvertime();
            }
        });

        updateVisibility();
        refreshWeekStarts();
    }

    private void updateVisibility() {
        String t = (String) periodTypeCombo.getSelectedItem();
        boolean weekly = "WEEKLY".equals(t);
        weekStartCombo.setEnabled(weekly);
        monthCombo.setEnabled(!"YEARLY".equals(t));
    }

    private void loadEmployees() {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            List<Employee> list = dao.getAllEmployees();
            DefaultComboBoxModel<Employee> m = new DefaultComboBoxModel<>();
            for (Employee e : list) m.addElement(e);
            employeeCombo.setModel(m);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement employés: " + ex.getMessage());
        }
    }

    private void refreshWeekStarts() {
        try {
            weekStartCombo.removeAllItems();
            int year = (Integer) yearCombo.getSelectedItem();
            int month = (Integer) monthCombo.getSelectedItem();
            dao.OvertimeDAO dao = new dao.OvertimeDAO();
            java.util.List<java.util.Date> starts = dao.getWeekStartsForMonth(year, month);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            for (Date d : starts) weekStartCombo.addItem(df.format(d));
        } catch (Exception ex) {
            // ignore
        }
    }

    private void saveOvertime() {
        try {
            Employee emp = (Employee) employeeCombo.getSelectedItem();
            if (emp == null) { JOptionPane.showMessageDialog(this, "Sélectionnez un employé"); return; }
            String type = (String) periodTypeCombo.getSelectedItem();
            int year = (Integer) yearCombo.getSelectedItem();
            int month = (Integer) monthCombo.getSelectedItem();
            double hours = Double.parseDouble(hoursField.getText());

            OvertimeEntry entry = new OvertimeEntry();
            entry.setEmployeeId(emp.getEmpno());
            entry.setPeriodType(type);
            entry.setYear(year);
            entry.setMonth(month);
            entry.setHours(hours);

            if ("WEEKLY".equals(type)) {
                String dateStr = (String) weekStartCombo.getSelectedItem();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                entry.setPeriodStart(df.parse(dateStr));
            } else {
                Calendar cal = Calendar.getInstance();
                cal.clear();
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month-1);
                cal.set(Calendar.DAY_OF_MONTH, 1);
                entry.setPeriodStart(cal.getTime());
            }

            OvertimeDAO otDao = new OvertimeDAO();
            boolean ok = otDao.saveOvertime(entry);
            if (ok) JOptionPane.showMessageDialog(this, "Heures sup enregistrées");
            else JOptionPane.showMessageDialog(this, "Échec enregistrement");
            if (ok) {
                // After saving overtime, attempt to recompute and persist the net salary for the affected period
                try {
                    // Sum all overtime for this employee for the same month/year
                    List<OvertimeEntry> entries = otDao.getOvertimeForEmployee(entry.getEmployeeId(), entry.getYear(), entry.getMonth());
                    double totalHours = 0.0;
                    for (OvertimeEntry e : entries) totalHours += e.getHours();

                    // Determine the date to use for selecting the salary history row (periodStart we set earlier)
                    java.util.Date periodDate = entry.getPeriodStart();

                    // Get the salary for that employee at that date
                    dao.SalaryHistoryDAO histDao = new dao.SalaryHistoryDAO();
                    java.util.List<model.SalaryHistory> hist = histDao.getSalaryHistoryByDate(periodDate);
                    double salary = 0.0;
                    for (model.SalaryHistory sh : hist) {
                        if (sh.getEmpno() == entry.getEmployeeId()) { salary = sh.getSalary(); break; }
                    }
                    if (salary == 0.0) {
                        // fallback to employee base salary
                        if (emp != null) salary = emp.getSal();
                    }

                    // Try to get the latest stored CNAPS for the employee (if any)
                    dao.RubriqueDAO rubDao = new dao.RubriqueDAO();
                    double cnapsVal = 1.0; boolean cnapsIsPct = true;
                    try {
                        model.Rubrique latest = rubDao.getLatestRubrique(entry.getEmployeeId());
                        if (latest != null) {
                            cnapsVal = latest.getCnaps();
                            // We stored cnaps as an absolute amount in the rubrique table; treat as absolute
                            cnapsIsPct = false;
                        }
                    } catch (Exception ignore) {}

                    // Recompute payslip with overtime
                    dao.PayrollCalculator.PayrollResult res = dao.PayrollCalculator.computePayslipWithOvertime(
                        salary, cnapsVal, 0.0, cnapsIsPct, false, 0.0, "calculer", totalHours
                    );

                    // Persist net into salary_history row that covers this period
                    boolean updated = histDao.updateNetForEmployeeAtDate(entry.getEmployeeId(), periodDate, res.netSalary);
                    if (updated) {
                        JOptionPane.showMessageDialog(this, "Salaire net recalculé et mis à jour pour la période (incluant heures sup): " + String.format("%.2f", res.netSalary));
                    }
                } catch (Exception ex) {
                    // best-effort: log and continue
                    System.err.println("Erreur recalcul net après insertion heures sup: " + ex.getMessage());
                }
            }
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Heures invalides");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }
}
