package net.sparkly.server.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import net.sparkly.api.position.Position;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record NetworkBuffer(ByteBuf buffer) {
    
    public NetworkBuffer() {
        this(ByteBufAllocator.DEFAULT.buffer());
    }
    
    public int remaining() {
        return buffer.readableBytes();
    }
    
    public void markReader() {
        buffer.markReaderIndex();
    }
    
    public void resetReader() {
        buffer.resetReaderIndex();
    }
    
    public void markWriter() {
        buffer.markWriterIndex();
    }
    
    public void resetWriter() {
        buffer.resetWriterIndex();
    }
    
    public byte readByte() {
        return buffer.readByte();
    }
    
    public void writeByte(int value) {
        buffer.writeByte(value);
    }
    
    public short readShort() {
        return buffer.readShort();
    }
    
    public void writeShort(int value) {
        buffer.writeShort(value);
    }
    
    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }
    
    public int readInt() {
        return buffer.readInt();
    }
    
    public void writeInt(int value) {
        buffer.writeInt(value);
    }
    
    public long readLong() {
        return buffer.readLong();
    }
    
    public void writeLong(long value) {
        buffer.writeLong(value);
    }
    
    public float readFloat() {
        return buffer.readFloat();
    }
    
    public void writeFloat(float value) {
        buffer.writeFloat(value);
    }
    
    public double readDouble() {
        return buffer.readDouble();
    }
    
    public void writeDouble(double value) {
        buffer.writeDouble(value);
    }
    
    public String readString() {
        int length = readVarInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }
    
    public void writeString(String value) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        buffer.writeBytes(bytes);
    }
    
    public int readVarInt() {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = buffer.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            
            numRead++;
            if (numRead > 5) throw new RuntimeException("VarInt too big");
        } while ((read & 0b10000000) != 0);
        
        return result;
    }
    
    public void writeVarInt(int value) {
        while ((value & 0xFFFFFF80) != 0L) {
            buffer.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buffer.writeByte(value);
    }
    
    public long readVarLong() {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = buffer.readByte();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));
            
            numRead++;
            if (numRead > 10) throw new RuntimeException("VarLong too big");
        } while ((read & 0b10000000) != 0);
        
        return result;
    }
    
    public void writeVarLong(long value) {
        while ((value & ~0x7FL) != 0L) {
            buffer.writeByte(((int) value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buffer.writeByte((int) value);
    }
    
    public void writeBytes(byte[] bytes) {
        buffer.writeBytes(bytes);
    }
    
    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        buffer.readBytes(bytes);
        return bytes;
    }
    
    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }
    
    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }
    
    public void writeBoolean(boolean value) {
        buffer.writeBoolean(value);
    }
    
    public boolean readBoolean() {
        return buffer.readBoolean();
    }
    
    public void writePosition(Position position) {
        long encoded = ((long) (position.blockX() & 0x3FFFFFF) << 38)
            | ((long) (position.blockZ() & 0x3FFFFFF) << 12)
            | (position.blockY() & 0xFFF);
        writeLong(encoded);
    }
    
    public Position readPosition() {
        long value = readLong();
        int x = (int) (value >> 38);
        int y = (int) (value << 52 >> 52);
        int z = (int) (value << 26 >> 38);
        return new Position(x, y, z);
    }
}