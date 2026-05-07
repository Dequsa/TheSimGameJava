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

    protected void update() {
        age++;
        setActive(false);
    }

    protected Vec2[] getValidMoves(int y, int speed) {
        Vec2 []validMoves;
        if (y % 2 == 0) {
            validMoves = new Vec2[] {
                    new Vec2(-speed, -speed), // Up Left
                    new Vec2(0, -speed),  // Up Right
                    new Vec2(-speed, 0),  // Left
                    new Vec2(speed, 0),   // Right
                    new Vec2(-speed, speed),  // Down Left
                    new Vec2(0, speed),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        } else {
            validMoves = new Vec2[] {
                    new Vec2(0, -speed),  // Up Left
                    new Vec2(speed, -speed),  // Up Right
                    new Vec2(-speed, 0),  // Left
                    new Vec2(speed, 0),   // Right
                    new Vec2(0, speed),   // Down Left
                    new Vec2(speed, speed),   // Down Right
                    new Vec2(0, 0)    // No move
            };
        }
        return validMoves;
    }

    protected Vec2 getRandomMoveVec() {
        var rand = new java.util.Random();
        int y = position.y();

        Vec2[] validMoves = getValidMoves(y, data.moveSpeed());

        int randomIndex = rand.nextInt(validMoves.length);
        return validMoves[randomIndex];
    }

    protected void move (Vec2 moveVec) {}

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

    public boolean specialAbility(Organism other) {return false;}

    @Override
    public String toString() {
        return data.type().name() + " at: " + position.toString() + " age: " + age;
    }
}
