package net.sparkly.api.position;

public enum Facing {

    DOWN(0, 1, -1, "down"),
    UP(1, 0, -1, "up"),
    NORTH(2, 3, 2, "north"),
    SOUTH(3, 2, 0, "south"),
    WEST(4, 5, 1, "west"),
    EAST(5, 4, 3, "east");

    public final int index;
    public final int opposite;
    public final int horizontalIndex;
    public final String direction;

    Facing(int index, int opposite, int horizontalIndex, String direction) {
        this.index = index;
        this.opposite = opposite;
        this.horizontalIndex = horizontalIndex;
        this.direction = direction;
    }

    public int index() {
        return index;
    }

    public int opposite() {
        return opposite;
    }

    public int horizontalIndex() {
        return horizontalIndex;
    }

    public String direction() {
        return direction;
    }
}
