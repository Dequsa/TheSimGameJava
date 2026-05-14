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
    protected boolean controllable = false;
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

    public void update() {
        age++;
        setActive(false);
    }

    protected Vec2 determineMove() {
        var rand = new java.util.Random();
        int y = position.y();

        Vec2[] validMoves = movementHandler.getMultiStep(y, data.moveSpeed());

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

    /*
    TODO figure how to change to so i dont have to create new instance of data everytime i want to change strength
    */
    public void setStr(int str) {
        data = new OrganismData(data.type(), str, data.init(), data.moveSpeed(), data.color());
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

    public boolean isControllable() {
        return controllable;
    }

    public void setControllable(boolean controllable) {
        this.controllable = controllable;
    }

    public void setMoveVector(Vec2 moveVec) {}
}
