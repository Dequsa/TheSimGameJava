package movementHandler;

import Structs.Vec2;

public interface movementType {
    Vec2[] getSingleStep(int y);

    Vec2[] getMultiStep(int currentRowY, int speed);
}
