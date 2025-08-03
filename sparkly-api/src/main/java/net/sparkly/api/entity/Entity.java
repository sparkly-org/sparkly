package net.sparkly.api.entity;

import net.sparkly.api.entity.metadata.Metadata;
import net.sparkly.api.position.Location;
import net.sparkly.api.position.Vector;
import net.sparkly.api.world.World;

import java.util.List;

public interface Entity {
    World world();

    Location location();

    Vector velocity();

    MobType type();

    List<Metadata<?>> metadata();

    int entityId();

    double health();

    void tick();

    void setHealth(double health);

    void setLocation(Location location);

    void teleport(Location location);
}