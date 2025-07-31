package net.sparkly.server.player;

import net.sparkly.api.flags.impl.TeleportFlags;
import net.sparkly.api.player.GameMode;
import net.sparkly.api.player.GameProfile;
import net.sparkly.api.player.Player;
import net.sparkly.api.position.Location;
import net.sparkly.api.world.Difficulty;
import net.sparkly.api.world.Dimension;
import net.sparkly.api.world.LevelType;
import net.sparkly.api.world.World;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.impl.server.play.ServerPositionAndLook;
import net.sparkly.server.network.packets.impl.server.play.ServerRespawn;

import java.util.UUID;

public class SparklyPlayer implements Player {
    
    private final MinecraftServer server;
    private final GameProfile gameProfile;
    private final PlayerConnection connection;
    private Location location;
    private double health;
    
    public SparklyPlayer(MinecraftServer server, GameProfile gameProfile, PlayerConnection connection) {
        this.server = server;
        this.gameProfile = gameProfile;
        this.connection = connection;
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
    public void teleport(Location newLocation) {
        this.location = newLocation;
        server.schedule(() -> {
            World currentWorld = location.world();
            
            if (newLocation.world() != currentWorld) {
                ServerConfig config = server.config();
                Difficulty difficulty = Difficulty.values()[config.difficulty()];
                
                if (currentWorld != null) {
                    ServerRespawn respawn = new ServerRespawn(Dimension.OVERWORLD, difficulty, GameMode.SURVIVAL, LevelType.DEFAULT);
                    
                    connection.sendPacket(respawn);
                }
            }
            
            ServerPositionAndLook teleport = new ServerPositionAndLook(newLocation, TeleportFlags.EMPTY);
            connection.sendPacket(teleport);
        });
    }
    
    @Override
    public World world() {
        return location.world();
    }
}
