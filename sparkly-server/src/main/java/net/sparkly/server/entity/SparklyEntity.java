package net.sparkly.server.entity;

import net.sparkly.api.entity.Entity;
import net.sparkly.api.entity.MobType;
import net.sparkly.api.entity.metadata.Metadata;
import net.sparkly.api.position.Location;
import net.sparkly.api.position.Vector;
import net.sparkly.api.world.World;
import net.sparkly.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class SparklyEntity implements Entity {

    protected final MinecraftServer server;
    protected final int entityId;

    protected List<Metadata<?>> metadata = new ArrayList<>();
    protected Vector velocity = new Vector(0, 0, 0);
    protected Location location;
    protected MobType type;
    private double health;

    public SparklyEntity(MinecraftServer server, Location location, MobType type, int entityId) {
        this.server = server;
        this.entityId = entityId;
        this.type = type;
        this.location = location;
    }

    @Override
    public World world() {
        return location.world();
    }

    @Override
    public Location location() {
        return location;
    }

    @Override
    public Vector velocity() {
        return velocity;
    }

    @Override
    public MobType type() {
        return type;
    }

    @Override
    public List<Metadata<?>> metadata() {
        return metadata;
    }

    @Override
    public int entityId() {
        return entityId;
    }

    @Override
    public double health() {
        return health;
    }

    @Override
    public void tick() {
        // TODO
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void teleport(Location location) {
        setLocation(location);
    }
}
