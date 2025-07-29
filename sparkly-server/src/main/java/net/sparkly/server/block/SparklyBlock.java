package net.sparkly.server.block;

import net.sparkly.api.block.Block;
import net.sparkly.api.block.Material;
import net.sparkly.api.position.Position;

public record SparklyBlock(Material material, Position position) implements Block {
    
    @Override
    public char state() {
        return material.state();
    }
}
