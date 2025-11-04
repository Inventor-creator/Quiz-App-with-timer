package MiniProj.ui;

import MiniProj.models.Attempt;
import MiniProj.models.Question;
import MiniProj.models.Quiz;
import MiniProj.models.User;
import MiniProj.DBUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentDashboard extends JPanel {
    private JFrame parent;
    private User student;
    private JList<Quiz> quizList;

    public StudentDashboard(JFrame parent, User student) {
        this.parent = parent;
        this.student = student;
        initUI();
        loadQuizzes();
    }

    private void initUI() {
        setLayout(new BorderLayout(10,10));
        JPanel top = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Student Dashboard - " + student.username);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        top.add(lbl, BorderLayout.WEST);

        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> {
            parent.setContentPane(new LoginFrame(parent));
            parent.revalidate();
        });
        top.add(logout, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        quizList = new JList<>(new DefaultListModel<>());
        add(new JScrollPane(quizList), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(3,1,8,8));
        JButton attemptBtn = new JButton("Attempt Selected Quiz");
        JButton refresh = new JButton("Refresh");
        JButton viewMarks = new JButton("My Marks");
        right.add(attemptBtn); right.add(refresh); right.add(viewMarks);
        add(right, BorderLayout.EAST);

        attemptBtn.addActionListener(e -> {
            Quiz q = quizList.getSelectedValue();
            if (q == null) { JOptionPane.showMessageDialog(this, "Select a quiz first."); return; }
            try {
                List<Question> questions = DBUtil.getQuestionsForQuiz(q.id);
                if (questions.isEmpty()) { JOptionPane.showMessageDialog(this, "Quiz has no questions."); return; }
                AttemptQuizDialog dialog = new AttemptQuizDialog((JFrame)SwingUtilities.getWindowAncestor(this), q, questions, student);
                dialog.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        refresh.addActionListener(e -> loadQuizzes());

        viewMarks.addActionListener(e -> {
            try {
                List<Attempt> attempts = new ArrayList<>();
                String q = "SELECT a.id, a.student_id, u.username, a.quiz_id, a.score, a.total, a.attempted_at FROM attempts a JOIN users u ON a.student_id = u.id WHERE a.student_id = ? ORDER BY a.attempted_at DESC";
                try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(q)) {
                    ps.setInt(1, student.id);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        attempts.add(new Attempt(rs.getInt("id"), rs.getInt("student_id"), rs.getString("username"), rs.getInt("quiz_id"), rs.getInt("score"), rs.getInt("total"), rs.getTimestamp("attempted_at").toLocalDateTime()));
                    }
                }

                JFrame f = new JFrame("My Marks");
                f.setSize(600,400);
                String[] cols = {"Quiz ID", "Score", "Total", "When"};
                DefaultTableModel tm = new DefaultTableModel(cols, 0);
                for (Attempt a : attempts) tm.addRow(new Object[]{a.quizId, a.score, a.total, a.when});
                JTable table = new JTable(tm);
                f.add(new JScrollPane(table));
                f.setLocationRelativeTo(this);
                f.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }

    private void loadQuizzes() {
        try {
            java.util.List<Quiz> quizzes = DBUtil.getAllQuizzes();
            DefaultListModel<Quiz> model = new DefaultListModel<>();
            for (Quiz q : quizzes) model.addElement(q);
            quizList.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
