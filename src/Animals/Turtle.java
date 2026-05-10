package Animals;
import BaseClasses.Organism;
import BaseClasses.SpecialAnimal;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;

import java.awt.*;

public class Turtle extends SpecialAnimal {

    public Turtle(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        final int TURTLE_STR = 2;
        final int TURTLE_INIT = 2;
        final Color TURTLE_COLOR = Color.GREEN;
        final int TURTLE_MOVESPEED = 1;

        data = new OrganismData(Types.TURTLE,TURTLE_STR, TURTLE_INIT, TURTLE_MOVESPEED, TURTLE_COLOR);
    }

    @Override
    protected Vec2 getRandomMoveVec() {
        var rand = new java.util.Random();
        double CHANCE_TO_MOVE = 0.2;

        if (rand.nextInt(10) >= (int)(CHANCE_TO_MOVE * 10)) {
            return new Vec2(0, 0);
        }

        return super.getRandomMoveVec();
    }

    @Override
    public boolean specialAbilityCheck(Organism other) {
        if (!isMoving()) {
            int MIN_DEFLECT_STR = 5;
            return other.getData().str() <= MIN_DEFLECT_STR;
        }

        return false;
    }

    @Override
    public boolean specialAbility(Organism other) {
        System.out.println(this + " has deflected: " + other);
        return true;
    }
}
