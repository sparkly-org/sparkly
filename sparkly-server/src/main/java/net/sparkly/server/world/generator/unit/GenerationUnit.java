package net.sparkly.server.world.generator.unit;

import net.sparkly.api.block.Material;
import net.sparkly.api.position.Position;
import net.sparkly.api.world.World;
import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.api.world.chunk.ChunkSection;
import net.sparkly.server.block.SparklyBlock;
import net.sparkly.server.world.chunk.SparklyChunk;

public record GenerationUnit(World world) {
    
    private static final int CHUNK_SIZE = 16;
    private static final int CHUNK_RANGE = 16;
    
    public void fill(Material material) {
        fill(0, 256, material);
    }
    
    public void fill(int minHeight, int maxHeight, Material material) {
        for (int chunkX = -CHUNK_RANGE; chunkX < CHUNK_RANGE; chunkX++) {
            for (int chunkZ = -CHUNK_RANGE; chunkZ < CHUNK_RANGE; chunkZ++) {
                fillChunk(chunkX, chunkZ, minHeight, maxHeight, material);
            }
        }
    }
    
    private void fillChunk(int chunkX, int chunkZ, int minHeight, int maxHeight, Material material) {
        Chunk chunk = world.chunkAt(chunkX, chunkZ);
        
        if (chunk == null) {
            chunk = new SparklyChunk(chunkX, chunkZ);
        }
        
        for (int y = minHeight; y < maxHeight; y++) {
            ChunkSection section = chunk.sectionAt(y >> 4);
            
            fillSection(section, y & 15, material);
        }
        
        world.addChunk(chunkX, chunkZ, chunk);
    }
    
    private void fillSection(ChunkSection section, int y, Material material) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                SparklyBlock block = new SparklyBlock(material, new Position(x, y, z));
                section.setBlock(x, y, z, block.state());
            }
        }
    }
}