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

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}