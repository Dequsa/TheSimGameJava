package GUI.GridDisplay;

import BaseClasses.Organism;
import WorldManager.WorldManager;
import GUI.WorldPanel;
import Structs.Types;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HexagonPanel extends WorldPanel {

    public HexagonPanel(WorldManager worldManager, int radius) {
        super(worldManager, radius);
        updatePreferredSize();
    }

    private void updatePreferredSize() {
        double hexWidth = Math.sqrt(3) * cellSize;
        double hexHeight = cellSize * 1.5;

        int totalWidth = (int) (worldManager.getMapSizeX() * hexWidth + (hexWidth / 2.0)) + (margin * 2);
        int totalHeight = (int) (worldManager.getMapSizeY() * hexHeight + (cellSize * 0.5)) + (margin * 2);

        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
    }

    private Polygon getHexagonAtPosition(int x, int y) {
        double hexWidth = Math.sqrt(3) * cellSize;
        double vertDist = cellSize * 1.5;

        double xPos = x * hexWidth;
        if (y % 2 == 1) {
            xPos += hexWidth / 2.0;
        }
        double yPos = y * vertDist;

        int centerX = (int)(xPos + (hexWidth / 2.0)) + margin;
        int centerY = (int)(yPos + cellSize) + margin;

        return createHexagon(centerX, centerY, cellSize);
    }

    private Polygon createHexagon(int centerX, int centerY, int radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = Math.toRadians(60 * i + 30);
            int pX = (int) (centerX + radius * Math.cos(angle));
            int pY = (int) (centerY + radius * Math.sin(angle));
            polygon.addPoint(pX, pY);
        }
        return polygon;
    }

    @Override
    protected MouseAdapter createMouseListener() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                for(int x = 0; x < worldManager.getMapSizeX(); x++) {
                    for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                        Polygon hex = getHexagonAtPosition(x, y);

                        if (hex.contains(mouseX, mouseY)) {
                            var map = worldManager.getWorldMap();
                            if (map[x][y] == null) {
                                showSpawnMenu(x, y, mouseX, mouseY);
                            } else {
                                worldManager.handleMouseClick(x, y, Types.NONE);
                                repaint();
                            }
                            return;
                        }
                    }
                }
            }
        };
    }

    @Override
    protected ComponentListener createComponentListener() {
        return new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int currentWidth = getWidth();
                int currentHeight = getHeight();

                double maxRadiusX = (currentWidth - (margin * 2)) / (worldManager.getMapSizeX() * Math.sqrt(3));
                double maxRadiusY = (currentHeight - (margin * 2)) / (worldManager.getMapSizeY() * 1.5);

                cellSize = (int) Math.min(maxRadiusX, maxRadiusY);
                repaint();
            }
        };
    }

    @Override
    protected void drawGrid(Graphics2D g2d) {
        var map = worldManager.getWorldMap();

        for (int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                Polygon hex = getHexagonAtPosition(x, y);

                g2d.setColor(Color.GRAY);
                g2d.fillPolygon(hex);
                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(hex);

                Organism o = map[x][y];
                if (o != null) {
                    g2d.setColor(o.getColor());
                    g2d.fillPolygon(hex);
                    g2d.setColor(Color.BLACK);
                    g2d.drawPolygon(hex);
                }
            }
        }
    }
}