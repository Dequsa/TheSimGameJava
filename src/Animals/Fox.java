package Animals;

import BaseClasses.Organism;
import BaseClasses.SpecialAnimal;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;

import java.awt.*;

public class Fox extends SpecialAnimal {

    private boolean moving = false;

    public Fox(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        final int FOX_STR = 5;
        final int FOX_INIT = 7;
        final Color FOX_COLOR = Color.ORANGE;
        final int  FOX_MOVESPEED = 1;

        data = new OrganismData(Types.FOX, FOX_STR, FOX_INIT, FOX_MOVESPEED, FOX_COLOR);
    }

    private boolean avoidEnemy(Organism other) {
        Vec2 safePosition = findSafeSpot();

        // can't escape
        if  (safePosition == null) {
            return false;
        }

        super.move(safePosition);

        System.out.println(this + " has avoided an enemy: " + other);

        return true;
    }

    @Override
    public boolean specialAbilityCheck(Organism other) {
        if (isMoving()) {
            // is moving means its attacking
            return other.getData().str() > data.str();
        }

        // it's not moving so it's defending
        return false;
    }

    @Override
    public boolean specialAbility(Organism other) {
        return !avoidEnemy(other);
    }
}
