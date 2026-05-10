package Animals;
import BaseClasses.Organism;
import BaseClasses.SpecialAnimal;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;

import java.awt.*;

public class Antelope extends SpecialAnimal {
    public Antelope(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        final int ANTELOPE_STR      =   6;
        final int ANTELOPE_INIT     =   9;
        final Color ANTELOPE_COLOR  =   Color.MAGENTA;
        final int  ANTELOPE_MOVESPEED = 2;

        data = new OrganismData(Types.ANTELOPE, ANTELOPE_STR, ANTELOPE_INIT, ANTELOPE_MOVESPEED, ANTELOPE_COLOR);
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
        // activate only if the antelope is defending
        return !isMoving();
    }

    @Override
    public boolean specialAbility(Organism other) {
        return !avoidEnemy(other);
    }
}
