package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPlayerLook extends ClientPlayerIdle {

    private float yaw;
    private float pitch;

    public ClientPlayerLook() {
    }

    public ClientPlayerLook(float yaw, float pitch, boolean onGround) {
        super(onGround);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.yaw = buffer.readFloat();
        this.pitch = buffer.readFloat();
        super.read(buffer);
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleLook(this);
    }
    
    public float yaw() {
        return yaw;
    }
    
    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
    
    public float pitch() {
        return pitch;
    }
    
    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
