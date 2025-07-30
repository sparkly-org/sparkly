package net.sparkly.server.network.packets.impl.server.status;

import net.sparkly.api.player.GameMode;
import net.sparkly.api.world.Difficulty;
import net.sparkly.api.world.Dimension;
import net.sparkly.api.world.LevelType;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerJoinGame implements Packet.Server {

    private int entityId;
    private GameMode gameMode;
    private Dimension dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private LevelType levelType;
    private boolean reducedDebugInfo;

    public ServerJoinGame() {
    }

    public ServerJoinGame(int entityId, GameMode gameMode, Dimension dimension, Difficulty difficulty, int maxPlayers, LevelType levelType, boolean reducedDebugInfo) {
        this.entityId = entityId;
        this.gameMode = gameMode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.reducedDebugInfo = reducedDebugInfo;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeInt(entityId);
        buffer.writeByte(gameMode.ordinal());
        buffer.writeByte(dimension.ordinal());
        buffer.writeByte(difficulty.ordinal());
        buffer.writeByte(maxPlayers);
        buffer.writeString(levelType.name().toLowerCase());
        buffer.writeBoolean(reducedDebugInfo);
    }
    
    public int entityId() {
        return entityId;
    }
    
    public GameMode gameMode() {
        return gameMode;
    }
    
    public Dimension dimension() {
        return dimension;
    }
    
    public Difficulty difficulty() {
        return difficulty;
    }
    
    public int maxPlayers() {
        return maxPlayers;
    }
    
    public LevelType levelType() {
        return levelType;
    }
    
    public boolean reducedDebugInfo() {
        return reducedDebugInfo;
    }
}
