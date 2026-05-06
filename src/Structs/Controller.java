package Structs;

import BaseClasses.Organism;

public interface Controller {
    enum Results {
        MOVE,
        FIGHT_LOST,
        FIGHT_WON,
        REPRODUCE,
        NONE
    }
    boolean requestMove(Organism o, Vec2 moveVec);

    Results moveResults(Organism o, Vec2 moveVec);

    void setTile(Organism o, Vec2 position);
}