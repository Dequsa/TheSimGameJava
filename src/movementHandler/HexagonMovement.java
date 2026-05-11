package movementHandler;

import Structs.Vec2;

public class HexagonMovement implements movementType {
    @Override
    public Vec2[] getValidMoves(int y, int speed) {
        Vec2[] validMoves;
        if (y % 2 == 0) {
            validMoves = new Vec2[]{
                    new Vec2(-speed, -speed), // Up Left
                    new Vec2(0, -speed),  // Up Right
                    new Vec2(-speed, 0),  // Left
                    new Vec2(speed, 0),   // Right
                    new Vec2(-speed, speed),  // Down Left
                    new Vec2(0, speed),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        } else {
            validMoves = new Vec2[]{
                    new Vec2(0, -speed),  // Up Left
                    new Vec2(speed, -speed),  // Up Right
                    new Vec2(-speed, 0),  // Left
                    new Vec2(speed, 0),   // Right
                    new Vec2(0, speed),   // Down Left
                    new Vec2(speed, speed),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        }
        return validMoves;
    }
}
