package MiniProj;
import MiniProj.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DBUtil.init("jdbc:postgresql://localhost:5432/javaminiproj", "postgres", "postgres");
                DBUtil.initTables();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "DB initialization failed: " + ex.getMessage());
                System.exit(1);
            }

            JFrame frame = new JFrame("The Quiz App");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            ImageIcon icon = new ImageIcon("src/MiniProj/assest/q.png");
            frame.setIconImage(icon.getImage());
            frame.setContentPane(new LoginFrame(frame));
            frame.setVisible(true);
        });
    }
}
