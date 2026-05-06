package Input;

import BaseClasses.WorldManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    WorldManager worldManager;

    public InputHandler(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_ENTER: {
                worldManager.setTurnRequested(true);
                System.out.println("Enter pressed");
                break;
            }
            case KeyEvent.VK_ESCAPE: {
                // close the program
                worldManager.setRunning(false);
                System.out.println("Escape pressed");
                System.exit(0);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
