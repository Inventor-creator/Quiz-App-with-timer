package MiniProj.ui;

import MiniProj.models.Question;
import MiniProj.models.Quiz;
import MiniProj.models.User;
import MiniProj.DBUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AttemptQuizDialog extends JDialog {
    private final Quiz quiz;
    private final java.util.List<Question> questions;
    private final User student;
    private int current = 0;
    private int score = 0;

    private JLabel qLabel;
    private JLabel progressLbl;
    private JRadioButton aBtn, bBtn, cBtn, dBtn;
    private ButtonGroup group;

    public AttemptQuizDialog(JFrame owner, Quiz quiz, java.util.List<Question> questions, User student) {
        super(owner, "Attempt - " + quiz.getTitle(), true);
        this.quiz = quiz;
        this.questions = questions;
        this.student = student;
        initUI();
        loadQuestion();
        setSize(700, 420);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(12, 12));
        getContentPane().setBackground(new Color(0x1B262C));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0x0F4C75));
        top.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel title = new JLabel("📘 " + quiz.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(Color.WHITE);

        progressLbl = new JLabel("Question 1 of " + questions.size(), SwingConstants.RIGHT);
        progressLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        progressLbl.setForeground(new Color(0xBBE1FA));

        top.add(title, BorderLayout.WEST);
        top.add(progressLbl, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBackground(new Color(0xBBE1FA));
        center.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        qLabel = new JLabel("", SwingConstants.LEFT);
        qLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        qLabel.setForeground(new Color(0x0F4C75));
        qLabel.setVerticalAlignment(SwingConstants.TOP);
        center.add(qLabel, BorderLayout.NORTH);

        JPanel opts = new JPanel(new GridLayout(4, 1, 10, 10));
        opts.setBackground(new Color(0xBBE1FA));
        opts.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        aBtn = createStyledRadioButton();
        bBtn = createStyledRadioButton();
        cBtn = createStyledRadioButton();
        dBtn = createStyledRadioButton();

        group = new ButtonGroup();
        group.add(aBtn);
        group.add(bBtn);
        group.add(cBtn);
        group.add(dBtn);

        opts.add(aBtn);
        opts.add(bBtn);
        opts.add(cBtn);
        opts.add(dBtn);
        center.add(opts, BorderLayout.CENTER);

        add(center, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottom.setBackground(new Color(0x1B262C));
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JButton next = createStyledButton("Next", new Color(0x0F4C75));
        JButton finish = createStyledButton("Finish", new Color(0x3282B8));

        bottom.add(next);
        bottom.add(finish);
        add(bottom, BorderLayout.SOUTH);

        next.addActionListener(e -> {
            if (!checkSelectedAndScore()) {
                showInfo("Please select an answer before continuing.");
                return;
            }
            current++;
            if (current < questions.size()) {
                loadQuestion();
            } else {
                finishQuiz();
            }
        });

        finish.addActionListener(e -> {
            if (!checkSelectedAndScore()) {
                showInfo("Please select an answer before finishing.");
                return;
            }
            finishQuiz();
        });
    }

    private JRadioButton createStyledRadioButton() {
        JRadioButton btn = new JRadioButton();
        btn.setBackground(new Color(0xE1F0FA));
        btn.setForeground(new Color(0x1B262C));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(0xD4E9FA));
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(0xE1F0FA));
            }
        });
        return btn;
    }

    private JButton createStyledButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.brighter());
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    private void loadQuestion() {
        if (current >= questions.size()) return;
        Question q = questions.get(current);
        progressLbl.setText("Question " + (current + 1) + " of " + questions.size());
        qLabel.setText("<html><b>Q" + (current + 1) + ":</b> " + safe(q.getText()) + "</html>");
        aBtn.setText("A. " + safe(q.getA()));
        bBtn.setText("B. " + safe(q.getB()));
        cBtn.setText("C. " + safe(q.getC()));
        dBtn.setText("D. " + safe(q.getD()));
        group.clearSelection();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private boolean checkSelectedAndScore() {
        Question q = questions.get(current);
        char selected = ' ';
        if (aBtn.isSelected()) selected = 'A';
        if (bBtn.isSelected()) selected = 'B';
        if (cBtn.isSelected()) selected = 'C';
        if (dBtn.isSelected()) selected = 'D';
        if (selected == q.getCorrect()) score++;
        return selected != ' ';
    }

    private void finishQuiz() {
        showSuccess("You completed the quiz!\nScore: " + score + "/" + questions.size());
        saveAndClose();
    }

    private void saveAndClose() {
        try {
            DBUtil.saveAttempt(student.getId(), quiz.getId(), score, questions.size());
        } catch (Exception ex) {
            showError(" Failed to save attempt: " + ex.getMessage());
            ex.printStackTrace();
        }
        dispose();
    }

    private void showSuccess(String msg) {
        showCustomDialog("Success", msg, new Color(0x00BCD4));
    }

    private void showError(String msg) {
        showCustomDialog("Error", msg, new Color(0xE74C3C));
    }

    private void showInfo(String msg) {
        showCustomDialog("Notice", msg, new Color(0x3282B8));
    }

    private void showCustomDialog(String title, String message, Color accentColor) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(0x1B262C));
        dialog.setUndecorated(true);

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(accentColor);
        dialog.add(titleLbl, BorderLayout.NORTH);

        JLabel msgLbl = new JLabel("<html><div style='text-align:center;'>" + message.replace("\n", "<br>") + "</div></html>", SwingConstants.CENTER);
        msgLbl.setFont(new Font("SansSerif", Font.PLAIN, 15));
        msgLbl.setForeground(Color.WHITE);
        dialog.add(msgLbl, BorderLayout.CENTER);

        JButton okBtn = new JButton("OK");
        okBtn.setBackground(accentColor);
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setFocusPainted(false);
        okBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        okBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        okBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(new Color(0x1B262C));
        btnPanel.add(okBtn);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}
