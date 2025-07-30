package net.sparkly.server.network.packets.impl.client.login;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientLoginStart implements Packet.Client {

    private String name;

    public ClientLoginStart() {
    }

    public ClientLoginStart(String name) {
        this.name = name;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.name = buffer.readString();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleLoginStart(this);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
