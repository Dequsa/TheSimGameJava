package Animals.Human;

import BaseClasses.SpecialAnimal;
import Structs.*;
import movementHandler.GridType;

import java.awt.*;

public class Human extends SpecialAnimal {
    private Vec2 moveVec = null;

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
    public void setMoveVector(Vec2 moveVec) {
         this.moveVec = moveVec;
    }

    @Override
    protected Vec2 determineMove() {
        if (moveVec == null) {
            return new Vec2(0, 0);
        }

        return moveVec;
    }

    @Override
    public void update() {
        super.update();
        moveVec = null;
    }
}
