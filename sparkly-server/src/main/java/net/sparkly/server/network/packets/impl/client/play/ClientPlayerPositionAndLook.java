package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPlayerPositionAndLook extends ClientPlayerIdle {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public ClientPlayerPositionAndLook() {
    }

    public ClientPlayerPositionAndLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        super(onGround);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.yaw = buffer.readFloat();
        this.pitch = buffer.readFloat();
        super.read(buffer);
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handlePositionAndLook(this);
    }
    
    public double x() {
        return x;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public double y() {
        return y;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public double z() {
        return z;
    }
    
    public void setZ(double z) {
        this.z = z;
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
