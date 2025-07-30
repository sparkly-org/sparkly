package net.sparkly.server.network.packets.impl.client.status;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientStatusRequest implements Packet.Client {

    public ClientStatusRequest() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleStatusRequest(this);
    }
}
