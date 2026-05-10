import BaseClasses.*;
import GUI.GridDisplay.HexagonPanel;
import GUI.GridDisplay.SquarePanel;
import GUI.WindowInstance;
import GUI.WorldPanel;
import Input.InputHandler;
import movementHandler.GridType;

public class Main {
  void main() {
    int defaultWinSizeX = 800;
    int defaultWinSizeY = 600;

    int radius = 20;
    int organismCount = 0;
    GridType gridType = GridType.SQUARE;

    WorldManager worldManager = new WorldManager(gridType,organismCount, radius);

    var window = new WindowInstance(defaultWinSizeX, defaultWinSizeY);

    var keyHandler = new InputHandler(worldManager);
    window.addKeyListener(keyHandler);

    WorldPanel worldPanel = null;

    switch (gridType) {
      case SQUARE ->  {
        worldPanel = new SquarePanel(worldManager, radius);
      }
      case HEXAGON ->  {
        worldPanel = new HexagonPanel(worldManager,radius);
      }
    }

    window.addComponent(worldPanel);

    window.show();

    while (worldManager.isRunning()) {
      if (worldManager.isTurnRequested()) {
        worldManager.Update();
        worldManager.setTurnRequested(false);
        window.refresh();
      }

      try {Thread.sleep(10);} catch (InterruptedException e) {}
    }
  }
}