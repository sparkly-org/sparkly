package net.sparkly.server.network.packets.impl.client.status;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPing implements Packet.Client {

    private long id;

    public ClientPing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readLong();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handlePing(this);
    }
    
    public long id() {
        return id;
    }
}
