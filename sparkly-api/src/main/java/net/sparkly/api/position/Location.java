package net.sparkly.api.position;

import net.sparkly.api.world.World;

public class Location extends Position {

    private World world;
    private float yaw;
    private float pitch;

    public Location() {
        super(0, 0, 0);
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(World world, float yaw, float pitch) {
        this(world, 0, 0, 0, yaw, pitch);
    }

    public World world() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public float yaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float pitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
