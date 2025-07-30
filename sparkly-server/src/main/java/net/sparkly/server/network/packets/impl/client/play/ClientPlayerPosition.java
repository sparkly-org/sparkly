package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPlayerPosition extends ClientPlayerIdle {

    private double x;
    private double y;
    private double z;

    public ClientPlayerPosition() {
    }

    public ClientPlayerPosition(double x, double y, double z, boolean onGround) {
        super(onGround);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        super.read(buffer);
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handlePosition(this);
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
}
