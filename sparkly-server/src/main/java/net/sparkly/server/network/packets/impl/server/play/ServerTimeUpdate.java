package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerTimeUpdate implements Packet.Server {

    private int worldAge;
    private int timeOfDay;

    public ServerTimeUpdate() {
    }

    public ServerTimeUpdate(int worldAge, int timeOfDay) {
        this.worldAge = worldAge;
        this.timeOfDay = timeOfDay;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeLong(worldAge);
        buffer.writeLong(timeOfDay);
    }
}
