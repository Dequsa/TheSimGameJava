package Input;

import WorldManager.WorldManager;
import Structs.Direction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    private transient WorldManager worldManager;

    public InputHandler(WorldManager worldManager) {
        this.worldManager = worldManager;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Q -> worldManager.setRunning(false);
//            case KeyEvent.VK_W -> worldManager.setNextMoveDirection(Direction.UP);
//            case KeyEvent.VK_E -> worldManager.setNextMoveDirection(Direction.UP_RIGHT);
//            case KeyEvent.VK_A -> worldManager.setNextMoveDirection(Direction.LEFT);
//            case KeyEvent.VK_SPACE -> worldManager.setNextMoveDirection(Direction.NONE);
//            case KeyEvent.VK_D -> worldManager.setNextMoveDirection(Direction.RIGHT);
//            case KeyEvent.VK_Z -> worldManager.setNextMoveDirection(Direction.DOWN_LEFT);
//            case KeyEvent.VK_X -> worldManager.setNextMoveDirection(Direction.DOWN);
//            case KeyEvent.VK_C -> worldManager.setNextMoveDirection(Direction.DOWN_RIGHT);
//            case KeyEvent.VK_F -> worldManager.setNextMoveDirection(Direction.SPECIAL);
        }
        worldManager.setTurnRequested(true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}
