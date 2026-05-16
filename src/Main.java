import GUI.GridDisplay.HexagonPanel;
import GUI.GridDisplay.SquarePanel;
import GUI.WindowInstance;
import GUI.WorldPanel;
import Input.InputHandler;
import Tools.ObjectManager;
import WorldManager.WorldManager;
import movementHandler.GridType;

public class Main {
  void main() {
    int defaultWinSizeX = 800;
    int defaultWinSizeY = 600;

    int radius = 20;
    int organismCount = 0;
    GridType gridType = GridType.HEXAGON;
    final String savePath = "save.dat";

    ObjectManager manager = new ObjectManager();

//    WorldManager worldManager = new WorldManager(gridType,organismCount, radius);

    WorldManager worldManager = (WorldManager) manager.loadGameState(savePath);
    worldManager.setRunning(true);

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
    manager.saveGameState(savePath, worldManager);

    System.exit(0);
  }
}