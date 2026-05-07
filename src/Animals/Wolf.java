package Animals;

import BaseClasses.Animal;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;

import java.awt.*;

public class Wolf extends Animal {
    public Wolf(Vec2 position, Controller controller) {
        super(position, controller);

        int WOLF_STR = 9;
        int WOLF_INIT = 4;
        int WOLF_MOVESPEED = 1;

        data = new OrganismData(Types.WOLF, WOLF_STR, WOLF_INIT, WOLF_MOVESPEED, Color.blue);
    }
}
