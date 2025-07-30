package net.sparkly.api.world.chunk;

public interface ChunkSection {
    char[] blocks();
    
    char blockAt(int x, int y, int z);
    
    void setBlockAt(int x, int y, int z, char value);

    int nonAirBlocks();
}
