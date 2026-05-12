package BaseClasses;
import Structs.Controller;
import Structs.Vec2;
import movementHandler.GridType;

public abstract class Plant extends Organism{
    protected final int PLANT_STR = 0;
    protected final int PLANT_INIT = 0;
    protected final int PLANT_MOVE_SPEED = 1;
    protected float CHANCE_TO_SOW = 0.5F;

    public Plant(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);
    }

    protected void handleSowing(Vec2 moveVec) {
        var rand = new java.util.Random();
        if (rand.nextFloat() < CHANCE_TO_SOW) {
            controller.sowingResults(this, moveVec, false);
        }
    }

    @Override
    protected void update() {
        super.update();
        Vec2 moveVec = getRandomMoveVec();

        // check if sowing direction is in bound
        if (controller.requestMove(this, moveVec)){
            handleSowing(moveVec);
        }
    }
}
