package GUI;

import BaseClasses.Organism;
import BaseClasses.WorldManager;
import javax.swing.*;
import java.awt.*;

public class WorldPanel extends JPanel {
    private final WorldManager worldManager;
    private final int radius;

    public WorldPanel(WorldManager worldManager, int radius) {
        this.worldManager = worldManager;
        this.radius = radius;

        // hexagon math
        double hexWidth = Math.sqrt(3) * radius;
        int totalWidth = (int) (worldManager.getMapSizeX() * hexWidth + (hexWidth / 2.0));
        int totalHeight = (int) (worldManager.getMapSizeY() * (radius * 1.5) + (radius * 0.5));

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
        double hexWidth = Math.sqrt(3) * radius;
        var map = worldManager.getWorldMap();

        for (int x = 0; x < worldManager.getMapSizeX(); x++) {
            for (int y = 0; y < worldManager.getMapSizeY(); y++) {

                // pixel rotation
                double xPos = x * hexWidth;

                // offset row
                // for every odd row add offset to compensate hexagonal figure
                if (y % 2 == 1) {
                    xPos += hexWidth / 2.0;
                }

                // space hexagons so they don't overlap in vertical manner
                double yPos = y * (radius * 1.5);


                Polygon hex = createHexagon((int)xPos + radius, (int)yPos + radius, radius);

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