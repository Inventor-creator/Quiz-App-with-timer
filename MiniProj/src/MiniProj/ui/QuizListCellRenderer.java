package MiniProj.ui;

import MiniProj.models.Quiz;
import javax.swing.*;
import java.awt.*;

public class QuizListCellRenderer extends JPanel implements ListCellRenderer<Quiz> {
    private JLabel titleLabel;
    private JLabel descLabel;

    public QuizListCellRenderer() {
        setLayout(new BorderLayout(5, 3));
        setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        titleLabel = new JLabel();
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setForeground(new Color(0x0F4C75));

        descLabel = new JLabel();
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        descLabel.setForeground(new Color(0x1B262C));

        add(titleLabel, BorderLayout.NORTH);
        add(descLabel, BorderLayout.SOUTH);
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(
            JList<? extends Quiz> list,
            Quiz quiz,
            int index,
            boolean isSelected,
            boolean cellHasFocus) {

        if (quiz != null) {
            titleLabel.setText(quiz.getTitle());
            String desc = quiz.getDescription();
            descLabel.setText(
                    (desc == null || desc.isEmpty())
                            ? "<html><i>No description</i></html>"
                            : desc
            );
        }

        if (isSelected) {
            setBackground(new Color(0x3282B8));
            titleLabel.setForeground(Color.WHITE);
            descLabel.setForeground(new Color(0xEAF6FF));
        } else {
            setBackground(new Color(0xBBE1FA));
            titleLabel.setForeground(new Color(0x0F4C75));
            descLabel.setForeground(new Color(0x1B262C));
        }

        return this;
    }
}
