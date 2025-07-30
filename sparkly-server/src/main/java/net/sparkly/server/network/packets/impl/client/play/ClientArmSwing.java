package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientArmSwing implements Packet.Client {

    public ClientArmSwing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleArmSwing(this);
    }
}
