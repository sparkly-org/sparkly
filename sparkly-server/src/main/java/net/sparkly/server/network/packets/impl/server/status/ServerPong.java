package net.sparkly.server.network.packets.impl.server.status;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerPong implements Packet.Server {

    private long id;

    public ServerPong() {
    }

    public ServerPong(long id) {
        this.id = id;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeLong(id);
    }
}
