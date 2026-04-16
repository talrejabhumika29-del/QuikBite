package dbnew;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main.java
 * Entry point for the QuickBite - Food Order Manager application.
 * Simply launches the MainFrame on the Swing Event Dispatch Thread (EDT).
 */
public class Main {

    public static void main(String[] args) {
        // Run GUI creation on the Event Dispatch Thread (best practice for Swing)
        SwingUtilities.invokeLater(() -> {
            try {
                // Use the system look-and-feel for native window decorations
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Falls back to default Swing L&F if system one is unavailable
                System.err.println("Could not set system look-and-feel: " + e.getMessage());
            }

            // Open the main window
            new MainFrame();
        });
    }
}
