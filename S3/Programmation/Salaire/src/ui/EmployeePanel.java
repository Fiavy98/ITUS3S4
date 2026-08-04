package ui;

import model.Employee;
import dao.EmployeeDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EmployeePanel extends JPanel {
    private JTable employeeTable;
    private DefaultTableModel tableModel;
    
    // Composants pour le formulaire
    private JTextField empnoField;
    private JTextField enameField;
    private JComboBox<String> jobCombo;
    private JTextField salField;
    private JTextField hiredateField;
    private JComboBox<Integer> deptCombo;
    private JButton addButton;
    private JButton clearButton;
    private JLabel messageLabel;
    
    public EmployeePanel() {
        initializeUI();
        loadEmployees();
    }

    private void showPayslipForSelected() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow < 0) {
            showMessage("Sélectionnez un employé pour voir le bulletin.", Color.RED);
            return;
        }

        int empno = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        try {
            dao.RubriqueDAO rubDao = new dao.RubriqueDAO();
            model.Rubrique r = rubDao.getLatestRubrique(empno);
            if (r == null) {
                showMessage("Aucun bulletin trouvé pour cet employé.", Color.RED);
                return;
            }

            double salaireBrut = r.getSalaireBrut();
            double cnaps = r.getCnaps();
            // OSTI not present in rubrique table/schema; assume 0
            double osti = 0.0;
            double bonus = r.getBonus();
            double irsa = dao.PayrollCalculator.calculateIRSA(salaireBrut, cnaps, osti);
            double net = salaireBrut - cnaps - osti - irsa + bonus;

            StringBuilder sb = new StringBuilder();
            sb.append(String.format("Employé: %d\n", empno));
            sb.append(String.format("Salaire brut: %.2f\n", salaireBrut));
            sb.append(String.format("CNAPS: %.2f\n", cnaps));
            sb.append(String.format("IRSA: %.2f\n", irsa));
            sb.append(String.format("Bonus: %.2f\n", bonus));
            sb.append(String.format("Salaire net: %.2f\n", net));

            JOptionPane.showMessageDialog(this, sb.toString(), "Bulletin de paie", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            showMessage("Erreur lors de la récupération du bulletin: " + ex.getMessage(), Color.RED);
        }
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        
        // Titre
        JLabel titleLabel = new JLabel("Gestion des Employés - Schéma SCOTT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);
        
        // Panel principal avec formulaire et liste
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(createFormPanel());
        splitPane.setRightComponent(createTablePanel());
        splitPane.setDividerLocation(400);
        add(splitPane, BorderLayout.CENTER);
        
        // Panel de message
        messageLabel = new JLabel(" ", JLabel.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        add(messageLabel, BorderLayout.SOUTH);
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Ajouter un Nouvel Employé"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Initialisation des composants
    empnoField = new JTextField(15);
    enameField = new JTextField(15);
    jobCombo = new JComboBox<>();
    salField = new JTextField(15);
    hiredateField = new JTextField(15);
        hiredateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    deptCombo = new JComboBox<>();
        
        addButton = new JButton("Ajouter Employé");
        clearButton = new JButton("Effacer");
        
        // Positionnement des composants
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("ID Employé*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(empnoField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nom*:"), gbc);
        gbc.gridx = 1;
        formPanel.add(enameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Poste*:"), gbc);
        gbc.gridx = 1;
    formPanel.add(jobCombo, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Salaire*:"), gbc);
        gbc.gridx = 1;
    formPanel.add(salField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Date Embauche* (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
    formPanel.add(hiredateField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Département*:"), gbc);
        gbc.gridx = 1;
    formPanel.add(deptCombo, gbc);
        
        // Boutons
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(clearButton);
        formPanel.add(buttonPanel, gbc);
        
        // Gestion des événements
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        return formPanel;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Liste des Employés Existants"));
        
        // Tableau des employés
        String[] columnNames = {"ID", "Nom", "Poste", "Salaire", "Date Embauche", "Département"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // pour  Rendre le tableau non éditable
            }
        };
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        // Delete button under the table
        JPanel tblBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton editButton = new JButton("Modifier Employé");
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = employeeTable.getSelectedRow();
                    if (selectedRow < 0) {
                        showMessage("Sélectionnez un employé dans le tableau pour modifier.", Color.RED);
                        return;
                    }

                    int empno = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
                    try {
                        EmployeeDAO dao = new EmployeeDAO();
                        Employee emp = dao.getEmployeeById(empno);
                        if (emp == null) {
                            showMessage("Employé introuvable.", Color.RED);
                            return;
                        }

                        EditEmployeeDialog dialog = new EditEmployeeDialog(SwingUtilities.getWindowAncestor(EmployeePanel.this), emp, dao);
                        dialog.setLocationRelativeTo(EmployeePanel.this);
                        dialog.setVisible(true);
                        if (dialog.wasSaved()) {
                            showMessage("Employé modifié.", Color.GREEN);
                            loadEmployees();
                        }
                    } catch (Exception ex) {
                        showMessage("Erreur lors de l'édition: " + ex.getMessage(), Color.RED);
                    }
                }
            });
        tblBtnPanel.add(editButton);

            JButton deleteButton = new JButton("Supprimer Employé");
            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deleteSelectedEmployee();
                }
            });
        tblBtnPanel.add(deleteButton);
            
            // Voir bulletin button
            JButton viewPayslipBtn = new JButton("Voir Bulletin");
            viewPayslipBtn.addActionListener(e -> showPayslipForSelected());
            tblBtnPanel.add(viewPayslipBtn);
        tablePanel.add(tblBtnPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private void addEmployee() {
        try {
            // Validation des champs
            if (empnoField.getText().trim().isEmpty() ||
                enameField.getText().trim().isEmpty() ||
                jobCombo.getSelectedItem() == null ||
                salField.getText().trim().isEmpty() ||
                hiredateField.getText().trim().isEmpty() ||
                deptCombo.getSelectedItem() == null) {
                
                showMessage("Tous les champs sont obligatoires!", Color.RED);
                return;
            }
            
            int empno = Integer.parseInt(empnoField.getText());
            String ename = enameField.getText().trim();
            String job = (String) jobCombo.getSelectedItem();
            double sal = Double.parseDouble(salField.getText().trim().replaceAll("[^0-9.]", ""));
            Date hiredate = new SimpleDateFormat("yyyy-MM-dd").parse(hiredateField.getText().trim());
            int deptno = (Integer) deptCombo.getSelectedItem();
            
            // Vérifier si l'employé existe déjà
            EmployeeDAO dao = new EmployeeDAO();
            if (dao.isEmployeeExists(empno)) {
                showMessage("L'employé avec l'ID " + empno + " existe déjà!", Color.RED);
                return;
            }
            
            Employee employee = new Employee(empno, ename, job, sal, hiredate, deptno);
            
            if (dao.addEmployee(employee)) {
                showMessage("Employé ajouté avec succès!", Color.GREEN);
                clearForm();
                loadEmployees(); // Rafraîchir la liste
            } else {
                showMessage("Erreur lors de l'ajout de l'employé", Color.RED);
            }
            
        } catch (NumberFormatException ex) {
            showMessage("Erreur de format numérique: " + ex.getMessage(), Color.RED);
        } catch (Exception ex) {
            showMessage("Erreur: " + ex.getMessage(), Color.RED);
        }
    }
    
    private void clearForm() {
        empnoField.setText("");
        enameField.setText("");
        jobCombo.setSelectedIndex(jobCombo.getItemCount() > 0 ? 0 : -1);
    salField.setText("");
        hiredateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        deptCombo.setSelectedIndex(deptCombo.getItemCount() > 0 ? 0 : -1);
        messageLabel.setText(" ");
    }
    
    private void showMessage(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
    
    private void loadEmployees() {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            List<Employee> employees = dao.getAllEmployees();
            List<String> jobs = dao.getDistinctJobs();
            List<Integer> depts = dao.getDistinctDeptnos();

            // Fill job combo
            jobCombo.removeAllItems();
            for (String j : jobs) {
                jobCombo.addItem(j);
            }

            salField.setText("");

            // Fill dept combo
            deptCombo.removeAllItems();
            for (Integer d : depts) {
                deptCombo.addItem(d);
            }
            
            // Vider le tableau
            tableModel.setRowCount(0);
            
            // Remplir avec les données existantes
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            for (Employee emp : employees) {
                Object[] row = {
                    emp.getEmpno(),
                    emp.getEname(),
                    emp.getJob(),
                    String.format("%.2f", emp.getSal()),
                    dateFormat.format(emp.getHiredate()),
                    emp.getDeptno()
                };
                tableModel.addRow(row);
            }
        } catch (Exception ex) {
            showMessage("Erreur lors du chargement des employés: " + ex.getMessage(), Color.RED);
        }
    }

        // Add delete capability: remove selected employee
        private void deleteSelectedEmployee() {
            int selectedRow = employeeTable.getSelectedRow();
            if (selectedRow < 0) {
                showMessage("Sélectionnez un employé dans le tableau pour supprimer.", Color.RED);
                return;
            }

            int empno = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
            int choice = JOptionPane.showConfirmDialog(this, "Voulez-vous supprimer l'employé " + empno + " ?", "Confirmer", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;

            try {
                EmployeeDAO dao = new EmployeeDAO();
                if (dao.deleteEmployee(empno)) {
                    showMessage("Employé supprimé.", Color.GREEN);
                    loadEmployees();
                } else {
                    showMessage("Erreur lors de la suppression.", Color.RED);
                }
            } catch (Exception ex) {
                showMessage("Erreur: " + ex.getMessage(), Color.RED);
            }
        }
}