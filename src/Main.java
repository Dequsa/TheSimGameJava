import BaseClasses.*;
import GUI.WindowInstance;
import GUI.WorldPanel;
import Input.InputHandler;

public class Main {
  void main() {
    int defaultWinSizeX = 800;
    int defaultWinSizeY = 600;

    int radius = 20;
    int organismCount = 10;

    WorldManager worldManager = new WorldManager(organismCount, radius);

    var window = new WindowInstance(defaultWinSizeX, defaultWinSizeY);

    var keyHandler = new InputHandler(worldManager);
    window.addKeyListener(keyHandler);

    var worldPanel = new WorldPanel(worldManager, radius);

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