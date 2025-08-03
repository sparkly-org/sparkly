package net.sparkly.server.entity;

import net.sparkly.api.entity.metadata.Metadata;
import net.sparkly.api.position.Vector;
import net.sparkly.server.network.NetworkBuffer;

import java.util.ArrayList;
import java.util.List;

public class SparklyMetadata<T> implements Metadata<T> {

    private final int type;
    private final int id;
    private T value;

    public SparklyMetadata(int type, int id, T value) {
        this.type = type;
        this.id = id;
        this.value = value;
    }

    public static List<Metadata<?>> read(NetworkBuffer buffer) {
        List<Metadata<?>> metadataList = new ArrayList<>();

        for (int i = buffer.readByte(); i != 127; i = buffer.readByte()) {
            int type = (i & 0b11100000) >> 5; // get the 3 highest bits
            int id = i & 0b11111; // get the 5 lowest bits
            
            Metadata<?> metadata = switch (type) {
                case 0 -> new SparklyMetadata<>(type, id, buffer.readByte());
                case 1 -> new SparklyMetadata<>(type, id, buffer.readShort());
                case 2 -> new SparklyMetadata<>(type, id, buffer.readInt());
                case 3 -> new SparklyMetadata<>(type, id, buffer.readFloat());
                case 4 -> new SparklyMetadata<>(type, id, buffer.readString());
                case 5 -> null; // TODO
                case 6 ->
                    new SparklyMetadata<>(type, id, new Vector(buffer.readInt(), buffer.readInt(), buffer.readInt()));
                case 7 ->
                    new SparklyMetadata<>(type, id, new Vector(buffer.readFloat(), buffer.readFloat(), buffer.readFloat()));
                default -> null;
            };
            
            metadataList.add(metadata);
        }
        
        return metadataList;
    }

    public static void write(NetworkBuffer buffer, Metadata<?> metadata) {
        int type = (metadata.type() << 5 | metadata.id() & 31) & 255;
        buffer.writeByte(type);
        
        switch (metadata.type()) {
            case 0 -> buffer.writeByte((byte) metadata.value());
            case 1 -> buffer.writeShort((short) metadata.value());
            case 2 -> buffer.writeInt((int) metadata.value());
            case 3 -> buffer.writeFloat((float) metadata.value());
            case 4 -> buffer.writeString((String) metadata.value());
            case 5 -> {}
            case 6 -> {
                Vector vector = (Vector) metadata.value();
                buffer.writeInt(vector.blockX());
                buffer.writeInt(vector.blockY());
                buffer.writeInt(vector.blockZ());
            }
            case 7 -> {
                Vector vector = (Vector) metadata.value();
                buffer.writeFloat((float) vector.x());
                buffer.writeFloat((float) vector.y());
                buffer.writeFloat((float) vector.z());
            }
        }
    }

    @Override
    public int type() {
        return type;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public T value() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
