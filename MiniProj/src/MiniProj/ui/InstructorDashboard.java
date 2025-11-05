package MiniProj.ui;

import MiniProj.DBUtil;
import MiniProj.models.Quiz;
import MiniProj.models.Attempt;
import MiniProj.models.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
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
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1B262C));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0x0F4C75));
        top.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lbl = new JLabel("Instructor Dashboard - " + instructor.getUsername());
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setForeground(Color.WHITE);
        top.add(lbl, BorderLayout.WEST);

        JButton logout = new JButton("Logout");
        stylePrimaryButton(logout, new Color(0x3282B8), Color.WHITE);
        logout.addActionListener(e -> {
            parent.setContentPane(new LoginFrame(parent));
            parent.revalidate();
            parent.repaint();
        });
        top.add(logout, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        quizList = new JList<>(new DefaultListModel<>());
        quizList.setBackground(new Color(0xBBE1FA));
        quizList.setSelectionBackground(new Color(0x3282B8));
        quizList.setSelectionForeground(Color.WHITE);
        quizList.setFixedCellHeight(60);
        quizList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        quizList.setCellRenderer(new QuizListCellRenderer());
        add(new JScrollPane(quizList), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(4, 1, 12, 12));
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        right.setBackground(new Color(0x1B262C));

        JButton createQuizBtn = new JButton("➕ Create Quiz");
        JButton addQuestionBtn = new JButton("📝 Add Question");
        JButton viewAttemptsBtn = new JButton("📊 View Attempts");
        JButton refreshBtn = new JButton("🔄 Refresh");

        stylePrimaryButton(createQuizBtn, new Color(0x0F4C75), Color.WHITE);
        stylePrimaryButton(addQuestionBtn, new Color(0x3282B8), Color.WHITE);
        stylePrimaryButton(viewAttemptsBtn, new Color(0x0F4C75), Color.WHITE);
        stylePrimaryButton(refreshBtn, new Color(0x3282B8), Color.WHITE);

        right.add(createQuizBtn);
        right.add(addQuestionBtn);
        right.add(viewAttemptsBtn);
        right.add(refreshBtn);
        add(right, BorderLayout.EAST);

        createQuizBtn.addActionListener(e -> {
            CreateQuizDialog dialog = new CreateQuizDialog(parent, instructor);
            dialog.setVisible(true);
            loadQuizzes();
        });

        addQuestionBtn.addActionListener(e -> {
            Quiz q = quizList.getSelectedValue();
            if (q == null) {
                DialogUtil.showInfo(this, "Select a quiz first.");

                return;
            }
            AddQuestionDialog dialog = new AddQuestionDialog(parent, q);
            dialog.setVisible(true);
        });

        viewAttemptsBtn.addActionListener(e -> {
            Quiz q = quizList.getSelectedValue();
            if (q == null) {
                DialogUtil.showInfo(this, "Select a quiz first.");

                return;
            }
            try {
                List<Attempt> attempts = DBUtil.getAttemptsForQuiz(q.getId());
                showAttemptsDialog(q, attempts);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error fetching attempts: " + ex.getMessage());

            }
        });

        refreshBtn.addActionListener(e -> loadQuizzes());
    }

    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = DBUtil.getAllQuizzes();
            DefaultListModel<Quiz> model = new DefaultListModel<>();
            for (Quiz q : quizzes) model.addElement(q);
            quizList.setModel(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading quizzes: " + ex.getMessage());
        }
    }

    private void showAttemptsDialog(Quiz quiz, List<Attempt> attempts) {
        JDialog dialog = new JDialog(parent, "📊 Attempts for: " + quiz.getTitle(), true);
        dialog.setSize(700, 450);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(0x1B262C));
        dialog.setLayout(new BorderLayout(0, 0)); // No spacing gaps

        JLabel title = new JLabel("📘 Attempts for Quiz: " + quiz.getTitle());
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(0xBBE1FA));
        title.setOpaque(true);
        title.setBackground(new Color(0x0F4C75));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        dialog.add(title, BorderLayout.NORTH);

        String[] cols = {"Student", "Score", "Total", "Date"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0);
        for (Attempt a : attempts) {
            tm.addRow(new Object[]{
                    a.getStudentName(),
                    a.getScore(),
                    a.getTotal(),
                    a.getWhen()
            });
        }

        JTable table = new JTable(tm);
        table.setFont(new Font("SansSerif", Font.PLAIN, 15));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 15));
        table.setBackground(new Color(0xBBE1FA));
        table.setForeground(new Color(0x1B262C));
        table.getTableHeader().setBackground(new Color(0x0F4C75));
        table.getTableHeader().setForeground(Color.WHITE);
        table.setRowHeight(28);
        table.setGridColor(new Color(0x3282B8));
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setFillsViewportHeight(true);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder()); // remove white border
        scroll.getViewport().setBackground(new Color(0xBBE1FA));
        scroll.setBackground(new Color(0xBBE1FA));
        dialog.add(scroll, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        bottom.setBackground(new Color(0x1B262C));

        JButton closeBtn = new JButton("✖ Close");
        JButton refreshBtn = new JButton("🔄 Refresh");

        stylePrimaryButton(closeBtn, new Color(0x0F4C75), Color.WHITE);
        stylePrimaryButton(refreshBtn, new Color(0x3282B8), Color.WHITE);

        bottom.add(refreshBtn);
        bottom.add(closeBtn);
        dialog.add(bottom, BorderLayout.SOUTH);

        closeBtn.addActionListener(e -> dialog.dispose());
        refreshBtn.addActionListener(e -> {
            try {
                List<Attempt> refreshed = DBUtil.getAttemptsForQuiz(quiz.getId());
                tm.setRowCount(0);
                for (Attempt a : refreshed) {
                    tm.addRow(new Object[]{
                            a.getStudentName(),
                            a.getScore(),
                            a.getTotal(),
                            a.getWhen()
                    });
                }
            } catch (SQLException ex) {
                DialogUtil.showError(dialog, "Failed to refresh: " + ex.getMessage());
            }
        });

        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 3, true));

        dialog.setVisible(true);
    }

    private void stylePrimaryButton(JButton btn, Color bg, Color fg) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
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
