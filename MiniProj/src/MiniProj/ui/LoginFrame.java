package MiniProj.ui;

import MiniProj.models.User;
import MiniProj.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginFrame extends JPanel {
    private JFrame parent;

    public LoginFrame(JFrame parent) {
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Quiz App - Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JLabel userLbl = new JLabel("Username:");
        JTextField userField = new JTextField(20);
        JLabel roleLbl = new JLabel("Role:");
        String[] roles = {"student","instructor"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);

        c.gridx = 0; c.gridy = 0; form.add(userLbl, c);
        c.gridx = 1; form.add(userField, c);
        c.gridx = 0; c.gridy = 1; form.add(roleLbl, c);
        c.gridx = 1; form.add(roleCombo, c);

        JButton loginBtn = new JButton("Login");
        c.gridx = 0; c.gridy = 2; c.gridwidth = 2; form.add(loginBtn, c);
        add(form, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String role = (String) roleCombo.getSelectedItem();
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter username.");
                return;
            }
            try {
                User u = DBUtil.ensureUserExists(username, role);
                if (role.equals("student")) {
                    parent.setContentPane(new StudentDashboard(parent, u));
                } else {
                    parent.setContentPane(new InstructorDashboard(parent, u));
                }
                parent.revalidate();
                parent.repaint();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
