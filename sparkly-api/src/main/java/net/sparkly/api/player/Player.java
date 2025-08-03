package net.sparkly.api.player;

import net.sparkly.api.entity.Entity;
import net.sparkly.api.position.Location;
import net.sparkly.api.world.World;

import java.util.UUID;

public interface Player extends Entity {
    String name();

    UUID uuid();

    void setName(String name);

    void setUuid(UUID uuid);
}
