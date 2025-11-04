package MiniProj;
import MiniProj.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Initialize DB (will create tables if missing)
                DBUtil.init("jdbc:postgresql://localhost:5432/JavaMiniProj ", "postgres", "postgres");
                DBUtil.initTables();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "DB initialization failed: " + ex.getMessage());
                System.exit(1);
            }

            JFrame frame = new JFrame("Quiz App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new LoginFrame(frame));
            frame.setVisible(true);
        });
    }
}
