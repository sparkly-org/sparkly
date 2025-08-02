package net.sparkly.api.player;

import net.sparkly.api.position.Location;
import net.sparkly.api.world.World;

import java.util.UUID;

public interface Player {
    String name();
    
    void setName(String name);
    
    UUID uuid();
    
    void setUuid(UUID uuid);
    
    double health();
    
    void setHealth(double health);

    Location location();

    void setLocation(Location location);

    void teleport(Location location);

    World world();
}
