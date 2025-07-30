package net.sparkly.api.world.chunk;

public interface Chunk {
    int x();

    int z();

    ChunkSection[] sections();
    
    ChunkSection sectionAt(int y);
}
