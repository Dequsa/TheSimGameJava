package menu;

import Animals.Wolf;
import GUI.GridDisplay.HexagonPanel;
import GUI.GridDisplay.SquarePanel;
import GUI.WindowInstance;
import GUI.WorldPanel;
import Tools.ObjectManager;
import WorldManager.WorldManager;
import movementHandler.GridType;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JPanel {
    private final int BORDER_SIZE = 20;
    private final String savePath = "save.dat";

    private int tileSize = 20;
    private int mapSize = 10;
    private int organismCount = 10;
    private GridType gridType = GridType.SQUARE;

    private WorldManager wm = null;
    private WorldPanel wp = null;
    private final ObjectManager om = new ObjectManager();
    private final WindowInstance window;

    public MainMenu(WindowInstance window) {
        this.window = window;
        this.setBackground(Color.DARK_GRAY);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE));

        createButtons();
    }

    private void loadSavedGame() {
        wm = (WorldManager) om.loadGameState(savePath);
        if (wm != null) {
            this.gridType = wm.getGridType();
            wp = chooseWorldPanel();
            launchActiveGamePanel();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to load state or save file missing.", "Load Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private WorldPanel chooseWorldPanel() {
        switch (gridType) {
            case SQUARE -> {
                return new SquarePanel(wm, tileSize);
            }
            case HEXAGON -> {
                return new HexagonPanel(wm, tileSize);
            }
            default -> {
                return null;
            }
        }
    }

    private void launchActiveGamePanel() {
        if (wp == null) return;

        window.removeComponent(this);
        wm.setPrinter(window.launchGame(wp, wm));
        window.refresh();
    }

    private void initAndStartNewGame() {
        wm = new WorldManager(gridType, organismCount, mapSize);
        wp = chooseWorldPanel();
        launchActiveGamePanel();
    }

    private void showGameConfigDialog() {
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));

        JTextField tileSizeText = new JTextField(String.valueOf(tileSize));
        JTextField mapSizeText = new JTextField(String.valueOf(mapSize));
        JTextField organismCountText = new JTextField(String.valueOf(organismCount));
        JComboBox<GridType> gridMenu = new JComboBox<>(GridType.values());
        gridMenu.setSelectedItem(gridType);

        form.add(new JLabel("Tile Size (px):"));
        form.add(tileSizeText);
        form.add(new JLabel("Map Dimension:"));
        form.add(mapSizeText);
        form.add(new JLabel("Initial Organisms:"));
        form.add(organismCountText);
        form.add(new JLabel("Grid Geometry:"));
        form.add(gridMenu);

        int result = JOptionPane.showConfirmDialog(this, form,
                "Configure New Simulation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                this.tileSize = Integer.parseInt(tileSizeText.getText().trim());
                this.mapSize = Integer.parseInt(mapSizeText.getText().trim());
                this.organismCount = Integer.parseInt(organismCountText.getText().trim());
                this.gridType = (GridType) gridMenu.getSelectedItem();

                if (tileSize <= 0 || mapSize <= 0 || organismCount <= 0) {
                    JOptionPane.showMessageDialog(this, "All values must be greater than 0!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    showGameConfigDialog();
                    return;
                }
                initAndStartNewGame();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter numeric values only.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                showGameConfigDialog();
            }
        }
    }

    private void createButtons() {
        JButton loadBtn = new JButton("Load");
        JButton newBtn = new JButton("New");

        loadBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        newBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createVerticalGlue());
        this.add(newBtn);
        this.add(Box.createVerticalStrut(15));
        this.add(loadBtn);
        this.add(Box.createVerticalGlue());

        loadBtn.addActionListener(e -> {
            loadSavedGame();
        });

        newBtn.addActionListener(e -> {
            showGameConfigDialog();
        });
    }

    public WorldManager getWorldManager() {
        return wm;
    }

    public ObjectManager getObjectManager() {
        return om;
    }
}
