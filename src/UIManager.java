import javax.swing.*;
import java.awt.*;

public class UIManager {
    public static JPanel createTopPanel(JLabel timerLabel, JLabel flagLabel, JLabel statusLabel, ImageIcon icon) {
        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel leftInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        timerLabel.setText("Time: 0");
        flagLabel.setText("Flags: 0");

        if (icon != null) {
            JLabel imageLabel = new JLabel(icon);
            leftInfoPanel.add(imageLabel);
            leftInfoPanel.add(Box.createHorizontalStrut(10));
        }

        leftInfoPanel.add(timerLabel);
        leftInfoPanel.add(Box.createHorizontalStrut(20));
        leftInfoPanel.add(flagLabel);

        JPanel rightInfoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusLabel.setText("Ready to play!");
        statusLabel.setForeground(new Color(0, 128, 0));
        statusLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 12));
        rightInfoPanel.add(statusLabel);

        topPanel.add(leftInfoPanel, BorderLayout.WEST);
        topPanel.add(rightInfoPanel, BorderLayout.EAST);
        return topPanel;
    }

    public static JPanel createBottomPanel(JButton restartButton, ImageIcon icon) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (icon != null) {
            bottomPanel.add(new JLabel(icon));
            bottomPanel.add(Box.createHorizontalStrut(10));
        }

        bottomPanel.add(restartButton);

        if (icon != null) {
            bottomPanel.add(Box.createHorizontalStrut(10));
            bottomPanel.add(new JLabel(icon));
        }

        return bottomPanel;
    }
}
