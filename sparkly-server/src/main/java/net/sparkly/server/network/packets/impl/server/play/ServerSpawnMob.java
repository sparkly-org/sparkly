package net.sparkly.server.network.packets.impl.server.play;

import net.sparkly.api.entity.Entity;
import net.sparkly.api.entity.metadata.Metadata;
import net.sparkly.api.position.Location;
import net.sparkly.api.position.Vector;
import net.sparkly.server.entity.SparklyMetadata;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;

public class ServerSpawnMob implements Packet.Server {

    private Entity entity;

    public ServerSpawnMob() {
    }

    public ServerSpawnMob(Entity entity) {
        this.entity = entity;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        Location location = entity.location();
        Vector velocity = entity.velocity();

        buffer.writeVarInt(entity.entityId());
        buffer.writeByte(entity.type().id());
        buffer.writeInt((int) (location.x() * 32));
        buffer.writeInt((int) (location.y() * 32));
        buffer.writeInt((int) (location.z() * 32));
        buffer.writeByte((int) (location.yaw() * 256 / 360));
        buffer.writeByte((int) (location.pitch() * 256 / 360));
        buffer.writeByte((int) (location.yaw() * 256 / 360));
        buffer.writeShort((int) (velocity.x() * 8000));
        buffer.writeShort((int) (velocity.y() * 8000));
        buffer.writeShort((int) (velocity.z() * 8000));

        for (Metadata<?> metadata : entity.metadata()) {
            SparklyMetadata.write(buffer, metadata);
        }

        buffer.writeByte(127);
    }
}
