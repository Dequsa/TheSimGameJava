package BaseClasses;
import Structs.Controller;
import Structs.Vec2;

public abstract class Plant extends Organism{
    protected final int PLANT_INIT = 0;
    protected final int PLANT_MOVESPEED = 1;

    public Plant(Vec2 position, Controller controller) {
        super(position, controller);
    }

    @Override
    protected void update() {
        super.update();

        Vec2 moveVec = getRandomMoveVec();

        // check if sowing direction is in bound
        if (controller.requestMove(this, moveVec)){
            controller.sowingResults(this, moveVec);
        }
    }
}
