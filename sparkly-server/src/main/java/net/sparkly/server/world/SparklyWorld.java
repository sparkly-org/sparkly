package net.sparkly.server.world;

import net.sparkly.api.block.Block;
import net.sparkly.api.block.Material;
import net.sparkly.api.position.Position;
import net.sparkly.api.world.World;
import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.api.world.chunk.ChunkSection;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.block.SparklyBlock;
import net.sparkly.server.ticking.TickScheduler;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SparklyWorld implements World {
    
    private final Map<Long, Chunk> chunks;
    private final String name;
    private final TickScheduler tickScheduler;
    
    public SparklyWorld(MinecraftServer server, String name) {
        this.name = name;
        this.chunks = new HashMap<>();
        this.tickScheduler = new TickScheduler(server, this);
    }
    
    public void tick() {
        tickScheduler.run();
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
        
        section.setBlockAt(x & 15, y & 15, z & 15, block.state());
    }
    
    @Override
    public Block blockAt(int x, int y, int z) {
        Chunk chunk = chunkAt(x >> 4, z >> 4);
        
        if (chunk == null) return null;
        
        ChunkSection section = chunk.sectionAt(y >> 4);
        
        if (section == null) return null;
        
        char data = section.blockAt(x & 15, y & 15, z & 15);

        Position position = new Position(x, y, z);
        Material material = Material.fromCombinedId(data);
        
        return new SparklyBlock(material, position);
    }
    
    @Override
    public Collection<Chunk> chunks() {
        return chunks.values();
    }
    
    @Override
    public Chunk chunkAt(int x, int z) {
        return chunks.get(chunkId(x, z));
    }
    
    private long chunkId(int x, int z) {
        return ((long) x << 32L) | z & 0xFFFFFFFFL;
    }
}
