package BaseClasses;

import Animals.Fox;
import Animals.Sheep;
import Animals.Wolf;
import Structs.Controller;
import Structs.Types;
import Structs.Vec2;

import java.util.ArrayList;
import java.util.Random;

public class WorldManager implements Controller {
    private boolean isRunning = true;
    private boolean turnRequested =  false;
    private final Random rand = new Random(); // randomizer
    private final ArrayList<Organism> organisms;
    private final Organism[][] worldMap;
    private final ArrayList<Organism> toAdd = new ArrayList<>();
    private final ArrayList<Organism> toRemove = new ArrayList<>();

    enum ColisionType {
        EMPTY,
        REPRODUCE,
        FIGHT
    }

    public WorldManager(int organismCount, int mapSize) {
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

    public void handleMouseClick(int x, int y, Types type) {
        Organism target = worldMap[x][y];
        if (target != null) {
            target.setActive(false);
            removeFromWorld(target);
            System.out.println("Removed organism: " + target);
            return;
        }

        Vec2 pos = new Vec2(x, y);
        Organism child = spawnOrganism(pos, type);
        addToWorld(child);
    }

    private boolean isOutOfBounds(Vec2 newPosition) {
        boolean x_hit = newPosition.x() < 0 || newPosition.x() >= worldMap.length;
        boolean y_hit = newPosition.y() < 0 || newPosition.y() >= worldMap[0].length;

        return x_hit || y_hit;
    }


    private ColisionType isOccupied(Vec2 newPosition, Types type) {
        var target = worldMap[newPosition.x()][newPosition.y()];

        if (target == null) {
            return ColisionType.EMPTY;
        }

        if (target.getData().type() == type) {
            return ColisionType.REPRODUCE;
        }

        // target is an enemy
        return ColisionType.FIGHT;
    }

    private Vec2 getRandomPosition() {
        int maxTries = 5;

        while (maxTries-- != 0) {
            var x = rand.nextInt(worldMap.length); // 0 to map_size
            var y = rand.nextInt(worldMap[0].length);
            var pos = new Vec2(x, y);

            if (isOccupied(pos, Types.NONE) == ColisionType.EMPTY) {
                return new Vec2(x, y);
            }
        }
        return null;
    }

    private Vec2 getChildPosition(Vec2 pos) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                var tarX = pos.x() + x;
                var tarY = pos.y() + y;

                if (tarX >= getMapSizeX() || tarY >= getMapSizeY()) {
                    continue;
                }
                if (tarX < 0 || tarY < 0) {
                    continue;
                }

                if (worldMap[pos.x() + x][pos.y() + y] == null) {
                    return new Vec2(pos.x() + x, pos.y() + y);
                }
            }
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
        for (int i = 0; i < organismCount; i++) {
            int type_id = rand.nextInt(Types.NONE.ordinal());
            Types type = Types.values()[type_id];
            var childPosition = getRandomPosition();
            var child = spawnOrganism(childPosition, type);
            addToWorld(child);
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
            case WOLF -> {
                return new Wolf(pos, this);
            }
            case SHEEP -> {
                return new Sheep(pos, this);
            }
            case FOX -> {
                return new Fox(pos, this);
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
        organisms.sort((a,b) -> {

            if (a == b) return 0;

            int initCompare = Integer.compare(a.getData().init(), b.getData().init());

            if (initCompare != 0) return -initCompare;

            return  Integer.compare(a.getAge(), b.getAge());

        });
    }

    private void fillAndRemoveOrganisms() {

        organisms.removeAll(toRemove);
        organisms.addAll(toAdd);

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
            organism.Update();
        }
    }

    private Organism reproduce(Organism []orgs) {
        for (var org : orgs) {
            if (org == null) {
                return null;
            }

            org.setActive(false);

            var childPosition = getChildPosition(org.getPosition());
            if (childPosition == null) {
                continue;
            }

            Types parentRace = org.data.type();
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
        var winner = attacker.data.str() >= defender.data.str() ? attacker : defender;

        attacker.setActive(false);
        defender.setActive(false);


        if (attacker.specialAbilityCheck(defender)) {

            // couldn't use special no space or sum else
            if (!attacker.specialAbilityCheck(defender)) {
                return FightResults.DEFENDER_WIN;
            }

            return FightResults.ATTACKER_SPECIAL;
        }

        if (defender.specialAbilityCheck(attacker)) {

            // couldn't defend sum happen
            if (!defender.specialAbility(attacker)) {
                return FightResults.ATTACKER_WIN;
            }

            return FightResults.DEFENDER_SPECIAL;
        }

        if (winner == attacker) {
            return FightResults.ATTACKER_WIN;
        }

        return  FightResults.DEFENDER_WIN;
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
    public Results moveResults(Organism o, Vec2 moveVec) {
        Vec2 targetPosition = o.getPosition().add(moveVec);
        Organism target = worldMap[targetPosition.x()][targetPosition.y()];

        if (target == null || target == o) {
            return Results.MOVE;
        }

        switch(isOccupied(target.getPosition(), o.data.type())) {
            case FIGHT -> {
                switch (fight(o, target)) {
                    case ATTACKER_WIN -> {
                        removeFromWorld(target);
                        return Results.FIGHT_WON;
                    }
                    case DEFENDER_WIN -> {
                        removeFromWorld(o);
                        return Results.FIGHT_LOST;
                    }
                    default -> {
                        return Results.NONE;
                    }
                }
            }
            case REPRODUCE -> {
                Organism []orgs = new Organism[2];
                orgs[0] = o;
                orgs[1] = target;

                var child = reproduce(orgs);

                // he can't move as he didn't make a child but there is still a thing on this tile
                if (child == null) {
                    return Results.NONE;
                }

                addToWorld(child);

                return Results.REPRODUCE;
            }
            default -> {
                return Results.MOVE;
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
}
