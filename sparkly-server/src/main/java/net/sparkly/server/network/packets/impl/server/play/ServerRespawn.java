package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.api.enums.GameMode;
import net.sparkly.api.world.Difficulty;
import net.sparkly.api.world.Dimension;
import net.sparkly.api.world.LevelType;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerRespawn implements Packet.Server {

    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private LevelType levelType;

    public ServerRespawn() {
    }

    public ServerRespawn(Dimension dimension, Difficulty difficulty, GameMode gameMode, LevelType levelType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.levelType = levelType;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeInt(dimension.ordinal());
        buffer.writeByte(difficulty.ordinal());
        buffer.writeByte(gameMode.ordinal());
        buffer.writeString(levelType.name().toLowerCase());
    }
}
