package net.sparkly.api.world;

import net.sparkly.api.block.Block;
import net.sparkly.api.world.chunk.Chunk;

import java.util.Collection;

public interface World {
    String name();
    
    void setBlock(int x, int y, int z, Block block);
    
    Block blockAt(int x, int y, int z);
    
    Collection<Chunk> chunks();
    
    Chunk chunkAt(int x, int z);
    
    void addChunk(int chunkX, int chunkZ, Chunk chunk);
}
