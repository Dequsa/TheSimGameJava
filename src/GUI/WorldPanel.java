//package GUI;
//
//import BaseClasses.WorldManager;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class WorldPanel extends JPanel {
//    private final WorldManager worldManager;
//    private final int tileSize;
//
//    public WorldPanel(WorldManager worldManager, int tileSize) {
//        this.worldManager = worldManager;
//        this.tileSize = tileSize;
//
//        this.setPreferredSize(new Dimension(
//                worldManager.getMapSizeX() * tileSize,
//                worldManager.getMapSizeY() * tileSize));
//    }
//
//    public Polygon createHexagon(int centerX, int centerY, int radius) {
//        Polygon polygon = new Polygon();
//
//        for (int i = 0; i < 6; i++) {
//            // Calculate the angle for each point (starting at 0 for a "pointy-topped" hex)
//            double angle = Math.toRadians(60 * i);
//
//            int x = (int) (centerX + radius * Math.cos(angle));
//            int y = (int) (centerY + radius * Math.sin(angle));
//
//            polygon.addPoint(x, y);
//        }
//        return polygon;
//    }
//
////    @Override
////    public void paintComponent(Graphics g) {
////        super.paintComponent(g);
////        var map = worldManager.getWorldMap();
////
////        for (int x = 0; x < map.length; x++) {
////            for (int y = 0; y < map[0].length; y++) {
////                g.setColor(Color.GRAY);
////                g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
////
////                if (map[x][y] != null) {
////                    g.setColor(map[x][y].getColor());
////                    g.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
////                }
////            }
////        }
////    }
//
//    private void drawHexGrid(Graphics2D g2d) {
//        int radius = 30;
//        double hexWidth = Math.sqrt(3) * radius;
//        double hexHeight = 2.0 * radius;
//        var map = worldManager.getWorldMap();
//
//        for (int x = 0; x < worldManager.getMapSizeY(); x++) {
//            for (int y = 0; y < worldManager.getMapSizeX(); y++) {
//
//                // Calculate X with an offset for odd rows
//                double xPos = y * hexWidth;
//                if (x % 2 == 1) {
//                    xPos += hexWidth / 2.0;
//                }
//
//                // Calculate Y (rows are closer together than in a square grid)
//                double yPos = x * (radius * 1.5);
//
//                // Create and draw the hexagon
//                Polygon hex = createHexagon((int)xPos + 50, (int)yPos + 50, radius);
//
//                // Draw the tile base
//                g2d.setColor(Color.LIGHT_GRAY);
//                g2d.drawPolygon(hex);
//
//                if (map[x][y] != null) {
//                    g2d.setColor(map[x][y].getColor());
//                    g2d.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D g2d = (Graphics2D) g;
//
////        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        Polygon hex = createHexagon(100, 100, 50);
//
//        g2d.setColor(Color.GRAY);
//        g2d.fillPolygon(hex);
//
//        g2d.setColor(Color.BLACK);
//        g2d.drawPolygon(hex);
//    }
//
//
//}

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
                if (y % 2 == 1) {
                    xPos += hexWidth / 2.0;
                }

                // hex vertical spacing
                double yPos = y * (radius * 1.5);


                Polygon hex = createHexagon((int)xPos + radius, (int)yPos + radius, radius);

                g2d.setColor(new Color(60, 60, 60));
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