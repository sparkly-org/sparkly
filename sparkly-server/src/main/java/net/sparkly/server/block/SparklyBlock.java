package net.sparkly.server.block;

import net.sparkly.api.block.Block;
import net.sparkly.api.block.Material;
import net.sparkly.api.position.Position;

public final class SparklyBlock implements Block {
    
    private final Material material;
    private final Position position;
    private int data;
    
    public SparklyBlock(Material material, Position position) {
        this.material = material;
        this.position = position;
    }
    
    public static SparklyBlock fromCombined(int combinedId, Position position) {
        char id = (char) (combinedId >> 4);
        int data = combinedId & 15;
        
        Material material = Material.fromId(id);
        SparklyBlock block = new SparklyBlock(material, position);
        
        block.setData(data);
        return block;
    }
    
    @Override
    public Material material() {
        return material;
    }
    
    @Override
    public Position position() {
        return position;
    }
    
    @Override
    public int data() {
        return data;
    }
    
    @Override
    public char state() {
        return (char) (material.id() << 4 | data);
    }
    
    public void setData(int data) {
        this.data = data;
    }
}
