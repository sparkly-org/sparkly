package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerKeepAlive implements Packet.Server {
    
    private int id;
    
    public ServerKeepAlive() {
    }
    
    public ServerKeepAlive(int id) {
        this.id = id;
    }
    
    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeVarInt(id);
    }
    
    public int id() {
        return id;
    }
}
