package GUI;

import WorldManager.WorldManager;
import Structs.Types;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;

public abstract class WorldPanel extends JPanel {
    protected final WorldManager worldManager;
    protected int cellSize;
    protected final int margin = 20;

    public WorldPanel(WorldManager worldManager, int cellSize) {
        this.worldManager = worldManager;
        this.cellSize = cellSize;
        this.setBackground(Color.DARK_GRAY);

        this.addMouseListener(createMouseListener());
        this.addComponentListener(createComponentListener());
    }

    protected void showSpawnMenu(int gX, int gY, int pX , int pY) {
        JPopupMenu popup = new JPopupMenu();

        for (var type : Types.values()) {
            if (type == Types.NONE) continue;

            JMenuItem item = new JMenuItem(type.toString());
            item.addActionListener(e -> {
                worldManager.handleMouseClick(gX, gY, type);
                repaint();
            });

            popup.add(item);
        }

        popup.show(this, pX, pY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // line smoothing with antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
    }

    protected abstract MouseAdapter createMouseListener();
    protected abstract ComponentListener createComponentListener();
    protected abstract void drawGrid(Graphics2D g2d);
}