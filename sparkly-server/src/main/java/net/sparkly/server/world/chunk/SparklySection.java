package net.sparkly.server.world.chunk;

import net.sparkly.api.block.Block;
import net.sparkly.api.world.chunk.ChunkSection;
import net.sparkly.server.block.SparklyBlock;

public class SparklySection implements ChunkSection {
    
    public char[] blocks;
    
    public SparklySection() {
        this.blocks = new char[4096];
    }
    
    @Override
    public char[] blocks() {
        return blocks;
    }
    
    @Override
    public char blockAt(int x, int y, int z) {
        return blocks[y << 8 | z << 4 | x];
    }
    
    @Override
    public void setBlockAt(int x, int y, int z, char value) {
        blocks[y << 8 | z << 4 | x] = value;
    }
}
