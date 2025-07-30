package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.api.position.Position;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerSpawnPosition implements Packet.Server {

    private Position position;

    public ServerSpawnPosition() {
    }

    public ServerSpawnPosition(Position position) {
        this.position = position;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writePosition(position);
    }
}
