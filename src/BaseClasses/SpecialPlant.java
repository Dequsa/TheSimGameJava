package BaseClasses;

import Structs.Controller;
import Structs.Vec2;
import movementHandler.GridType;

public abstract class SpecialPlant extends Plant {
    public SpecialPlant(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);
    }
}
