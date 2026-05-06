import BaseClasses.*;
import GUI.WindowInstance;
import GUI.WorldPanel;
import Input.InputHandler;

import java.awt.*;

public class Main {
  void main() {
    int tileSize = 30;
    int mapSize = 20;
    int organismCount = 10;
    int buff = 50;

    WorldManager worldManager = new WorldManager(organismCount, mapSize);
    var window = new WindowInstance(mapSize * tileSize + buff, mapSize * tileSize + buff);
    var keyHandler = new InputHandler(worldManager);
    window.addKeyListener(keyHandler);
    var worldPanel = new WorldPanel(worldManager, tileSize);

    window.addComponent(worldPanel);
    window.getFrame().pack();
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