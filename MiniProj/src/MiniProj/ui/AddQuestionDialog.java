package MiniProj.ui;

import MiniProj.DBUtil;
import MiniProj.models.Quiz;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class AddQuestionDialog extends JDialog {
    private Quiz quiz;
    private JTextArea questionField;
    private JTextField optA, optB, optC, optD;
    private JComboBox<String> correctCombo;

    private static final Color BG_DARK = new Color(0x1B262C);
    private static final Color BG_PANEL = new Color(0x0F4C75);
    private static final Color BG_LIGHT = new Color(0xBBE1FA);
    private static final Color PRIMARY = new Color(0x3282B8);
    private static final Color TEXT_LIGHT = Color.WHITE;
    private static final Font FONT_REGULAR = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONT_BOLD = new Font("SansSerif", Font.BOLD, 15);

    public AddQuestionDialog(JFrame parent, Quiz quiz) {
        super(parent, "Add Question - " + quiz.getTitle(), true);
        this.quiz = quiz;
        initUI();
        setSize(600, 500);
        setLocationRelativeTo(parent);
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_DARK);

        JLabel header = new JLabel("➕ Add Question to Quiz: " + quiz.getTitle());
        header.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.setForeground(BG_LIGHT);
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setOpaque(true);
        header.setBackground(BG_PANEL);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(BG_LIGHT);
        form.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;

        JLabel qLabel = createLabel("Question:");
        questionField = new JTextArea(4, 30);
        questionField.setLineWrap(true);
        questionField.setWrapStyleWord(true);
        questionField.setFont(FONT_REGULAR);
        JScrollPane qScroll = new JScrollPane(questionField);
        qScroll.setPreferredSize(new Dimension(400, 80));
        styleScroll(qScroll);

        JLabel aLabel = createLabel("Option A:");
        optA = createInput(30);
        JLabel bLabel = createLabel("Option B:");
        optB = createInput(30);
        JLabel cLabel = createLabel("Option C:");
        optC = createInput(30);
        JLabel dLabel = createLabel("Option D:");
        optD = createInput(30);

        JLabel correctLabel = createLabel("Correct Option:");
        correctCombo = new JComboBox<>(new String[]{"A", "B", "C", "D"});
        correctCombo.setFont(FONT_REGULAR);
        correctCombo.setPreferredSize(new Dimension(60, 30));

        c.gridx = 0; c.gridy = 0; form.add(qLabel, c);
        c.gridx = 1; form.add(qScroll, c);
        c.gridx = 0; c.gridy = 1; form.add(aLabel, c);
        c.gridx = 1; form.add(optA, c);
        c.gridx = 0; c.gridy = 2; form.add(bLabel, c);
        c.gridx = 1; form.add(optB, c);
        c.gridx = 0; c.gridy = 3; form.add(cLabel, c);
        c.gridx = 1; form.add(optC, c);
        c.gridx = 0; c.gridy = 4; form.add(dLabel, c);
        c.gridx = 1; form.add(optD, c);
        c.gridx = 0; c.gridy = 5; form.add(correctLabel, c);
        c.gridx = 1; form.add(correctCombo, c);

        add(form, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottom.setBackground(BG_DARK);

        JButton addBtn = createButton("✔ Add Question", PRIMARY);
        JButton cancelBtn = createButton("✖ Cancel", BG_PANEL);

        bottom.add(addBtn);
        bottom.add(cancelBtn);
        add(bottom, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addQuestion());
        cancelBtn.addActionListener(e -> dispose());
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(FONT_BOLD);
        lbl.setForeground(BG_PANEL.darker());
        return lbl;
    }

    private JTextField createInput(int width) {
        JTextField field = new JTextField(width);
        field.setFont(FONT_REGULAR);
        field.setPreferredSize(new Dimension(400, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BG_PANEL, 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        return field;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(TEXT_LIGHT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(bg.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(bg); }
        });
        return btn;
    }

    private void styleScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(BG_PANEL, 1));
        scroll.getViewport().setBackground(Color.WHITE);
    }

    private void addQuestion() {
        String text = questionField.getText().trim();
        String a = optA.getText().trim();
        String b = optB.getText().trim();
        String c = optC.getText().trim();
        String d = optD.getText().trim();
        char correct = ((String) correctCombo.getSelectedItem()).charAt(0);

        if (text.isEmpty() || a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty()) {
            DialogUtil.showError(this, "Please fill all fields before adding the question.");
            return;
        }

        try {
            DBUtil.addQuestion(quiz.getId(), text, a, b, c, d, correct);
            DialogUtil.showSuccess(this, "Question added successfully!");
            dispose();
        } catch (SQLException ex) {
            DialogUtil.showError(this, "Database error: " + ex.getMessage());
        }
    }
}
