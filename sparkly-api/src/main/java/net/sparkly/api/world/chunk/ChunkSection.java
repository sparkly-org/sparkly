package net.sparkly.api.world.chunk;

import net.sparkly.api.block.Block;

public interface ChunkSection {
    char[] blocks();
    
    char blockAt(int x, int y, int z);
    
    void setBlockAt(int x, int y, int z, char value);
}
