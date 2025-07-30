package net.sparkly.server.network.packets;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public interface Packet {
    interface Client extends Packet {
        void read(NetworkBuffer buffer);
        
        void handle(PacketProcessor processor);
    }
    
    interface Server extends Packet {
        void write(NetworkBuffer buffer);
    }
}
