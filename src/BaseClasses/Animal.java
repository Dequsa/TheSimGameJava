package BaseClasses;

import Structs.Controller;
import Structs.Vec2;
import movementHandler.GridType;

public abstract class Animal extends Organism {
    public Animal(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);
    }

    @Override
    protected void update() {
        super.update();

        Vec2 moveVec = getRandomMoveVec();

        Controller.Results action = Controller.Results.NONE;

        if (controller.requestMove(this, moveVec)) {
            action = controller.moveResults(this,  moveVec);
        }

        if (action == Controller.Results.MOVE || action == Controller.Results.FIGHT_WON) {
            move(moveVec);
        }
    }

    @Override
    protected void move(Vec2 moveVec) {
        controller.setTile(null, getPosition());
        setPosition(getPosition().add(moveVec));
        controller.setTile(this, getPosition());
    }
}
