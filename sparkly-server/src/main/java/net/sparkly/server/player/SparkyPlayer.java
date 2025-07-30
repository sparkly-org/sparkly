package net.sparkly.server.player;

import net.sparkly.api.player.GameProfile;
import net.sparkly.api.player.Player;
import net.sparkly.api.position.Location;
import net.sparkly.api.world.World;

import java.util.UUID;

public class SparkyPlayer implements Player {
    
    private final GameProfile gameProfile;
    private Location location;
    private double health;
    
    public SparkyPlayer(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }
    
    @Override
    public String name() {
        return gameProfile.username();
    }
    
    @Override
    public void setName(String name) {
        gameProfile.setName(name);
    }
    
    @Override
    public UUID uuid() {
        return gameProfile.uuid();
    }
    
    @Override
    public void setUuid(UUID uuid) {
        gameProfile.setUuid(uuid);
    }
    
    @Override
    public double health() {
        return health;
    }
    
    @Override
    public void setHealth(double health) {
        this.health = health;
    }
    
    @Override
    public Location location() {
        return location;
    }
    
    @Override
    public void teleport(Location location) {
        this.location = location;
    }
    
    @Override
    public World world() {
        return location.world();
    }
}
