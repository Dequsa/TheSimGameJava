package Structs;

public record Vec2(int x, int y) {
    public Vec2 move(int vectorX, int vectorY) {
        return new Vec2(this.x + vectorX, this.y + vectorY);
    }

    public Vec2 setNewPosition(int newX, int newY) {
        return new Vec2(this.x, this.y + newY);
    }

    public Vec2 setNewPosition(Vec2 newVec2) {
        return new Vec2(newVec2.x(), newVec2.y());
    }

    public Vec2 add(Vec2 otherVec2) {
        return new Vec2(this.x + otherVec2.x, this.y + otherVec2.y);
    }

    public Vec2 subtract(Vec2 otherVec2) {
        return new Vec2(this.x - otherVec2.x, this.y - otherVec2.y);
    }

    public int distanceBetween(Vec2 otherVec2) {
        return (int) Math.sqrt(Math.pow(otherVec2.x - this.x, 2) + Math.pow(otherVec2.y - this.y, 2));
    }

    public Direction getRelativePositionToOther(Vec2 otherVec2) {
        Vec2 diff = otherVec2.subtract(this);
        if (diff.x() == 0) {
            return diff.y() > 0 ? Direction.UP : Direction.DOWN;
        } else if (diff.y() == 0) {
            return diff.x() > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return Direction.NONE;
        }
    }


    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}