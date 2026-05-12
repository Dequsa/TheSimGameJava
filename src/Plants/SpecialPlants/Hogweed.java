package Plants.SpecialPlants;

import BaseClasses.Organism;
import BaseClasses.SpecialPlant;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;
import java.awt.*;

public class Hogweed extends SpecialPlant {
    final Color COLOR = Color.BLACK;

    public Hogweed(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);
        IS_AOE = true;
        CHANCE_TO_SOW = 0.0F;
        data = new OrganismData(Types.HOGWEED, PLANT_STR,  PLANT_INIT, PLANT_MOVE_SPEED, COLOR);
    }

    @Override
    public boolean specialAbility(Organism other) {
        if (other == null) {
            return false;
        }

        // kill anything that eats it
        controller.removeOrganism(other);
        return true;
    }

    @Override
    public boolean specialAbilityCheck(Organism other) {
        return true;
    }
}
