package MiniProj.ui;

import MiniProj.models.User;
import MiniProj.DBUtil;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CreateQuizDialog extends JDialog {
    private User instructor;
    private JTextField titleField;
    private JTextArea descArea;
    private DefaultTableModel qModel;

    public CreateQuizDialog(JFrame owner, User instructor) {
        super(owner, "Create Quiz", true);
        this.instructor = instructor;
        initUI();
        setSize(800,500);
        setLocationRelativeTo(owner);
    }

    private void initUI() {
        setLayout(new BorderLayout(8,8));
        JPanel top = new JPanel(new GridLayout(2,1));
        titleField = new JTextField();
        descArea = new JTextArea(3,40);
        top.add(new JLabel("Title:")); top.add(titleField);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(descArea), BorderLayout.CENTER);

        String[] cols = {"Question", "A", "B", "C", "D", "Correct (A/B/C/D)"};
        qModel = new DefaultTableModel(cols, 0);
        JTable table = new JTable(qModel);
        add(new JScrollPane(table), BorderLayout.WEST);

        JPanel right = new JPanel(new GridLayout(5,1,8,8));
        JButton addRow = new JButton("Add Question Row");
        JButton removeRow = new JButton("Remove Selected Row");
        JButton save = new JButton("Save Quiz");
        JButton cancel = new JButton("Cancel");
        right.add(addRow); right.add(removeRow); right.add(save); right.add(cancel);
        add(right, BorderLayout.EAST);

        addRow.addActionListener(e -> qModel.addRow(new Object[]{"","","","","",""}));
        removeRow.addActionListener(e -> {
            int r = table.getSelectedRow();
            if (r >= 0) qModel.removeRow(r);
        });

        cancel.addActionListener(e -> dispose());

        save.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descArea.getText().trim();
            if (title.isEmpty()) { JOptionPane.showMessageDialog(this, "Title required"); return; }
            if (qModel.getRowCount() == 0) { JOptionPane.showMessageDialog(this, "Add at least one question"); return; }
            try {
                int quizId = DBUtil.createQuiz(title, desc, instructor.id);
                for (int i = 0; i < qModel.getRowCount(); ++i) {
                    String question = (String)(qModel.getValueAt(i,0) == null ? "" : qModel.getValueAt(i,0));
                    String a = (String)(qModel.getValueAt(i,1) == null ? "" : qModel.getValueAt(i,1));
                    String b = (String)(qModel.getValueAt(i,2) == null ? "" : qModel.getValueAt(i,2));
                    String c = (String)(qModel.getValueAt(i,3) == null ? "" : qModel.getValueAt(i,3));
                    String d = (String)(qModel.getValueAt(i,4) == null ? "" : qModel.getValueAt(i,4));
                    String corr = (String)(qModel.getValueAt(i,5) == null ? "" : qModel.getValueAt(i,5));
                    if (question.trim().isEmpty()) continue;
                    char correct = (corr.trim().isEmpty() ? ' ' : corr.trim().toUpperCase().charAt(0));
                    DBUtil.addQuestion(quizId, question, a, b, c, d, correct);
                }
                JOptionPane.showMessageDialog(this, "Quiz saved successfully");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Failed to save quiz: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
}
