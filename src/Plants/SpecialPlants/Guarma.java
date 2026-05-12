package Plants.SpecialPlants;

import BaseClasses.Organism;
import BaseClasses.SpecialPlant;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;

import java.awt.*;

public class Guarma extends SpecialPlant {

    final int STR_ADD = 5;
    final Color color = Color.CYAN;

    public Guarma(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        data = new OrganismData(Types.GUARMA, PLANT_STR,  PLANT_INIT, PLANT_MOVE_SPEED, color);
    }

    @Override
    public boolean specialAbility(Organism other) {
        if (other == null) {
            return false;
        }

        other.setStr(other.getData().str() + STR_ADD);

        // false means it doesn't win or avoid fight
        return false;
    }

    @Override
    public boolean specialAbilityCheck(Organism other) {
        return true;
    }
}
