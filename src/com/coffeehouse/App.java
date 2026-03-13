package com.coffeehouse;

import javax.swing.SwingUtilities;
import com.coffeehouse.util.DatabaseConnection;
import com.coffeehouse.util.Logger;
import com.coffeehouse.gui.GUI;

public class App {

	public static void main(String[] args) {
		try {
			Logger.debug(System.getProperty("java.class.path"));
			Logger.info("Connecting to database...");
			DatabaseConnection.testConnection(); 
			
			SwingUtilities.invokeLater(() -> {
				try {
					GUI gui = new GUI(); 
					gui.init();
					
					Logger.info("Application ready.");
				} catch (Exception e) {
					Logger.error("Failed to initialize GUI", e);
				}
			});

		} catch (Exception e) {
			Logger.error("System startup failed: " + e.getMessage(), e);
			System.exit(1);
		}
	}
}