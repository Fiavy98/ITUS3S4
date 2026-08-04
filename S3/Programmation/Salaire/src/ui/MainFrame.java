package ui;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private EmployeePanel employeePanel;
    private HistoryPanel historyPanel;
    private SalaryUpdatePanel salaryUpdatePanel;
    private OvertimePanel overtimePanel;
    
    public MainFrame() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Gestion des Employés - Historique des Salaires");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
       
        tabbedPane = new JTabbedPane();
        employeePanel = new EmployeePanel();
    historyPanel = new HistoryPanel();
    salaryUpdatePanel = new SalaryUpdatePanel();
    overtimePanel = new OvertimePanel();
        
        tabbedPane.addTab("📋 Gestion Employés", employeePanel);
        tabbedPane.addTab("📊 Historique Salaires", historyPanel);
        tabbedPane.addTab("💰 Modifier Salaire", salaryUpdatePanel);
    tabbedPane.addTab("🕒 Heures Supplémentaires", overtimePanel);
        
        add(tabbedPane);
    }
}