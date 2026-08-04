package com.gestion.stock.util;

import java.awt.*;
import javax.swing.*;

public final class SwingUtils {
	private SwingUtils() {}
    
	public static void showError(Component parent, String message, Exception e) {
		String details = message;
		if (e != null && e.getMessage() != null && !e.getMessage().isBlank()) {
			details = message + ": " + e.getMessage();
		}
		JOptionPane.showMessageDialog(parent, details, "Erreur", JOptionPane.ERROR_MESSAGE);
	}
}
