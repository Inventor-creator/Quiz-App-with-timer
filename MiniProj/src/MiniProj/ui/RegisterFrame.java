package MiniProj.ui;

import MiniProj.models.User;
import MiniProj.DBUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.SQLException;

public class RegisterFrame extends JPanel {
    private JFrame parent;

    public RegisterFrame(JFrame parent) {
        this.parent = parent;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setOpaque(false);

        JPanel gradientBg = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(0x1B262C),
                        0, getHeight(), new Color(0x0F4C75)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientBg.setLayout(new GridBagLayout());
        add(gradientBg, BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(0xBBE1FA));
        form.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        form.setPreferredSize(new Dimension(420, 380));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Your Account");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(0x0F4C75));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        form.add(title, c);

        JLabel userLbl = new JLabel("Username:");
        JTextField userField = createStyledTextField();
        styleLabel(userLbl);

        c.gridwidth = 1;
        c.gridx = 0; c.gridy = 1; form.add(userLbl, c);
        c.gridx = 1; form.add(userField, c);

        JLabel roleLbl = new JLabel("Role:");
        JComboBox<String> roleCombo = createStyledComboBox(new String[]{"student", "instructor"});
        styleLabel(roleLbl);
        c.gridx = 0; c.gridy = 2; form.add(roleLbl, c);
        c.gridx = 1; form.add(roleCombo, c);

        JLabel passLbl = new JLabel("Password:");
        JPasswordField passField = createStyledPasswordField();
        styleLabel(passLbl);
        c.gridx = 0; c.gridy = 3; form.add(passLbl, c);
        c.gridx = 1; form.add(passField, c);

        JButton registerBtn = new JButton("Register");
        stylePrimaryButton(registerBtn);
        c.gridx = 0; c.gridy = 4; c.gridwidth = 2;
        form.add(registerBtn, c);

        JLabel backLink = new JLabel("<html><u>Already have an account? Login here</u></html>");
        backLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLink.setHorizontalAlignment(SwingConstants.CENTER);
        backLink.setForeground(new Color(0x0F4C75));
        backLink.setFont(new Font("SansSerif", Font.BOLD, 14));
        c.gridy = 5;
        form.add(backLink, c);

        gradientBg.add(form);

        backLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backLink.setForeground(new Color(0x3282B8));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backLink.setForeground(new Color(0x0F4C75));
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                parent.setContentPane(new LoginFrame(parent));
                parent.revalidate();
                parent.repaint();
            }
        });

        registerBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            String role = (String) roleCombo.getSelectedItem();

            if (username.isEmpty() || password.isEmpty()) {
                DialogUtil.showError(this, "Please fill in all fields.");
                return;
            }

            try {
                User u = DBUtil.registerUser(username, password, role);
                DialogUtil.showSuccess(this, "Account created successfully!");
                parent.setContentPane(new LoginFrame(parent));
                parent.revalidate();
                parent.repaint();
            } catch (SQLException ex) {
                DialogUtil.showError(this, "Registration failed: " + ex.getMessage());
            }
        });
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(18);
        field.setBackground(new Color(0xE1F0FA));
        field.setForeground(new Color(0x1B262C));
        field.setCaretColor(new Color(0x0F4C75));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(0x3282B8), 2, true));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true));
            }
        });
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(18);
        field.setBackground(new Color(0xE1F0FA));
        field.setForeground(new Color(0x1B262C));
        field.setCaretColor(new Color(0x0F4C75));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(0x3282B8), 2, true));
            }

            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true));
            }
        });
        return field;
    }

    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(new Color(0xE1F0FA));
        combo.setForeground(new Color(0x1B262C));
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true));
        combo.setFocusable(false);
        combo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        combo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                combo.setBorder(BorderFactory.createLineBorder(new Color(0x3282B8), 2, true));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                combo.setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true));
            }
        });
        return combo;
    }

    private void styleLabel(JLabel label) {
        label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        label.setForeground(new Color(0x0F4C75));
    }

    private void stylePrimaryButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setBackground(new Color(0x0F4C75));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0x3282B8));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0x0F4C75));
            }
        });
    }
}
