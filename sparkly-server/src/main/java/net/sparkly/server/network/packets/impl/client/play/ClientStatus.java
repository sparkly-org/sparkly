package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientStatus implements Packet.Client {

    public enum Action {
        PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY
    }

    private Action action;

    public ClientStatus() {
    }

    public ClientStatus(Action action) {
        this.action = action;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.action = Action.values()[buffer.readVarInt()];
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleStatus(this);
    }
    
    public Action action() {
        return action;
    }
    
    public void setAction(Action action) {
        this.action = action;
    }
}
