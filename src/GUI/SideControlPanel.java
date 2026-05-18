package GUI;

import WorldManager.WorldManager;

import javax.swing.*;
import java.awt.*;

public class SideControlPanel extends JPanel implements TextPrinter {
    private final JPanel logContainer;
    private WorldManager wm;
    public SideControlPanel() {
        this.setBackground(new Color(45, 45, 45));
        this.setLayout(new BorderLayout());

        logContainer = new JPanel();
        logContainer.setLayout(new BoxLayout(logContainer, BoxLayout.Y_AXIS));
        logContainer.setBackground(new Color(45, 45, 45)); // Match the background color

        addScrollPanel();
        addMenu();
    }

    private void addMenu() {
        JMenuBar menu = new JMenuBar();
        menu.setBackground(new Color(134, 134, 134));

        JButton saveButton = new JButton("Save Game");

        saveButton.addActionListener(e -> {
            wm.setRunning(false);
        });

        JButton turnButton = new JButton("Turn");

        turnButton.addActionListener(e -> {
            wm.setTurnRequested(true);
        });

        menu.add(saveButton);
        menu.add(turnButton);

        add(menu, BorderLayout.NORTH);

    }

    public void setWm(WorldManager wm) {
        this.wm = wm;
    }

    private void addScrollPanel() {
        JScrollPane scrollPane = new JScrollPane(logContainer);
        scrollPane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));

        scrollPane.getVerticalScrollBar().setBackground(new Color(45, 45, 45));

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public void print(String str) {
        JLabel text = new JLabel(str);
        text.setForeground(Color.LIGHT_GRAY);
        text.setFont(new Font("Monospaced", Font.PLAIN, 12));
        text.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        logContainer.add(text);

        text.scrollRectToVisible(new Rectangle(0, text.getY(), 1, 1));

        logContainer.revalidate();
        logContainer.repaint();
    }

    @Override
    public void clear() {
        logContainer.removeAll();
        logContainer.revalidate();
        logContainer.repaint();
    }
}