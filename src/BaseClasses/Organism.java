package BaseClasses;
import Structs.Controller;
import Structs.OrganismData;
import Structs.Vec2;
import movementHandler.SquareMovement;
import movementHandler.GridType;
import movementHandler.HexagonMovement;
import movementHandler.movementType;

import java.awt.*;

public abstract class Organism {
    protected OrganismData data;
    private Vec2 position = null;
    private int age = 0;
    private boolean active = true;
    protected final Controller controller;
    protected movementType movementHandler;

    public Organism(Vec2 position, Controller controller, GridType gridType) {
        this.position = position;
        this.controller = controller;

        switch (gridType) {
            case HEXAGON:
                movementHandler = new HexagonMovement();
                break;
            case SQUARE:
                movementHandler = new SquareMovement();
        }

    }

    protected void update() {
        age++;
        setActive(false);
    }

    protected Vec2 getRandomMoveVec() {
        var rand = new java.util.Random();
        int y = position.y();

        Vec2[] validMoves = movementHandler.getValidMoves(y, data.moveSpeed());

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
