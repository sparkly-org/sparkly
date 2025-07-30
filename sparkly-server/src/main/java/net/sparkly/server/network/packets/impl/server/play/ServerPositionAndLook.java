package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.api.flags.impl.TeleportFlags;
import net.sparkly.api.position.Location;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerPositionAndLook implements Packet.Server {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private TeleportFlags flags;

    public ServerPositionAndLook() {
    }

    public ServerPositionAndLook(Location location, TeleportFlags flags) {
        this(location.x(), location.y(), location.z(), location.yaw(), location.pitch(), flags);
    }

    public ServerPositionAndLook(double x, double y, double z, float yaw, float pitch, TeleportFlags flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeByte(flags.getMask());
    }
}
