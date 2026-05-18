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

    @Override
    public abstract Dimension getPreferredSize();

    protected abstract Shape getTileAtPosition(int x, int y);

    protected abstract void drawGrid(Graphics2D g2d);

    protected abstract ComponentListener createComponentListener();

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // line smoothing with antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2d);
    }

    private Vec2 getMousePosition(MouseEvent e) {
        return new Vec2(e.getX(), e.getY());
    }

    protected void decideCellAction(Vec2 tilePos, Vec2 mousePos) {
        var map = worldManager.getWorldMap();
        if (worldManager.isOutOfBounds(tilePos)) return;
        if (map[tilePos.x()][tilePos.y()] == null) {
            showSpawnMenu(tilePos, mousePos);
        } else {
            worldManager.handleTileAction(tilePos, Types.NONE);
            repaint();
        }
    }

    protected void handleMouseClick(MouseEvent e) {
        Vec2 tilePosition = getMouseTilePosition(e);
        Vec2 mousePosition = getMousePosition(e);
        if (tilePosition == null) return;

        decideCellAction(tilePosition, mousePosition);
    }

    protected void handlePlayerMovement(MouseEvent e) {
        Vec2 tilePosition = getMouseTilePosition(e);
        if (tilePosition == null) return;

        handleRightMouseClick(tilePosition.x(), tilePosition.y());
    }

    protected Vec2 getMouseTilePosition(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        for (int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                Shape tile = getTileAtPosition(x, y);

                if (tile.contains(mouseX, mouseY)) {
                    return new Vec2(x, y);
                }
            }
        }
        return null;
    }

    protected void handleRightMouseClick(int tileX, int tileY) {
        ArrayList<Organism> organisms = worldManager.getContollableOrganisms();
        if (organisms.isEmpty()) return;

        Vec2 tilePosition = new Vec2(tileX, tileY);

        for (var org : organisms) {
            Vec2[] possibleOffsets = movementHandler.getMultiStep(org.getPosition().y(), org.getData().moveSpeed());
            if (tilePosition.isNeighboring(org.getPosition(), possibleOffsets)) {
                var moveVec = tilePosition.subtract(org.getPosition());
                org.setMoveVector(moveVec);
            }
        }
    }

    protected void showSpawnMenu(Vec2 gPos, Vec2 pPos) {
        JPopupMenu popup = new JPopupMenu();
        for (var type : Types.values()) {
            if (type == Types.NONE) continue;

            JMenuItem item = new JMenuItem(type.toString());
            item.addActionListener(e -> {
                worldManager.handleTileAction(gPos, type);
                repaint();
            });
            popup.add(item);
        }
        popup.show(this, pPos.x(), pPos.y());
    }

    private void showAndChooseItemFromMenu(Organism org, int pX, int pY) {
        JPopupMenu popup = new JPopupMenu();
        for (var it : org.getSpecialItems()) {
            if (it == null) continue;

            JMenuItem item = new JMenuItem(it.toString());
            item.addActionListener(e -> {
                org.useSpecialItem(it);
                repaint();
            });
            popup.add(item);
        }
        popup.show(this, pX, pY);
    }

    private void handleSpecialAbilityClick(MouseEvent e) {
        Vec2 tilePosition = getMouseTilePosition(e);
        if (tilePosition == null) return;

        var org = worldManager.getOrganismAt(tilePosition);
        if (org == null) return;

        Vec2 mousePosition = getMousePosition(e);
        showAndChooseItemFromMenu(org, mousePosition.x(), mousePosition.y());
    }

    protected MouseAdapter createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1 -> handleMouseClick(e);             /* left-click */
                    case MouseEvent.BUTTON3 -> handlePlayerMovement(e);         /* right-click */
                    case MouseEvent.BUTTON2 -> handleSpecialAbilityClick(e);    /* middle-click */
                }
//                worldManager.setTurnRequested(true);
            }
        };
    }
}