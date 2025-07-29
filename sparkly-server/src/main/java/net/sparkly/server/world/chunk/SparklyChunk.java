package net.sparkly.server.world.chunk;

import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.api.world.chunk.ChunkSection;

public class SparklyChunk implements Chunk {
    
    private final ChunkSection[] sections;
    
    public SparklyChunk() {
        this.sections = new SparklySection[16];
        
        for (int i = 0; i < sections.length; i++) {
            sections[i] = new SparklySection();
        }
    }
    
    public void tick() {
        // TODO
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
