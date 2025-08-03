package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.api.enums.ChatMode;
import net.sparkly.api.player.profile.GameSettings;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientSettings implements Packet.Client {

    private GameSettings settings;

    public ClientSettings() {
    }

    public ClientSettings(GameSettings settings) {
        this.settings = settings;
    }

    public ClientSettings(String locale, byte viewDistance, ChatMode chatMode, boolean chatColors, byte skinParts) {
        this(new GameSettings(locale, chatMode, chatColors, viewDistance, skinParts));
    }

    @Override
    public void read(NetworkBuffer buffer) {
        String locale = buffer.readString();
        byte viewDistance = buffer.readByte();
        ChatMode chatMode = ChatMode.values()[buffer.readVarInt()];
        boolean chatColors = buffer.readBoolean();
        byte skinParts = buffer.readByte();

        this.settings = new GameSettings(locale, chatMode, chatColors, viewDistance, skinParts);
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleSettings(this);
    }
    
    public GameSettings settings() {
        return settings;
    }
    
    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }
}
