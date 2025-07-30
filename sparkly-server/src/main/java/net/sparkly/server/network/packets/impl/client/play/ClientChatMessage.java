package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientChatMessage implements Packet.Client {

    private String message;

    public ClientChatMessage() {
    }

    public ClientChatMessage(String message) {
        this.message = message;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.message = buffer.readString();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleChatMessage(this);
    }
    
    public String message() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
