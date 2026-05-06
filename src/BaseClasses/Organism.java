package BaseClasses;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Vec2;

import java.awt.*;

public abstract class Organism {
    protected OrganismData data;
    private Vec2 position = null;
    private int age = 0;
    private boolean active = true;

    protected final Controller controller;

    public Organism(Vec2 position, Controller controller) {
        this.position = position;
        this.controller = controller;
    }

    protected void Update() {
        age++;
        setActive(false);

        Vec2 moveVec = getRandomMoveVec();

        Controller.Results action = Controller.Results.NONE;

        if (controller.requestMove(this, moveVec)) {
            action = controller.moveResults(this,  moveVec);
        }

        if (action == Controller.Results.MOVE || action == Controller.Results.FIGHT_WON) {
            move(moveVec);
        }
    }

    protected Vec2[] getValidMoves(int y) {
        Vec2 []validMoves;
        if (y % 2 == 0) {
            validMoves = new Vec2[] {
                    new Vec2(-1, -1), // Up Left
                    new Vec2(0, -1),  // Up Right
                    new Vec2(-1, 0),  // Left
                    new Vec2(1, 0),   // Right
                    new Vec2(-1, 1),  // Down Left
                    new Vec2(0, 1),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        } else {
            validMoves = new Vec2[] {
                    new Vec2(0, -1),  // Up Left
                    new Vec2(1, -1),  // Up Right
                    new Vec2(-1, 0),  // Left
                    new Vec2(1, 0),   // Right
                    new Vec2(0, 1),   // Down Left
                    new Vec2(1, 1),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        }
        return validMoves;
    }

    protected Vec2 getRandomMoveVec() {
        var rand = new java.util.Random();
        int y = position.y();

        Vec2[] validMoves = getValidMoves(y);

        int randomIndex = rand.nextInt(validMoves.length);
        return validMoves[randomIndex];
    }

    protected void move(Vec2 moveVec) {
        controller.setTile(null, position);
        position = position.add(moveVec);
        controller.setTile(this, position);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public OrganismData getData() {
        return data;
    }

    public void setPosition(Vec2 newVec2) {
        this.position = new Vec2(newVec2.x(), newVec2.y());
    }

    public Vec2 getPosition() {
        return position;
    }

    public Color getColor() {
        return getData().color();
    }

    public boolean specialAbilityCheck(Organism other) {
        return false;
    }

    protected boolean specialAbility(Organism other) {return false;}

    @Override
    public String toString() {
        return data.type().name() + " at: " + position.toString() + " age: " + age;
    }
}
