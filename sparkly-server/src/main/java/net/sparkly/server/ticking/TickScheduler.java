package net.sparkly.server.ticking;

import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.world.SparklyWorld;
import net.sparkly.server.world.chunk.SparklyChunk;

import java.util.List;
import java.util.concurrent.*;

public record TickScheduler(MinecraftServer server, SparklyWorld world) implements Runnable {
    
    @Override
    public void run() {
        ExecutorService executor = server.tickingService();
        List<Chunk> chunks = List.copyOf(world.chunks());
        
        int total = chunks.size();
        int batchSize = 16;
        
        int numBatches = (total + batchSize - 1) / batchSize;
        CountDownLatch latch = new CountDownLatch(numBatches);
        
        for (int i = 0; i < total; i += batchSize) {
            int start = i;
            int end = Math.min(i + batchSize, total);
            
            executor.execute(() -> {
                for (int j = start; j < end; j++) {
                    SparklyChunk chunk = (SparklyChunk) chunks.get(j);
                    chunk.tick();
                }
                
                latch.countDown();
            });
        }
        
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
