package GUI;

import BaseClasses.Organism;
import Structs.Vec2;
import WorldManager.WorldManager;
import Structs.Types;
import movementHandler.movementType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public abstract class WorldPanel extends JPanel {
    protected final WorldManager worldManager;
    protected int cellSize;
    protected final int margin = 20;
    protected movementType movementHandler = null;

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
                worldManager.handleTileAction(gX, gY, type);
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

    protected boolean decideCellAction(Shape tile, int tileX, int tileY, int mouseX, int mouseY) {
        if (tile.contains(mouseX, mouseY)) {
            var map = worldManager.getWorldMap();
            if (map[tileX][tileY] == null) {
                showSpawnMenu(tileX, tileY, mouseX, mouseY);
            } else {
                worldManager.handleTileAction(tileX, tileY, Types.NONE);
                repaint();
            }
            return true;
        }
        return false;
    }

    protected abstract void handleMouseClick(MouseEvent e);

    protected void handleRightMouseClick(Shape tile, int x, int y, int mouseX, int mouseY) {
        if (tile.contains(mouseX, mouseY)) {
            ArrayList<Organism> organisms = worldManager.getContollableOrganisms();

            if (organisms.isEmpty()) return;

            Vec2 tilePosition = new Vec2(x, y);

            for (var org : organisms) {
                Vec2 []possibleOffsets = movementHandler.getMultiStep(org.getPosition().y(), org.getData().moveSpeed());
                if (tilePosition.isNeighboring(org.getPosition(), possibleOffsets)) {
                    var moveVec = tilePosition.subtract(org.getPosition());
                    org.setMoveVector(moveVec);
                }
            }
        }
    }

    protected abstract void handlePlayerMovement(MouseEvent e);

    protected MouseAdapter createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    handleMouseClick(e);
                    return;
                } else {
                    handlePlayerMovement(e);
                }
                worldManager.setTurnRequested(true);
            }
        };
    }

    protected abstract ComponentListener createComponentListener();
    protected abstract void drawGrid(Graphics2D g2d);
}