package net.sparkly.server.world.chunk;

import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.api.world.chunk.ChunkSection;

public class SparklyChunk implements Chunk {
    
    private final ChunkSection[] sections;
    private final int chunkX;
    private final int chunkZ;
    
    public SparklyChunk(int chunkX, int chunkZ) {
        this.sections = new SparklySection[16];
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;

        for (int i = 0; i < sections.length; i++) {
            sections[i] = new SparklySection();
        }
    }
    
    public void tick() {
        // TODO
    }

    @Override
    public int x() {
        return chunkX;
    }

    @Override
    public int z() {
        return chunkZ;
    }

    @Override
    public ChunkSection[] sections() {
        return sections;
    }
    
    @Override
    public ChunkSection sectionAt(int y) {
        return sections[y];
    }
}
