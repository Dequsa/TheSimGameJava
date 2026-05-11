package movementHandler;

import Structs.Vec2;

public interface movementType {
    Vec2[] getValidMoves(int y, int speed);
}
