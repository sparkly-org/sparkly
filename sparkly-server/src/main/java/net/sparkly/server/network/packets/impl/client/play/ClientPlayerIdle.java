package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPlayerIdle implements Packet.Client {

    private boolean onGround;

    public ClientPlayerIdle() {
    }

    public ClientPlayerIdle(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.onGround = buffer.readBoolean();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleIdle(this);
    }
    
    public boolean onGround() {
        return onGround;
    }
    
    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
