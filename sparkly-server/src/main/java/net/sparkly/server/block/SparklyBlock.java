package net.sparkly.server.block;

import net.sparkly.api.block.Block;
import net.sparkly.api.block.Material;
import net.sparkly.api.position.Vector;

public final class SparklyBlock implements Block {
    
    private final Material material;
    private final Vector vector;
    private int data;
    
    public SparklyBlock(Material material, Vector vector) {
        this.material = material;
        this.vector = vector;
    }
    
    public static SparklyBlock fromCombined(int combinedId, Vector vector) {
        char id = (char) (combinedId >> 4);
        int data = combinedId & 15;
        
        Material material = Material.fromId(id);
        SparklyBlock block = new SparklyBlock(material, vector);
        
        block.setData(data);
        return block;
    }
    
    @Override
    public Material material() {
        return material;
    }
    
    @Override
    public Vector position() {
        return vector;
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
