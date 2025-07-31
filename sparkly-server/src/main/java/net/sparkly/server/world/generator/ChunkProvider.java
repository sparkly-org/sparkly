package net.sparkly.server.world.generator;

import net.sparkly.server.world.generator.unit.GenerationUnit;

import java.util.function.Consumer;

public class ChunkProvider {

    private Consumer<GenerationUnit> generator;

    public void setGenerator(Consumer<GenerationUnit> generator) {
        this.generator = generator;
    }

    public Consumer<GenerationUnit> generator() {
        return generator;
    }
}
