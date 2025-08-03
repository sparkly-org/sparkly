package net.sparkly.server.world;

import net.sparkly.api.block.Block;
import net.sparkly.api.position.Vector;
import net.sparkly.api.world.World;
import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.api.world.chunk.ChunkSection;
import net.sparkly.server.block.SparklyBlock;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SparklyWorld implements World {
    
    private final Map<Long, Chunk> chunks;
    private final String name;

    public SparklyWorld(String name) {
        this.name = name;
        this.chunks = new HashMap<>();
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public void setBlock(int x, int y, int z, Block block) {
        Chunk chunk = chunkAt(x >> 4, z >> 4); // >> 4 = / 16
        
        if (chunk == null) return;
        
        ChunkSection section = chunk.sectionAt(y >> 4);
    
        if (section == null) return;
        
        section.setBlock(x & 15, y & 15, z & 15, block.state());
    }
    
    @Override
    public Block blockAt(int x, int y, int z) {
        Chunk chunk = chunkAt(x >> 4, z >> 4);
        
        if (chunk == null) return null;
        
        ChunkSection section = chunk.sectionAt(y >> 4);
        
        if (section == null) return null;
        
        char data = section.blockAt(x & 15, y & 15, z & 15);
        
        Vector vector = new Vector(x, y, z);
        return SparklyBlock.fromCombined(data, vector);
    }
    
    @Override
    public Collection<Chunk> chunks() {
        return chunks.values();
    }
    
    @Override
    public Chunk chunkAt(int x, int z) {
        return chunks.get(chunkId(x, z));
    }
    
    @Override
    public void addChunk(int chunkX, int chunkZ, Chunk chunk) {
        chunks.put(chunkId(chunkX, chunkZ), chunk);
    }
    
    private long chunkId(int x, int z) {
        return ((long) x << 32L) | z & 0xFFFFFFFFL;
    }
}
