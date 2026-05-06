package GUI;

import BaseClasses.Organism;
import BaseClasses.WorldManager;
import Structs.Types;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WorldPanel extends JPanel {
    private final WorldManager worldManager;
    private final int radius;

    private Polygon getHexagonAtPosition(int x, int y) {
        double hexWidth = Math.sqrt(3) * radius;
        double vertDist = radius * 1.5;
        int margin = 20;

        double xPos = x * hexWidth;
        if (y % 2 == 1) {
            xPos += hexWidth / 2.0;
        }
        double yPos = y * vertDist;

        int centerX = (int)(xPos + (hexWidth / 2.0)) + margin;
        int centerY = (int)(yPos + radius) + margin;

        return createHexagon(centerX, centerY, radius);
    }

    private void showSpawnMenu(int gX, int gY, int pX , int pY) {
        JPopupMenu popup = new JPopupMenu();

        for (var type : Types.values()) {
            JMenuItem item = new JMenuItem(type.toString());
            item.addActionListener(e -> {
                worldManager.handleMouseClick(gX, gY, type);
                repaint();
            });

            popup.add(item);
        }

        popup.show(this, pX, pY);
    }

    private MouseAdapter createMouseListener() {
        var m = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                for(int x = 0; x < worldManager.getMapSizeX(); x++) {
                    for (int y = 0; y < worldManager.getMapSizeY(); y++) {
                        Polygon hex = getHexagonAtPosition(x, y);

                        if (hex.contains(mouseX, mouseY)) {
                            var map = worldManager.getWorldMap();
                            var target = map[x][y];

                            if (target == null) {
                                // x amd y are the grid coords on the matrix
                                showSpawnMenu(x, y, mouseX, mouseY);
                                return;
                            }

                            worldManager.handleMouseClick(x, y, Types.NONE);
                            repaint();
                            return;
                        }
                    }
                }
            }
        };
        return m;
    }

    public WorldPanel(WorldManager worldManager, int radius) {
        this.worldManager = worldManager;
        this.radius = radius;
        this.addMouseListener(createMouseListener());

        // hexagon math
        double hexWidth = Math.sqrt(3) * radius;
        double hexHeight = radius * 1.5;

        int totalWidth = (int) (worldManager.getMapSizeX() * hexWidth + (hexWidth / 2.0));
        int totalHeight = (int) (worldManager.getMapSizeY() * hexHeight + (radius * 0.5));

        this.setPreferredSize(new Dimension(totalWidth, totalHeight));
        this.setBackground(Color.DARK_GRAY);
    }

    public Polygon createHexagon(int centerX, int centerY, int radius) {
        Polygon polygon = new Polygon();
        for (int i = 0; i < 6; i++) {

            // we need to make it pointy
            // some math to make the hexagon
            double angle = Math.toRadians(60 * i + 30);
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            polygon.addPoint(x, y);
        }
        return polygon;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // line smoothing with antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawHexGrid(g2d);
    }

    private void drawHexGrid(Graphics2D g2d) {
        var map = worldManager.getWorldMap();

        for (int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {

                Polygon hex = getHexagonAtPosition(x, y);

                g2d.setColor(Color.DARK_GRAY);
                g2d.fillPolygon(hex);
                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(hex);

                // draw organism if present
                Organism o = map[x][y];
                if (o != null) {
                    g2d.setColor(o.getColor());
                    g2d.fillPolygon(hex);
                    g2d.setColor(Color.WHITE);
                    g2d.drawPolygon(hex);
                }
            }
        }
    }
}