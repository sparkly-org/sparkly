package net.sparkly.server.network.packets.impl.client.handshake;

import net.sparkly.server.event.impl.PlayerHandshakeEvent;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientHandshake implements Packet.Client {

    private String address;
    private int protocolVersion;
    private int port;
    private PlayerHandshakeEvent.HandshakeState nextState;

    public ClientHandshake() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.address = buffer.readString();
        this.port = buffer.readUnsignedShort();
        this.nextState = PlayerHandshakeEvent.HandshakeState.values()[buffer.readVarInt() - 1];
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleHandshake(this);
    }
    
    public String address() {
        return address;
    }
    
    public int protocolVersion() {
        return protocolVersion;
    }
    
    public int port() {
        return port;
    }
    
    public PlayerHandshakeEvent.HandshakeState nextState() {
        return nextState;
    }
}
