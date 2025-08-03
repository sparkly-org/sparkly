package net.sparkly.server.player;

import net.sparkly.api.entity.MobType;
import net.sparkly.api.flags.impl.TeleportFlags;
import net.sparkly.api.enums.GameMode;
import net.sparkly.api.player.profile.GameProfile;
import net.sparkly.api.player.Player;
import net.sparkly.api.position.Location;
import net.sparkly.api.position.Vector;
import net.sparkly.api.world.Difficulty;
import net.sparkly.api.world.Dimension;
import net.sparkly.api.world.LevelType;
import net.sparkly.api.world.World;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.entity.SparklyEntity;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.impl.server.play.ServerPositionAndLook;
import net.sparkly.server.network.packets.impl.server.play.ServerRespawn;

import java.util.UUID;

public class SparklyPlayer extends SparklyEntity implements Player {

    private final MinecraftServer server;
    private final GameProfile gameProfile;
    private final PlayerConnection connection;

    private int lastKeepAliveId;
    private long lastKeepAliveTime;
    private long lastKeepAliveReceived;

    public SparklyPlayer(GameProfile gameProfile, PlayerConnection connection, MinecraftServer server, Location location, int entityId) {
        super(server, location, MobType.PLAYER, entityId);
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
    public void tick() {
        // TODO
    }

    @Override
    public void teleport(Location location) {
        super.teleport(location);
        server.schedule(() -> {
            World currentWorld = world();

            if (location.world() != currentWorld) {
                ServerConfig config = server.config();
                Difficulty difficulty = Difficulty.values()[config.difficulty()];

                if (currentWorld != null) {
                    ServerRespawn respawn = new ServerRespawn(Dimension.OVERWORLD, difficulty, GameMode.SURVIVAL, LevelType.DEFAULT);
                    connection.sendPacket(respawn);
                }
            }

            ServerPositionAndLook teleport = new ServerPositionAndLook(location, TeleportFlags.EMPTY);
            connection.sendPacket(teleport);
        });
    }

    public PlayerConnection connection() {
        return connection;
    }

    public int lastKeepAliveId() {
        return lastKeepAliveId;
    }

    public void setLastKeepAliveId(int lastKeepAliveId) {
        this.lastKeepAliveId = lastKeepAliveId;
    }

    public long lastKeepAliveTime() {
        return lastKeepAliveTime;
    }

    public void setLastKeepAliveTime(long lastKeepAliveTime) {
        this.lastKeepAliveTime = lastKeepAliveTime;
    }

    public long lastKeepAliveReceived() {
        return lastKeepAliveReceived;
    }

    public void setLastKeepAliveReceived(long lastKeepAliveReceived) {
        this.lastKeepAliveReceived = lastKeepAliveReceived;
    }

    public boolean isTimedOut() {
        return System.currentTimeMillis() - lastKeepAliveReceived > 30000; // 30 seconds timeout
    }
}
