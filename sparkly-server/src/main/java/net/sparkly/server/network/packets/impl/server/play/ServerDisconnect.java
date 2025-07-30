package net.sparkly.server.network.packets.impl.server.play;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerDisconnect implements Packet.Server {

    private Component reason;

    public ServerDisconnect() {
    }

    public ServerDisconnect(Component reason) {
        this.reason = reason;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(GsonComponentSerializer.gson().serialize(this.reason));
    }

    public Component reason() {
        return reason;
    }
}
