package Plants;
import BaseClasses.Plant;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;

import java.awt.*;

public class Grass extends Plant{

    public Grass(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        final Color GRASS_COLOR = new Color(136, 231, 136, 255);

        data = new OrganismData(Types.GRASS, PLANT_STR, PLANT_INIT, PLANT_MOVESPEED, GRASS_COLOR);
    }
}
