package net.sparkly.server.network.packets.impl.client.play;

import io.netty.buffer.Unpooled;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPluginMessage implements Packet.Client {

    private String channel;
    private NetworkBuffer data;

    public ClientPluginMessage() {
    }

    public ClientPluginMessage(String channel, NetworkBuffer data) {
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.channel = buffer.readString();
        int length = buffer.remaining();
        
        NetworkBuffer data = new NetworkBuffer(Unpooled.buffer(65536));
        data.writeBytes(buffer.readBytes(length));
        
        this.data = data;
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handlePluginMessage(this);
    }
    
    public String channel() {
        return channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    public NetworkBuffer data() {
        return data;
    }
    
    public void setData(NetworkBuffer data) {
        this.data = data;
    }
}
