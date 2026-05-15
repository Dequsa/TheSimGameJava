package Animals.Human;

import BaseClasses.SpecialAnimal;
import Structs.*;
import items.Item;
import movementHandler.GridType;

import java.awt.*;
import java.util.ArrayList;

public class Human extends SpecialAnimal {
    private Vec2 moveVec = null;
    ArrayList<Item> specialItems = new ArrayList<>();
    ArrayList<Item> usedItems = new ArrayList<>();

    public Human(Vec2 position, Controller controller, GridType gridType) {
        super(position, controller, gridType);

        controllable = true;
        final int HUMAN_STR = 6;
        final int HUMAN_INIT = 9;
        final Color HUMAN_COLOR = Color.WHITE;
        final int HUMAN_MOVE_SPEED = 1;
        final int POTION_STR = 10;

        specialItems.add(new items.StrengthPotion(POTION_STR, 5));

        data = new OrganismData(Types.HUMAN, HUMAN_STR, HUMAN_INIT, HUMAN_MOVE_SPEED, HUMAN_COLOR);
    }

    @Override
    public void setMoveVector(Vec2 moveVec) {
         this.moveVec = moveVec;
    }

    @Override
    protected Vec2 determineMove() {
        if (moveVec == null) {
            return new Vec2(0, 0);
        }

        return moveVec;
    }

    @Override
    public void useSpecialItem(Item item) {
        if (specialItems.contains(item)) {
            item.applyEffect(this);
            usedItems.add(item);
            specialItems.remove(item);
        }
    }

    private void itemDecay() {
        ArrayList<Item> toRemove = new ArrayList<>();

        for (var item : usedItems) {
            if (item == null) continue;

            item.decayEffect(this);

            if (item.getDuration() <= 0) {
                toRemove.add(item);
            }
        }
        usedItems.removeAll(toRemove);
    }

    @Override
    public void update() {
        super.update();
        itemDecay();
        moveVec = null;
    }

    @Override
    public ArrayList<Item> getSpecialItems() {
        return specialItems;
    }
}
