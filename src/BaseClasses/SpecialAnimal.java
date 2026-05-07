package BaseClasses;

import Structs.Controller;
import Structs.Vec2;

import java.util.ArrayList;

public abstract class SpecialAnimal extends Animal{
    private boolean moving = false;

    public SpecialAnimal(Vec2 position, Controller controller) {
        super(position, controller);
    }

    @Override
    public void update() {
        setMoving(true);
        super.update();
        setMoving(false);
    }

    protected Vec2 findSafeSpot() {
        Vec2 []possibleMove = getValidMoves(getPosition().y(), getData().moveSpeed());

        var safeMoves = new ArrayList<Vec2>(possibleMove.length);
        for (var move :  possibleMove) {
            // if safe add to safe moves
            if (controller.checkSafeTiles(getPosition(), move)) {
                safeMoves.add(move);
            };
        }

        if (safeMoves.isEmpty()) {
            return null;
        }

        var rand =  new java.util.Random();
        int idx = rand.nextInt(safeMoves.size());
        return safeMoves.get(idx);
    }

    protected boolean isMoving() {
        return moving;
    }

    private void setMoving(boolean moving) {
        this.moving = moving;
    }
}
