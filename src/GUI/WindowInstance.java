package GUI;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

public class WindowInstance {
    private final JFrame frame;

    public WindowInstance(int width, int height) {
        String WINDOW_NAME = "Simulation Game";

        frame = new JFrame(WINDOW_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(width, height));
        frame.setResizable(true);
        frame.setLocationRelativeTo(null);
        frame.setUndecorated(false);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.requestFocus();
    }

    public void refresh() {
        frame.repaint();
    }

    public void show() {
        frame.pack();
        frame.setVisible(true);
    }

    public void addComponent(JComponent component) {
        frame.getContentPane().add(component, BorderLayout.CENTER);
    }

    public void removeComponent(JComponent component) {
        frame.getContentPane().remove(component);
    }

    public void addKeyListener(KeyListener keyListener) {
        frame.addKeyListener(keyListener);
    }
}
