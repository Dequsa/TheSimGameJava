package GUI.GridDisplay;

import BaseClasses.Organism;
import Structs.Vec2;
import WorldManager.WorldManager;
import GUI.WorldPanel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SquarePanel extends WorldPanel {

    public SquarePanel(WorldManager worldManager, int squareSize) {
        super(worldManager, squareSize);
        updatePreferredSize();

        movementHandler = new movementHandler.SquareMovement();
    }

    private void updatePreferredSize() {
        int totalWidth = (worldManager.getMapSizeX() * cellSize) + (margin * 2);
        int totalHeight = (worldManager.getMapSizeY() * cellSize) + (margin * 2);
        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
    }

    private Rectangle getSquareAtPosition(int x, int y) {
        int xPos = (x * cellSize) + margin;
        int yPos = (y * cellSize) + margin;
        return new Rectangle(xPos, yPos, cellSize, cellSize);
    }

    @Override
    protected void handlePlayerMovement(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        for(int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                Rectangle tile = getSquareAtPosition(x, y);

                handleRightMouseClick(tile, x, y, mouseX, mouseY);
            }
        }
    }

    protected void handleMouseClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();

        for(int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                Rectangle tile = getSquareAtPosition(x, y);

                if (decideCellAction(tile, x, y, mouseX, mouseY)) return;
            }
        }
    }


    @Override
    protected ComponentListener createComponentListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int currentWidth = getWidth();
                int currentHeight = getHeight();

                double maxSizeX = (currentWidth - (margin * 2.0)) / worldManager.getMapSizeX();
                double maxSizeY = (currentHeight - (margin * 2.0)) / worldManager.getMapSizeY();

                cellSize = (int) Math.min(maxSizeX, maxSizeY);
                repaint();
            }
        };
    }

    @Override
    protected void drawGrid(Graphics2D g2d) {
        var map = worldManager.getWorldMap();

        for (int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                Rectangle square = getSquareAtPosition(x, y);

                g2d.setColor(Color.GRAY);
                g2d.fill(square);
                g2d.setColor(Color.BLACK);
                g2d.draw(square);

                Organism o = map[x][y];
                if (o != null) {
                    g2d.setColor(o.getColor());
                    g2d.fill(square);
                    g2d.setColor(Color.BLACK);
                    g2d.draw(square);
                }
            }
        }
    }
}