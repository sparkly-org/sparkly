package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.api.position.Vector;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerSpawnPosition implements Packet.Server {

    private Vector vector;

    public ServerSpawnPosition() {
    }

    public ServerSpawnPosition(Vector vector) {
        this.vector = vector;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writePosition(vector);
    }
}
