import GUI.GridDisplay.HexagonPanel;
import GUI.GridDisplay.SquarePanel;
import GUI.WindowInstance;
import GUI.WorldPanel;
import Input.InputHandler;
import Tools.ObjectManager;
import WorldManager.WorldManager;
import menu.MainMenu;
import movementHandler.GridType;

public class Main {
  public static void main(String[] args) {
    int defaultWinSizeX = 800;
    int defaultWinSizeY = 600;
    var window = new WindowInstance(defaultWinSizeX, defaultWinSizeY);
    MainMenu mainMenu = new MainMenu(window);
    final String savePath = "save.dat";

    window.addComponent(mainMenu);
    window.show();

    WorldManager worldManager = null;

    while (worldManager == null) {
      worldManager = mainMenu.getWorldManager();

      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
    }

    ObjectManager objectManager = mainMenu.getObjectManager();

    while (worldManager.isRunning()) {
      if (worldManager.isTurnRequested()) {
        worldManager.Update();
        worldManager.setTurnRequested(false);
        window.refresh();
      }

      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
    }

    objectManager.saveGameState(savePath, worldManager);

    System.exit(0);
  }
}