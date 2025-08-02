package net.sparkly.server.network.packets.impl.client.play;


import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientKeepAlive implements Packet.Client {

    private int id;

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readVarInt();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleKeepAlive(this);
    }

    public int id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
