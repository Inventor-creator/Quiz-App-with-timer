
package MiniProj.ui;


import MiniProj.models.Question;
import MiniProj.models.Quiz;
import MiniProj.models.User;
import MiniProj.DBUtil;

import javax.swing.*;
import java.awt.*;

public class AttemptQuizDialog extends JDialog {
    private Quiz quiz;
    private java.util.List<Question> questions;
    private User student;
    private int current = 0;
    private int score = 0;

    private JLabel qLabel;
    private JRadioButton aBtn, bBtn, cBtn, dBtn;
    private ButtonGroup group;

    public AttemptQuizDialog(JFrame owner, Quiz quiz, java.util.List<Question> questions, User student) {
        super(owner, "Attempt - " + quiz.title, true);
        this.quiz = quiz;
        this.questions = questions;
        this.student = student;
        initUI();
        loadQuestion();
        setSize(700,400);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));
        qLabel = new JLabel();
        qLabel.setFont(qLabel.getFont().deriveFont(Font.PLAIN, 16f));
        add(qLabel, BorderLayout.NORTH);

        JPanel opts = new JPanel(new GridLayout(4,1,6,6));
        aBtn = new JRadioButton();
        bBtn = new JRadioButton();
        cBtn = new JRadioButton();
        dBtn = new JRadioButton();
        group = new ButtonGroup();
        group.add(aBtn); group.add(bBtn); group.add(cBtn); group.add(dBtn);
        opts.add(aBtn); opts.add(bBtn); opts.add(cBtn); opts.add(dBtn);
        add(opts, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton next = new JButton("Next");
        JButton finish = new JButton("Finish");
        bottom.add(next); bottom.add(finish);
        add(bottom, BorderLayout.SOUTH);

        next.addActionListener(e -> checkAnswerAndAdvance());
        finish.addActionListener(e -> { checkAnswerAndFinish(); });
    }

    private void loadQuestion() {
        if (current >= questions.size()) return;
        Question q = questions.get(current);
        qLabel.setText(String.format("Q%d. %s", current+1, q.text));
        aBtn.setText("A. " + safe(q.a));
        bBtn.setText("B. " + safe(q.b));
        cBtn.setText("C. " + safe(q.c));
        dBtn.setText("D. " + safe(q.d));
        group.clearSelection();
    }

    private String safe(String s) { return s == null ? "" : s; }

    private void checkAnswerAndAdvance() {
        if (checkSelectedAndScore()) {
            current++;
            if (current < questions.size()) loadQuestion(); else {
                JOptionPane.showMessageDialog(this, "You completed the quiz. Score: " + score + "/" + questions.size());
                saveAndClose();
            }
        } else {
            current++;
            if (current < questions.size()) loadQuestion(); else {
                JOptionPane.showMessageDialog(this, "You completed the quiz. Score: " + score + "/" + questions.size());
                saveAndClose();
            }
        }
    }

    private void checkAnswerAndFinish() {
        checkSelectedAndScore();
        JOptionPane.showMessageDialog(this, "Quiz finished. Score: " + score + "/" + questions.size());
        saveAndClose();
    }

    private boolean checkSelectedAndScore() {
        Question q = questions.get(current);
        char selected = ' ';
        if (aBtn.isSelected()) selected = 'A';
        if (bBtn.isSelected()) selected = 'B';
        if (cBtn.isSelected()) selected = 'C';
        if (dBtn.isSelected()) selected = 'D';
        if (selected == q.correct) score++;
        return selected != ' ';
    }

    private void saveAndClose() {
        try {
            DBUtil.saveAttempt(student.id, quiz.id, score, questions.size());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to save attempt: " + ex.getMessage());
            ex.printStackTrace();
        }
        dispose();
    }
}
