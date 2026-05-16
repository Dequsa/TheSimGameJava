package movementHandler;

import Structs.Vec2;

import java.io.Serializable;

public interface movementType extends Serializable {
    Vec2[] getSingleStep(int y);

    Vec2[] getMultiStep(int currentRowY, int speed);
}
