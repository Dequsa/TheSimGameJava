package Animals;
import BaseClasses.Organism;
import BaseClasses.SpecialAnimal;
import Structs.*;
import movementHandler.GridType;

import java.awt.*;

public class Antelope extends SpecialAnimal {
    public Antelope(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        final int ANTELOPE_STR      =   6;
        final int ANTELOPE_INIT     =   9;
        final Color ANTELOPE_COLOR  =   Color.MAGENTA;
        final int ANTELOPE_MOVE_SPEED = 2;

        data = new OrganismData(Types.ANTELOPE, ANTELOPE_STR, ANTELOPE_INIT, ANTELOPE_MOVE_SPEED, ANTELOPE_COLOR);
    }

    private boolean avoidEnemy(Organism other) {
        Vec2 safePosition = findSafeSpot();

        // can't escape
        if  (safePosition == null) {
            return false;
        }

        super.move(safePosition);

        controller.printText(this + " has avoided an enemy: " + other);

        return true;
    }

    @Override
    public boolean specialAbilityCheck(Organism other) {
        if (!isMoving() && other.getData().str() >= data.str()) {
            var rand = new java.util.Random();
            return rand.nextBoolean();
        }
        return false;
    }

    @Override
    public boolean specialAbility(Organism other) {
        return !avoidEnemy(other);
    }
}
