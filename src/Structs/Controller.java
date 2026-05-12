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
    boolean requestMove(Organism o, Vec2 moveVec);

    MoveResults moveResults(Organism o, Vec2 moveVec);

    MoveResults sowingResults(Organism o, Vec2 moveVec, boolean isAOE);

    boolean checkSafeTiles(Vec2 oPosition, Vec2 moveVec);

    void setTile(Organism o, Vec2 position);
}