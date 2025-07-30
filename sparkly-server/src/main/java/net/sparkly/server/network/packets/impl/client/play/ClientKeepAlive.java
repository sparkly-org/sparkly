package net.sparkly.server.network.packets.impl.client.play;


import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientKeepAlive implements Packet.Client {

    private long id;

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readVarInt();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleKeepAlive(this);
    }
    
    public long id() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
}
