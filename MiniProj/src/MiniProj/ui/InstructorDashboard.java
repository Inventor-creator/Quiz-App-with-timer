package MiniProj.ui;

import MiniProj.models.Attempt;
import MiniProj.models.Quiz;
import MiniProj.models.User;
import MiniProj.DBUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InstructorDashboard extends JPanel {
    private JFrame parent;
    private User instructor;
    private JList<Quiz> quizList;

    public InstructorDashboard(JFrame parent, User instructor) {
        this.parent = parent;
        this.instructor = instructor;
        initUI();
        loadQuizzes();
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));
        JPanel top = new JPanel(new BorderLayout());
        JLabel lbl = new JLabel("Instructor Dashboard - " + instructor.username);
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

        JPanel right = new JPanel(new GridLayout(4,1,8,8));
        JButton create = new JButton("Create Quiz");
        JButton viewMarks = new JButton("View Student Marks");
        JButton refresh = new JButton("Refresh");
        JButton viewStudents = new JButton("View Students");
        right.add(create); right.add(viewMarks); right.add(viewStudents); right.add(refresh);
        add(right, BorderLayout.EAST);

        create.addActionListener(e -> {
            CreateQuizDialog d = new CreateQuizDialog((JFrame)SwingUtilities.getWindowAncestor(this), instructor);
            d.setVisible(true);
            loadQuizzes();
        });

        refresh.addActionListener(e -> loadQuizzes());

        viewMarks.addActionListener(e -> {
            Quiz q = quizList.getSelectedValue();
            if (q == null) { JOptionPane.showMessageDialog(this, "Select a quiz first."); return; }
            try {
                java.util.List<Attempt> attempts = DBUtil.getAttemptsForQuiz(q.id);
                JFrame f = new JFrame("Marks for: " + q.title);
                f.setSize(700,400);
                String[] cols = {"Student", "Score", "Total", "When"};
                DefaultTableModel tm = new DefaultTableModel(cols, 0);
                for (Attempt a : attempts) tm.addRow(new Object[]{a.username, a.score, a.total, a.when});
                JTable table = new JTable(tm);
                f.add(new JScrollPane(table));
                f.setLocationRelativeTo(this);
                f.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        viewStudents.addActionListener(e -> {
            try {
                java.util.List<User> students = DBUtil.getAllStudents();
                JFrame f = new JFrame("All Students");
                f.setSize(400,400);
                String[] cols = {"ID", "Username"};
                DefaultTableModel tm = new DefaultTableModel(cols, 0);
                for (User s : students) tm.addRow(new Object[]{s.id, s.username});
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
            List<Quiz> quizzes = DBUtil.getAllQuizzes();
            DefaultListModel<Quiz> model = new DefaultListModel<>();
            for (Quiz q : quizzes) model.addElement(q);
            quizList.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "DB error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
