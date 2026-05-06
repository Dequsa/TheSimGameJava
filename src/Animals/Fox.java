package Animals;

import BaseClasses.Organism;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Types;
import Structs.Vec2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Fox extends Organism {

    private boolean moving = false;

    public Fox(Vec2 position, Controller controller) {
        super(position, controller);

        final int FOX_STR = 5;
        final int FOX_INIT = 7;
        final Color FOX_COLOR = Color.ORANGE;

        data = new OrganismData(Types.FOX, FOX_STR, FOX_INIT, FOX_COLOR);
    }

    @Override
    public void Update() {
        setMoving(true);
        super.Update();
        setMoving(false);
    }

    private Vec2 findSafeSpot(Vec2 enemyPos) {
        Vec2 []possibleMove = getValidMoves(getPosition().y());

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

    private boolean avoidEnemy(Vec2 enemyPos) {
        Vec2 safePosition = findSafeSpot(enemyPos);

        // can't escape
        if  (safePosition == null) {
            return false;
        }

        super.move(safePosition);


        return true;
    }

    public boolean specialAbilityCheck(Organism other) {
        if (isMoving()) {
            // is moving means its attacking
            return other.getData().str() > data.str();
        }

        // it's not moving so it's defending
        return false;
    }

    protected boolean specialAbility(Organism other) {
        System.out.println(this + "has avoided an enemy.");
        return avoidEnemy(other.getPosition());
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
