package net.sparkly.server.event.impl;

import net.sparkly.api.event.Cancellable;
import net.sparkly.api.event.Event;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.Packet;

public class PacketSendEvent extends Cancellable implements Event {
    
    private final PlayerConnection connection;
    private final Packet.Server packet;
    
    public PacketSendEvent(PlayerConnection connection, Packet.Server packet) {
        this.connection = connection;
        this.packet = packet;
    }
    
    public PlayerConnection connection() {
        return connection;
    }
    
    public Packet.Server packet() {
        return packet;
    }
}
