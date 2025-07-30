package net.sparkly.server.network.packets.impl.server.play;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerChatMessage implements Packet.Server {

    private Component component;
    private MessageType messageType;

    public ServerChatMessage() {
    }

    public ServerChatMessage(Component component, MessageType messageType) {
        this.component = component;
        this.messageType = messageType;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(GsonComponentSerializer.gson().serialize(component));
        buffer.writeByte(messageType.ordinal());
    }

    public enum MessageType {
        CHAT, SYSTEM_MESSAGE, HOTBAR
    }
}
