package net.sparkly.server.network.packets.impl.server.login;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerLoginDisconnect implements Packet.Server {

    private Component reason;

    public ServerLoginDisconnect() {
    }

    public ServerLoginDisconnect(Component reason) {
        this.reason = reason;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(GsonComponentSerializer.gson().serialize(reason));
    }
    
    public Component reason() {
        return reason;
    }
}
