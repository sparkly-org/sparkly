package net.sparkly.server.world.generator.unit.impl;

import net.sparkly.api.block.Material;
import net.sparkly.server.world.generator.unit.GenerationUnit;

import java.util.function.Consumer;

public record FlatWorldGenerator(int height) implements Consumer<GenerationUnit> {
    
    @Override
    public void accept(GenerationUnit unit) {
        unit.fill(0, 1, Material.BEDROCK);
        unit.fill(1, height - 1, Material.DIRT);
        unit.fill(height - 1, height, Material.GRASS);
    }
}
