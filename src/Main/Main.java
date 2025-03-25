package main;

import javax.swing.SwingUtilities;
import views.LoginFrame;

public class Main {
    public static void main(String[] args) {
        System.out.println("Main started...");  // Debugging output

        SwingUtilities.invokeLater(() -> {
            System.out.println("Opening LoginFrame...");
            new LoginFrame().setVisible(true);
        });
    }
}
