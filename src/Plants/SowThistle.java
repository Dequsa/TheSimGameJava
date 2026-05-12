package Plants;

import BaseClasses.Plant;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;

import java.awt.*;

public class SowThistle extends Plant {

    final int AMOUNT_OF_CHANCES = 3;

    public SowThistle(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        final Color GRASS_COLOR = new Color(106, 231, 106, 255);
        this.CHANCE_TO_SOW = 0.7F;

        data = new OrganismData(Types.SOWTHISTLE, PLANT_STR, PLANT_INIT, PLANT_MOVE_SPEED, GRASS_COLOR);
    }

    private boolean canSow(Controller.MoveResults results) {
        return results == Controller.MoveResults.NONE || results == Controller.MoveResults.MOVE;
    }

    @Override
    protected void handleSowing(Vec2 moveVec) {
        var rand = new java.util.Random();
        for (int i = 0; i < AMOUNT_OF_CHANCES; i++) {
            if (rand.nextDouble() < this.CHANCE_TO_SOW) {
                Controller.MoveResults results = controller.sowingResults(this, moveVec, false);

                /* it did it's part it can rest now */
                if (!canSow(results)) return;
            }
        }
    }

    @Override
    protected void update() {
        super.update();
    }
}
