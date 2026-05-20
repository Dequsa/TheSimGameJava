package Structs;

import BaseClasses.Organism;

public interface Controller {
    enum MoveResults {
        MOVE,
        FIGHT_LOST,
        FIGHT_WON,
        REPRODUCE,
        NONE
    }

    void printText(String text);

    boolean requestMove(Organism o, Vec2 moveVec);

    Vec2 getMoveDirection();

    MoveResults moveResults(Organism o, Vec2 moveVec);

    MoveResults sowingResults(Organism o, Vec2 moveVec, boolean isAOE, boolean sowing);

    boolean checkSafeTiles(Vec2 oPosition, Vec2 moveVec);

    void setTile(Organism o, Vec2 position);

    void removeOrganism(Organism target);
}