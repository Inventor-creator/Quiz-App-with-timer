package MiniProj.ui;

import MiniProj.DBUtil;
import MiniProj.models.User;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class CreateQuizDialog extends JDialog {
    private JTextField titleField;
    private JTextArea descArea;
    private User instructor;

    public CreateQuizDialog(JFrame owner, User instructor) {
        super(owner, "Create New Quiz", true);
        this.instructor = instructor;
        initUI();
        pack();
        setMinimumSize(new Dimension(420, 340));
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(0x1B262C));
        setResizable(false);

        JLabel header = new JLabel("Create New Quiz");
        header.setFont(new Font("SansSerif", Font.BOLD, 22));
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setForeground(new Color(0xBBE1FA));
        header.setBorder(BorderFactory.createEmptyBorder(12, 8, 8, 8));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(0xBBE1FA));
        form.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0F4C75), 2, true),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)
        ));
        add(form, BorderLayout.CENTER);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.NORTHWEST;

        JLabel titleLbl = new JLabel("Quiz Title:");
        titleLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        titleLbl.setForeground(new Color(0x0F4C75));
        c.gridx = 0; c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        form.add(titleLbl, c);

        titleField = createStyledTextField();
        c.gridx = 1; c.gridy = 0;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        form.add(titleField, c);

        JLabel descLbl = new JLabel("Description:");
        descLbl.setFont(new Font("SansSerif", Font.PLAIN, 16));
        descLbl.setForeground(new Color(0x0F4C75));
        c.gridx = 0; c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        form.add(descLbl, c);

        descArea = createStyledTextArea();
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true));
        descScroll.setPreferredSize(new Dimension(260, 120));

        c.gridx = 1; c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        form.add(descScroll, c);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 8));
        buttons.setBackground(new Color(0x1B262C));

        JButton cancelBtn = new JButton("Cancel");
        JButton createBtn = new JButton("Create Quiz");
        stylePrimaryButton(cancelBtn, new Color(0x3282B8), Color.WHITE);
        stylePrimaryButton(createBtn, new Color(0x0F4C75), Color.WHITE);

        buttons.add(cancelBtn);
        buttons.add(createBtn);
        add(buttons, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> dispose());
        createBtn.addActionListener(e -> onCreate());
    }

    private void onCreate() {
        String title = titleField.getText().trim();
        String desc = descArea.getText().trim();

        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a quiz title.");
            return;
        }

        try {
            int quizId = DBUtil.createQuiz(title, desc, instructor.getId());
            JOptionPane.showMessageDialog(this,
                    "Quiz created successfully! ID: " + quizId,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Failed to create quiz: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(22);
        field.setBackground(new Color(0xE1F0FA));
        field.setForeground(new Color(0x1B262C));
        field.setCaretColor(new Color(0x0F4C75));
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0x0F4C75), 1, true),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea();
        area.setRows(6);
        area.setColumns(26);
        area.setBackground(new Color(0xE1F0FA));
        area.setForeground(new Color(0x1B262C));
        area.setCaretColor(new Color(0x0F4C75));
        area.setFont(new Font("SansSerif", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        return area;
    }

    private void stylePrimaryButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bg);
            }
        });
    }
}
