package Animals.Human;

import BaseClasses.SpecialAnimal;
import Structs.*;
import movementHandler.GridType;

import java.awt.*;

public class Human extends SpecialAnimal {
    private Direction nextDir = Direction.NONE;

    public Human(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        controllable = true;
        final int HUMAN_STR = 6;
        final int HUMAN_INIT = 9;
        final Color HUMAN_COLOR = Color.WHITE;
        final int HUMAN_MOVE_SPEED = 1;

        data = new OrganismData(Types.HUMAN, HUMAN_STR, HUMAN_INIT, HUMAN_MOVE_SPEED, HUMAN_COLOR);
    }

    @Override
    public void setMoveDirection(Direction dir) {
        nextDir = dir;
    }

    @Override
    protected Vec2 determineMove() {     /* after this override it's not random anymore*/
        if (nextDir == Direction.SPECIAL ||  nextDir == Direction.NONE) {
            return new Vec2(0, 0);
        }
        Vec2 []possibleMoves = movementHandler.getValidMoves(getPosition().y(), data.moveSpeed());
        return possibleMoves[nextDir.ordinal()];
    }
}
