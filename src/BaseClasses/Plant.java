package BaseClasses;
import Structs.Controller;
import Structs.Vec2;
import movementHandler.GridType;

public abstract class Plant extends Organism{
    protected final int PLANT_STR = 0;
    protected final int PLANT_INIT = 0;
    protected final int PLANT_MOVE_SPEED = 1;
    protected float CHANCE_TO_SOW = 0.5F;
    protected boolean IS_AOE = false;

    public Plant(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);
    }

    protected void handleSowing(Vec2 moveVec) {
        var rand = new java.util.Random();
        boolean sowing = false;
        if (rand.nextFloat() < CHANCE_TO_SOW) {
            sowing = true;
        }

        controller.sowingResults(this, moveVec, IS_AOE, sowing);
    }

    @Override
    public void update() {
        super.update();
        Vec2 moveVec = determineMove();

        // check if sowing direction is in bound
        if (controller.requestMove(this, moveVec)){
            handleSowing(moveVec);
        }
    }
}
