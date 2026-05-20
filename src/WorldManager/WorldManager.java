package WorldManager;

import Animals.*;
import Animals.Human.Human;
import BaseClasses.Organism;
import GUI.TextPrinter;
import Plants.Grass;
import Plants.SowThistle;
import Plants.SpecialPlants.Belladonna;
import Plants.SpecialPlants.Guarma;
import Plants.SpecialPlants.Hogweed;
import Structs.Controller;
import Structs.Types;
import Structs.Vec2;
import movementHandler.GridType;
import movementHandler.HexagonMovement;
import movementHandler.SquareMovement;
import movementHandler.movementType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class WorldManager implements Controller, Serializable {
    private final GridType gridType;
    private boolean isRunning = true;
    private boolean turnRequested = false;
    private Vec2 nextMoveDirection = null;
    private final Random rand = new Random(); // randomizer
    private movementType positionFinder = null;
    private TextPrinter printer;
    private final ArrayList<Organism> organisms;
    private final Organism[][] worldMap;
    private final ArrayList<Organism> toAdd = new ArrayList<>();
    private final ArrayList<Organism> toRemove = new ArrayList<>();
    private final ArrayList<Organism> contollableOrganisms = new ArrayList<>();


    @Override
    public Vec2 getMoveDirection() {
        if (nextMoveDirection == null) {
            return null;
        }
        Vec2 returnDir = nextMoveDirection;
        nextMoveDirection = null;
        return returnDir;
    }

    public void setNextMoveDirection(Vec2 nextMoveDirection) {
        this.nextMoveDirection = nextMoveDirection;
    }

    public ArrayList<Organism> getControllableOrganisms() {
        return contollableOrganisms;
    }

    enum CollisionType {
        EMPTY,
        REPRODUCE,
        FIGHT
    }

    public WorldManager(GridType gridType, int organismCount, int mapSize) {
        this.gridType = gridType;
        switch (gridType) {
            case HEXAGON:
                positionFinder = new HexagonMovement();
                break;
            case SQUARE:
                positionFinder = new SquareMovement();
                break;
        }

        if (mapSize <= 0) {
            mapSize = 10;
        }
        if (organismCount <= 0) {
            organismCount = 1;
        }

        organisms = new ArrayList<>(organismCount);
        worldMap = new Organism[mapSize][mapSize];
        populateMap(organismCount);
    }

    public void setPrinter(TextPrinter printer) {
        this.printer = printer;
    }

//    public boolean isTileNextToOrganism(int tileX, int tileY) {
//        for (int x = -1; x <= 1; x++) {
//            for (int y = -1; y <= 1; y++) {
//                if (x == 0 && y == 0) continue;
//                Vec2 checkPosition = new Vec2(tileX + x, tileY + y);
//                if (isOutOfBounds(checkPosition)) continue;
//                if (isOccupied(checkPosition)) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    public Organism getOrganismAt(Vec2 position) {
        return worldMap[position.x()][position.y()];
    }

    public void handleTileAction(Vec2 tilePos, Types type) {
        Organism target = worldMap[tilePos.x()][tilePos.y()];
        if (target != null) {
            target.setActive(false);
            removeFromWorld(target);
            printText("Removed organism: " + target);
            return;
        }

        Organism child = spawnOrganism(tilePos, type);
        addToWorld(child);
    }

    public boolean isOutOfBounds(Vec2 newPosition) {
        boolean x_hit = newPosition.x() < 0 || newPosition.x() >= worldMap.length;
        boolean y_hit = newPosition.y() < 0 || newPosition.y() >= worldMap[0].length;

        return x_hit || y_hit;
    }


    private CollisionType getCollisionType(Vec2 newPosition, Types type) {
        var target = worldMap[newPosition.x()][newPosition.y()];

        if (target == null) {
            return CollisionType.EMPTY;
        }

        if (target.getData().type() == type) {
            return CollisionType.REPRODUCE;
        }

        // target is an enemy
        return CollisionType.FIGHT;
    }

    private boolean isOccupied(Vec2 pos) {
        if (pos == null) return true;

        if (isOutOfBounds(pos)) return true;

        Organism target = worldMap[pos.x()][pos.y()];
        return target != null;
    }

    private Vec2 getRandomPosition() {
        int maxTries = 5;

        while (maxTries-- != 0) {
            var x = rand.nextInt(worldMap.length); // 0 to map_size
            var y = rand.nextInt(worldMap[0].length);
            var pos = new Vec2(x, y);

            if (getCollisionType(pos, Types.NONE) == CollisionType.EMPTY) {
                return new Vec2(x, y);
            }
        }
        return null;
    }

    private Vec2 getChildPosition(Vec2 pos) {
        final int BIRTH_RADIUS = 1;

        Vec2[] possible_relative_positions = positionFinder.getSingleStep(pos.y());

        Vec2[] valid_positions = new Vec2[possible_relative_positions.length];

        int i = 0;
        for (var relative_pos : possible_relative_positions) {
            var check_position = pos.add(relative_pos);
            if (isOutOfBounds(check_position)) continue;

            Organism target = worldMap[check_position.x()][check_position.y()];
            if (target != null) continue;

            valid_positions[i++] = check_position;
        }

        if (valid_positions.length != 0) {
            return valid_positions[rand.nextInt(valid_positions.length)];
        }

        return null;
    }

    private void addToWorld(Organism o) {
        o.setActive(false);
        toAdd.add(o);
        worldMap[o.getPosition().x()][o.getPosition().y()] = o;
    }

    private void removeFromWorld(Organism o) {
        o.setActive(false);
        toRemove.add(o);
        toAdd.remove(o);
        worldMap[o.getPosition().x()][o.getPosition().y()] = null;
    }

    private void populateMap(int organismCount) {
        int human_count = 0;
        for (int i = 0; i < organismCount; i++) {
            int type_id = rand.nextInt(Types.NONE.ordinal());
            Types type = Types.values()[type_id];

            if (type == Types.HUMAN && human_count >= 1) {
                type = Types.GRASS;
            }

            var childPosition = getRandomPosition();
            var child = spawnOrganism(childPosition, type);

            if (child == null) continue;

            if (child.getData().type() == Types.HUMAN) {
                human_count++;
            }

            addToWorld(child);
        }

        if (human_count == 0 && organismCount > 0) {
            addToWorld(spawnOrganism(new Vec2(0, 0), Types.HUMAN));
            removeOrganism(organisms.getFirst());
        }
    }

    private Organism spawnOrganism(Vec2 pos, Types type) {
        if (pos == null) {
            return null;
        }
        if (type == null || type == Types.NONE) {
            return null;
        }
        if (pos.x() < 0 || pos.x() >= worldMap.length) {
            return null;
        }
        if (pos.y() < 0 || pos.y() >= worldMap[0].length) {
            return null;
        }

        switch (type) {
            case HUMAN -> {
                return new Human(pos, this, gridType);
            }
            case WOLF -> {
                return new Wolf(pos, this, gridType);
            }
            case SHEEP -> {
                return new Sheep(pos, this, gridType);
            }
            case FOX -> {
                return new Fox(pos, this, gridType);
            }
            case TURTLE -> {
                return new Turtle(pos, this, gridType);
            }
            case ANTELOPE -> {
                return new Antelope(pos, this, gridType);
            }
            case GRASS -> {
                return new Grass(pos, this, gridType);
            }
            case GUARMA -> {
                return new Guarma(pos, this, gridType);
            }
            case SOWTHISTLE -> {
                return new SowThistle(pos, this, gridType);
            }
            case BELLADONNA -> {
                return new Belladonna(pos, this, gridType);
            }
            case HOGWEED -> {
                return new Hogweed(pos, this, gridType);
            }
            default -> {
                return null;
            }
        }
    }

    public int getMapSizeX() {
        return worldMap.length;
    }

    public int getMapSizeY() {
        return worldMap[0].length;
    }

    private void sortOrganisms() {
        organisms.sort((a, b) -> {

            if (a == b) return 0;

            int initCompare = Integer.compare(a.getData().init(), b.getData().init());

            if (initCompare != 0) return -initCompare;

            return Integer.compare(a.getAge(), b.getAge());

        });
    }

    private void fillAndRemoveOrganisms() {
        organisms.removeAll(toRemove);
        contollableOrganisms.removeAll(toRemove);

        organisms.addAll(toAdd);

        for (var o : toAdd) {
            if (o.isControllable()) {
                contollableOrganisms.add(o);
            }
        }

        toRemove.clear();
        toAdd.clear();

        sortOrganisms();
    }

    private void resetActivityAll() {
        for (var o : organisms) {
            o.setActive(true);
        }
    }

    public void Update() {
        fillAndRemoveOrganisms();
        resetActivityAll();

        for (Organism organism : organisms) {
            if (!organism.isActive()) {
                continue;
            }

            organism.update();
        }
    }

    private Boolean makeChild(Organism o, Organism target) {
        Organism[] orgs = new Organism[2];
        orgs[0] = o;
        orgs[1] = target;

        var child = reproduce(orgs);

        // he can't move as he didn't make a child but there is still a thing on this tile
        if (child == null) {
            return false;
        }

        addToWorld(child);

        return true;
    }

    private Organism reproduce(Organism[] orgs) {
        for (var org : orgs) {
            if (org == null) {
                return null;
            }

            org.setActive(false);

            var childPosition = getChildPosition(org.getPosition());
            if (childPosition == null) {
                continue;
            }

            Types parentRace = org.getData().type();
            return spawnOrganism(childPosition, parentRace);
        }
        return null;
    }

    enum FightResults {
        ATTACKER_WIN,
        DEFENDER_WIN,
        DEFENDER_SPECIAL,
        ATTACKER_SPECIAL
    }

    // true attacker won he can move to next tile
    // false attacker lost and he died
    private FightResults fight(Organism attacker, Organism defender) {
        if (attacker == defender || attacker == null || defender == null) return null;

        var winner = attacker.getData().str() >= defender.getData().str() ? attacker : defender;

        attacker.setActive(false);
        defender.setActive(false);


        if (attacker.specialAbilityCheck(defender)) {

            if (attacker.specialAbility(defender)) {
                return FightResults.ATTACKER_SPECIAL;
            }
            // failed in special attacking
            return FightResults.DEFENDER_WIN;
        }

        if (winner == defender) {
            return FightResults.DEFENDER_WIN;
        }

        if (defender.specialAbilityCheck(attacker)) {
            if (defender.specialAbility(attacker)) {
                return FightResults.DEFENDER_SPECIAL;
            }
            // couldn't defend something happen
            return FightResults.ATTACKER_WIN;
        }

        return FightResults.ATTACKER_WIN;
    }

    @Override
    public void removeOrganism(Organism target) {
        if (target == null) return;
        removeFromWorld(target);
    }

    @Override
    public void printText(String text) {
        printer.print(text);
    }

    @Override
    public boolean requestMove(Organism o, Vec2 moveVec) {
        if (o == null) {
            return false;
        }
        return !isOutOfBounds(o.getPosition().add(moveVec));
    }

    @Override
    public boolean checkSafeTiles(Vec2 oPosition, Vec2 moveVec) {
        var newPosition = oPosition.add(moveVec);

        if (!isOutOfBounds(newPosition)) {
            return worldMap[newPosition.x()][newPosition.y()] == null;
        }

        return false;
    }

    @Override
    public MoveResults moveResults(Organism o, Vec2 moveVec) {
        Vec2 targetPosition = o.getPosition().add(moveVec);
        Organism target = worldMap[targetPosition.x()][targetPosition.y()];

        if (target == null || target == o) {
            return MoveResults.MOVE;
        }

        switch (getCollisionType(target.getPosition(), o.getData().type())) {
            case FIGHT -> {
                return getFightResults(o, target);
            }
            case REPRODUCE -> {
                if (makeChild(o, target)) {
                    return MoveResults.REPRODUCE;
                }

                return MoveResults.NONE;
            }
            default -> {
                return MoveResults.MOVE;
            }
        }
    }

    private movementType ChoosePositionFinder() {
        switch (gridType) {
            case GridType.SQUARE -> {
                return new SquareMovement();
            }

            case GridType.HEXAGON -> {
                return new HexagonMovement();
            }

            default -> {
                return null;
            }
        }
    }

    private void aoeAttack(Organism attacker) {
        movementType positionFinder = ChoosePositionFinder();

        if (positionFinder == null) return;

        Vec2 []possible_tiles = positionFinder.getSingleStep(attacker.getPosition().y());

        for (var relativePos : possible_tiles) {
            var checkPos = attacker.getPosition().add(relativePos);
            if (isOutOfBounds(checkPos)) continue;
            if (!isOccupied(checkPos)) continue;

            Organism target = worldMap[checkPos.x()][checkPos.y()];
            if (target.getData().type().ordinal() > Types.GRASS.ordinal()) continue;

            printText(target + " got hit by an AOE attack!");
            removeFromWorld(target);
        }
//
//        for (int x = -1; x <= 1; x++) {
//            for (int y = -1; y <= 1; y++) {
//                if (x == 0 && y == 0) continue;
//
//                Vec2 checkPosition = attacker.getPosition().add(new Vec2(x, y));
//
//                if (isOutOfBounds(checkPosition)) continue;
//                if (isOccupied(checkPosition)) {
//
//                    Organism target = worldMap[checkPosition.x()][checkPosition.y()];
//                    if (target.getData().type().ordinal() > Types.GRASS.ordinal()) continue;
//
//                    printText(target + " got hit by an AOE attack!");
//                    removeFromWorld(target);
//                }
//            }
//        }
    }

    @Override
    public MoveResults sowingResults(Organism o, Vec2 moveVec, boolean isAOE, boolean sowing) {
        if (o == null) {
            System.out.println("The sower is null.");
            return MoveResults.NONE;
        }

        Vec2 targetPosition = o.getPosition().add(moveVec);

        if (isAOE) {
            aoeAttack(o);
        }

        if (sowing) {
            if (getCollisionType(targetPosition, o.getData().type()) != CollisionType.FIGHT) {
                if (makeChild(o, null)) {
                    return MoveResults.REPRODUCE;
                }
            }
        }
        return MoveResults.NONE;
    }

    private MoveResults getFightResults(Organism o, Organism target) {
        switch (fight(o, target)) {
            case ATTACKER_WIN -> {
                removeFromWorld(target);
                return MoveResults.FIGHT_WON;
            }
            case DEFENDER_WIN -> {
                removeFromWorld(o);
                return MoveResults.FIGHT_LOST;
            }
            case null, default -> { // its also for special abilities use
                return MoveResults.NONE;
            }
        }
    }

    @Override
    public void setTile(Organism o, Vec2 position) {
        worldMap[position.x()][position.y()] = o;
    }

    public Organism[][] getWorldMap() {
        return worldMap;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public boolean isTurnRequested() {
        return turnRequested;
    }

    public void setTurnRequested(boolean turnRequested) {
        this.turnRequested = turnRequested;
    }

    public GridType getGridType() {
        return gridType;
    }
}
