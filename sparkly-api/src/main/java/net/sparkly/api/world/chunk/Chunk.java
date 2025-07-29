package net.sparkly.api.world.chunk;

public interface Chunk {
    ChunkSection[] sections();
    
    ChunkSection sectionAt(int y);
}
