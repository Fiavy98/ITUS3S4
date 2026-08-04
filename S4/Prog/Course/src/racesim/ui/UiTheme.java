package racesim.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public final class UiTheme {
    public static final Color BG_TOP = new Color(18, 18, 18);
    public static final Color BG_BOTTOM = new Color(32, 32, 32);
    public static final Color CARD = new Color(45, 45, 45);
    public static final Color CARD_BORDER = new Color(70, 70, 70);
    public static final Color TEXT = new Color(255, 255, 255);
    public static final Color MUTED = new Color(160, 160, 160);
    public static final Color ACCENT = new Color(0, 123, 255);
    public static final Color ACCENT_DARK = new Color(0, 100, 200);
    public static final Color ACCENT_SOFT = new Color(30, 50, 80);
    public static final Color SUCCESS = new Color(40, 167, 69);
    public static final Color DANGER = new Color(220, 53, 69);
    public static final Color HIGHLIGHT = new Color(255, 193, 7);

    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font SECTION = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font VALUE = new Font("Segoe UI", Font.BOLD, 18);

    private UiTheme() {
    }

    public static void styleButton(JButton button, Color background, Color foreground) {
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(background.darker(), 1, true),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFont(SECTION);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
    }

    public static void styleField(JTextField field) {
        field.setFont(BODY);
        field.setBackground(CARD);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
    }

    public static JPanel createCardPanel() {
        JPanel panel = new RoundedPanel();
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(CARD_BORDER, 1, true),
                BorderFactory.createEmptyBorder(14, 14, 14, 14)));
        return panel;
    }
}