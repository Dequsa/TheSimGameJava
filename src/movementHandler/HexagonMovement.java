package movementHandler;

import Structs.Vec2;

public class HexagonMovement implements movementType {
    @Override
    public Vec2[] getMultiStep(int currentRowY, int speed) {
        if (speed <= 1) {
            return getSingleStep(currentRowY);
        }

        Vec2[] singleSteps = getSingleStep(currentRowY);
        Vec2[] extendedMoves = new Vec2[singleSteps.length];

        // no move is always the last step and doesnt change
        extendedMoves[singleSteps.length - 1] = new Vec2(0, 0);

        // check all possible directions
        for (int i = 0; i < 6; i++) {
            Vec2 baseDir = singleSteps[i];

            int currentX = baseDir.x();
            int currentY = currentRowY + baseDir.y();

            // simulate move in that direction
            for (int step = 1; step < speed; step++) {
                Vec2[] nextPossibleSteps = getSingleStep(currentY);
                Vec2 nextStep = nextPossibleSteps[i];

                currentX += nextStep.x();
                currentY += nextStep.y();
            }

            extendedMoves[i] = new Vec2(currentX, currentY - currentRowY); // currentY - currentRowY is the offset not the actual y
        }

        return extendedMoves;
    }

    @Override
    public Vec2[] getSingleStep(int y) {
        Vec2[] validMoves;
        if (y % 2 == 0) {
            validMoves = new Vec2[]{
                    new Vec2(-1, -1), // Up Left
                    new Vec2(0, -1),  // Up Right
                    new Vec2(-1, 0),  // Left
                    new Vec2(1, 0),   // Right
                    new Vec2(-1, 1),  // Down Left
                    new Vec2(0, 1),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        } else {
            validMoves = new Vec2[]{
                    new Vec2(0, -1),  // Up Left
                    new Vec2(1, -1),  // Up Right
                    new Vec2(-1, 0),  // Left
                    new Vec2(1, 0),   // Right
                    new Vec2(0, 1),   // Down Left
                    new Vec2(1, 1),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        }
        return validMoves;
    }
}
