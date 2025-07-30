package net.sparkly.server.network.packets.impl.server.login;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

import java.util.UUID;

public class ServerLoginSuccess implements Packet.Server {

    private UUID uniqueId;
    private String username;

    public ServerLoginSuccess() {
    }

    public ServerLoginSuccess(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(uniqueId.toString());
        buffer.writeString(username);
    }
    
    public UUID uniqueId() {
        return uniqueId;
    }
    
    public String username() {
        return username;
    }
}
