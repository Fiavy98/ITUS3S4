package ui;

import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditEmployeeDialog extends JDialog {
    private final EmployeeDAO dao;
    private final Employee original;
    private boolean saved = false;

    private JTextField empnoField;
    private JTextField enameField;
    private JComboBox<String> jobCombo;
    private JTextField hiredateField;
    private JComboBox<Integer> deptCombo;

    public EditEmployeeDialog(Window owner, Employee employee, EmployeeDAO dao) {
        super(owner, "Modifier Employé", ModalityType.APPLICATION_MODAL);
        this.dao = dao;
        this.original = employee;

        initializeUI();
        pack();
        setResizable(false);
    }

    private void initializeUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        empnoField = new JTextField(10);
        empnoField.setEditable(false);
        enameField = new JTextField(15);
        jobCombo = new JComboBox<>();
        hiredateField = new JTextField(12);
        deptCombo = new JComboBox<>();

        try {
            List<String> jobs = dao.getDistinctJobs();
            for (String j : jobs) jobCombo.addItem(j);
            List<Integer> depts = dao.getDistinctDeptnos();
            for (Integer d : depts) deptCombo.addItem(d);
        } catch (Exception ex) {
            // si erreur, continuer avec combos vides
        }

    // Populate fields
        empnoField.setText(String.valueOf(original.getEmpno()));
        enameField.setText(original.getEname());
        if (original.getJob() != null) jobCombo.setSelectedItem(original.getJob());
        hiredateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(original.getHiredate()));
        deptCombo.setSelectedItem(original.getDeptno());

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("ID Employé:"), gbc);
        gbc.gridx = 1; panel.add(empnoField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1; panel.add(enameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panel.add(new JLabel("Poste:"), gbc);
        gbc.gridx = 1; panel.add(jobCombo, gbc);

    gbc.gridx = 0; gbc.gridy = 3; panel.add(new JLabel("Date Embauche (yyyy-MM-dd):"), gbc);
    gbc.gridx = 1; panel.add(hiredateField, gbc);

    gbc.gridx = 0; gbc.gridy = 4; panel.add(new JLabel("Département:"), gbc);
    gbc.gridx = 1; panel.add(deptCombo, gbc);

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveBtn = new JButton("Enregistrer");
        JButton cancelBtn = new JButton("Annuler");
        btns.add(saveBtn);
        btns.add(cancelBtn);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; panel.add(btns, gbc);

        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onSave();
            }
        });

        cancelBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saved = false;
                dispose();
            }
        });

        getContentPane().add(panel);
    }

    private void onSave() {
        try {
            String ename = enameField.getText().trim();
            if (ename.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le nom est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String job = (String) jobCombo.getSelectedItem();
            if (job == null || job.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Le poste est obligatoire", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Date hiredate = new SimpleDateFormat("yyyy-MM-dd").parse(hiredateField.getText().trim());
            int deptno = (Integer) deptCombo.getSelectedItem();

            // Keep existing salary (salary managed in SalaryUpdatePanel)
            Employee updated = new Employee(original.getEmpno(), ename, job, original.getSal(), hiredate, deptno);
            try {
                if (dao.updateEmployee(updated)) {
                    saved = true;
                    JOptionPane.showMessageDialog(this, "Employé mis à jour avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Aucune modification prise en compte.", "Info", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Erreur de format numérique: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean wasSaved() {
        return saved;
    }
}
