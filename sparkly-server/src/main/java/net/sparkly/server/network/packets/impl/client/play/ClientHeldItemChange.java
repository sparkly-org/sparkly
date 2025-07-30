package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientHeldItemChange implements Packet.Client {
    
    private int slot;
    
    public ClientHeldItemChange() {
    }
    
    public ClientHeldItemChange(int slot) {
        this.slot = slot;
    }
    
    @Override
    public void read(NetworkBuffer buffer) {
        this.slot = buffer.readShort();
    }
    
    @Override
    public void handle(PacketProcessor processor) {
        processor.handleHeldItemChange(this);
    }
    
    public int slot() {
        return slot;
    }
    
    public void setSlot(int slot) {
        this.slot = slot;
    }
}
