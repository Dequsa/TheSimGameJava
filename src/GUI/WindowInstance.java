package GUI;

import WorldManager.WorldManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class WindowInstance {
    private final JFrame frame;
    private WorldManager wm;
    private final SideControlPanel sidePanel = new SideControlPanel();

    public WindowInstance() {
        String WINDOW_NAME = "Simulation Game";

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(screenSize.width / 2, screenSize.height / 2));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(false);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();
    }

    public TextPrinter launchGame(WorldPanel gameGridPanel, WorldManager wm) {
        frame.getContentPane().removeAll();

        frame.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // grid map settings
        gbc.gridx = 0;
        gbc.weightx = 0.6;
        frame.getContentPane().add(gameGridPanel, gbc);


        sidePanel.setWm(wm);

        // side panel settings
        gbc.gridx = 1;
        gbc.weightx = 0.4;
        frame.getContentPane().add(sidePanel, gbc);

        frame.revalidate();
        frame.pack();
        frame.setLocationRelativeTo(null);

        return sidePanel;
    }

    public void refresh() {
        frame.repaint();
        sidePanel.clear();
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    public void addComponent(JComponent component) {
        frame.getContentPane().add(component);
    }

    public void removeComponent(JComponent component) {
        frame.getContentPane().remove(component);
    }

    public void addKeyListener(KeyListener keyListener) {
        frame.addKeyListener(keyListener);
    }

    public JFrame getFrame() { return frame;}
}