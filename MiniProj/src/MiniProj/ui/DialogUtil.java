package MiniProj.ui;

import javax.swing.*;
import java.awt.*;

public class DialogUtil {

    private static final Color BG_COLOR = new Color(0x1B262C);        // dark background
    private static final Color TITLE_COLOR = new Color(0x0F4C75);     // default title color
    private static final Color TEXT_COLOR = new Color(0xBBE1FA);      // light text
    private static final Color ERROR_COLOR = new Color(0xE74C3C);     // 🔴 red error
    private static final Color SUCCESS_COLOR = new Color(0x00B8D9);   // 💠 cyan success
    private static final Color INFO_COLOR = new Color(0x2980B9);      // 🔵 blue info
    private static final Font FONT = new Font("SansSerif", Font.PLAIN, 15);

    public static void showStyledMessage(Component parent, String title, String message, int type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        Color accent = switch (type) {
            case JOptionPane.ERROR_MESSAGE -> ERROR_COLOR;
            case JOptionPane.INFORMATION_MESSAGE -> INFO_COLOR;
            case JOptionPane.PLAIN_MESSAGE -> SUCCESS_COLOR;
            default -> TITLE_COLOR;
        };

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(accent);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JTextArea msg = new JTextArea(message);
        msg.setFont(FONT);
        msg.setEditable(false);
        msg.setFocusable(false);
        msg.setOpaque(false);
        msg.setForeground(TEXT_COLOR);
        msg.setLineWrap(true);
        msg.setWrapStyleWord(true);
        msg.setBorder(null);

        JScrollPane scrollPane = new JScrollPane(msg);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setPreferredSize(new Dimension(300, Math.min(140, message.length() * 2)));

        JButton okBtn = new JButton("OK");
        okBtn.setBackground(accent);
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setFocusPainted(false);
        okBtn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        okBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        okBtn.addActionListener(e -> SwingUtilities.getWindowAncestor(okBtn).dispose());

        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(BG_COLOR);
        btnPanel.add(okBtn);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(panel);
        dialog.pack();
        dialog.setSize(380, Math.max(160, dialog.getPreferredSize().height));
        dialog.setLocationRelativeTo(parent);
        dialog.setResizable(false);
        dialog.setVisible(true);
    }

    public static void showError(Component parent, String message) {
        showStyledMessage(parent, "Error", message, JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String message) {
        showStyledMessage(parent, "Info", message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showSuccess(Component parent, String message) {
        showStyledMessage(parent, "Success", message, JOptionPane.PLAIN_MESSAGE);
    }
}
