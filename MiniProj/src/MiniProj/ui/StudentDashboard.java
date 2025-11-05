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
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(0x1B262C));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(0x0F4C75));
        top.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lbl = new JLabel("🎓 Student Dashboard - " + student.getUsername());
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

        JPanel right = new JPanel(new GridLayout(3, 1, 12, 12));
        right.setBackground(new Color(0x1B262C));
        right.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton attemptBtn = new JButton("📝 Attempt Quiz");
        JButton refresh = new JButton("🔄 Refresh");
        JButton viewMarks = new JButton("📊 My Marks");

        stylePrimaryButton(attemptBtn, new Color(0x0F4C75), Color.WHITE);
        stylePrimaryButton(refresh, new Color(0x3282B8), Color.WHITE);
        stylePrimaryButton(viewMarks, new Color(0x0F4C75), Color.WHITE);

        right.add(attemptBtn);
        right.add(viewMarks);
        right.add(refresh);
        add(right, BorderLayout.EAST);

        attemptBtn.addActionListener(e -> {
            Quiz q = quizList.getSelectedValue();
            if (q == null) {
                showCustomDialog("Select Quiz", "Please select a quiz first.", new Color(0xE74C3C));
                return;
            }
            try {
                List<Question> questions = DBUtil.getQuestionsForQuiz(q.getId());
                if (questions.isEmpty()) {
                    showCustomDialog("Empty Quiz", "This quiz has no questions yet.", new Color(0xE74C3C));
                    return;
                }
                AttemptQuizDialog dialog = new AttemptQuizDialog(
                        parent, q, questions, student
                );
                dialog.setVisible(true);
            } catch (Exception ex) {
                showCustomDialog("Database Error", ex.getMessage(), new Color(0xE74C3C));
            }
        });

        refresh.addActionListener(e -> loadQuizzes());

        viewMarks.addActionListener(e -> showMarks());
    }

    private void loadQuizzes() {
        try {
            List<Quiz> quizzes = DBUtil.getAllQuizzes();
            DefaultListModel<Quiz> model = new DefaultListModel<>();
            for (Quiz q : quizzes) model.addElement(q);
            quizList.setModel(model);
        } catch (Exception ex) {
            showCustomDialog("❌ DB Error", ex.getMessage(), new Color(0xE74C3C));
        }
    }

    private void showMarks() {
        try {
            List<Attempt> attempts = new ArrayList<>();
            String q = """
            SELECT a.id, a.student_id, u.username, a.quiz_id, a.score, a.total, a.attempted_at
            FROM attempts a
            JOIN users u ON a.student_id = u.id
            WHERE a.student_id = ?
            ORDER BY a.attempted_at DESC
        """;
            try (PreparedStatement ps = DBUtil.getConnection().prepareStatement(q)) {
                ps.setInt(1, student.getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    attempts.add(new Attempt(
                            rs.getInt("id"),
                            rs.getInt("student_id"),
                            rs.getString("username"),
                            rs.getInt("quiz_id"),
                            rs.getInt("score"),
                            rs.getInt("total"),
                            rs.getTimestamp("attempted_at").toLocalDateTime()
                    ));
                }
            }

            JDialog dialog = new JDialog(parent, "📊 My Quiz Marks", true);
            dialog.setSize(820, 430);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            dialog.getContentPane().setBackground(new Color(0x1B262C));

            JLabel title = new JLabel("🧾 My Quiz Attempts");
            title.setFont(new Font("SansSerif", Font.BOLD, 20));
            title.setForeground(new Color(0xBBE1FA));
            title.setOpaque(true);
            title.setBackground(new Color(0x0F4C75));
            title.setHorizontalAlignment(SwingConstants.CENTER);
            title.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
            dialog.add(title, BorderLayout.NORTH);

            String[] cols = {"Quiz ID", "Score", "Total", "Date"};
            DefaultTableModel tm = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Disable editing
                }
            };

            for (Attempt a : attempts) {
                String formattedDate = a.getWhen().toString().replace("T", " ").split("\\.")[0];
                tm.addRow(new Object[]{
                        a.getQuizId(),
                        a.getScore(),
                        a.getTotal(),
                        formattedDate
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
            table.setSelectionBackground(new Color(0x3282B8));
            table.setSelectionForeground(Color.WHITE);
            table.setFocusable(false);
            table.setAutoCreateRowSorter(true); // ✅ enable sorting

            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.getColumnModel().getColumn(0).setPreferredWidth(100);
            table.getColumnModel().getColumn(1).setPreferredWidth(100);
            table.getColumnModel().getColumn(2).setPreferredWidth(100);
            table.getColumnModel().getColumn(3).setPreferredWidth(500);

            JScrollPane scroll = new JScrollPane(table);
            scroll.setBorder(BorderFactory.createEmptyBorder());
            dialog.add(scroll, BorderLayout.CENTER);

            JButton closeBtn = new JButton("✖ Close");
            stylePrimaryButton(closeBtn, new Color(0x0F4C75), Color.WHITE);
            closeBtn.addActionListener(e -> dialog.dispose());

            JPanel bottom = new JPanel();
            bottom.setBackground(new Color(0x1B262C));
            bottom.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
            bottom.add(closeBtn);
            dialog.add(bottom, BorderLayout.SOUTH);

            dialog.getRootPane().setBorder(BorderFactory.createLineBorder(new Color(0x0F4C75), 3, true));
            dialog.setVisible(true);

        } catch (Exception ex) {
            showCustomDialog("Error", ex.getMessage(), new Color(0xE74C3C));
        }
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

    private void showCustomDialog(String title, String message, Color accentColor) {
        JDialog dialog = new JDialog(parent, title, true);
        dialog.setLayout(new BorderLayout(15, 15));
        dialog.setSize(400, 180);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(new Color(0x1B262C));

        JLabel titleLbl = new JLabel(title, SwingConstants.CENTER);
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLbl.setForeground(accentColor);
        dialog.add(titleLbl, BorderLayout.NORTH);

        JLabel msgLbl = new JLabel("<html><div style='text-align:center;'>" + message + "</div></html>", SwingConstants.CENTER);
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

        dialog.setUndecorated(true);
        dialog.getRootPane().setBorder(BorderFactory.createLineBorder(accentColor, 3, true));
        dialog.setVisible(true);
    }
}
