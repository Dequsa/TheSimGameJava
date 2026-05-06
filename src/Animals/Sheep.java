package Animals;

import BaseClasses.Organism;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;

import java.awt.*;

public class Sheep extends Organism {
    public Sheep(Vec2 position, Controller controller) {
        super(position, controller);

        final int SHEEP_STR = 4;
        final int SHEEP_INIT = 7;
        final Color SHEEP_COLOR = Color.pink;

        data = new OrganismData(Types.SHEEP,SHEEP_STR, SHEEP_INIT, SHEEP_COLOR);
    }
}
