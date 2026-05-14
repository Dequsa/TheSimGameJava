package movementHandler;

import Structs.Vec2;

public class SquareMovement implements movementType {
    @Override
    public Vec2[] getSingleStep(int y) {
        return getMultiStep(y, 1);
    }

    @Override
    public Vec2[] getMultiStep(int y, int speed) {
        return new Vec2[] {
                new Vec2(-speed, -speed),
                new Vec2(0, -speed),
                new Vec2(speed, -speed),
                new Vec2(-speed, 0),
                new Vec2(speed, 0),
                new Vec2(-speed, speed),
                new Vec2(0, speed),
                new Vec2(speed, speed),
                new Vec2(0, 0)
        };
    }
}
