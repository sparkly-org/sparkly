package net.sparkly.server.ticking;

import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.world.SparklyWorld;
import net.sparkly.server.world.chunk.SparklyChunk;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public record TickScheduler(MinecraftServer server, SparklyWorld world) implements Runnable {

    private static final int DEFAULT_BATCH_SIZE = 16;

    @Override
    public void run() {
        Collection<Chunk> chunks = world.chunks();

        if(chunks.isEmpty()){
            return;
        }

        ExecutorService executorService = server.tickingService();

        int availableThreads = getAvailableProcessors(executorService);
        int total = chunks.size();

        int batchSize = calculateBatchSize(total, availableThreads);


        SparklyChunk[] chunkArray = chunks.toArray(new SparklyChunk[0]);

        AtomicInteger completedBatches = new AtomicInteger(0);
        int numBatches = (total + batchSize - 1) / batchSize;

        CompletableFuture<?>[] futures = new CompletableFuture[numBatches];

        for (int i = 0; i < total; i += batchSize) {
            final int start = i;
            final int end = Math.min(i + batchSize, total);
            final int batchIndex = i / batchSize;

            futures[batchIndex] = CompletableFuture.runAsync(() -> {
                try {
                    processBatch(chunkArray, start, end);
                }catch (Exception e){
                    server.logger().error("Error processing chunk batch [{}-{})", start, end, e);
                } finally {
                    completedBatches.incrementAndGet();
                }
            }, executorService).exceptionally(throwable -> {
                server.logger().error("Error processing chunk batch [{}-{})", start, end, throwable);
                return null;
            });
        }

        try {
            CompletableFuture.allOf(futures).get(5, TimeUnit.SECONDS);
        }catch (TimeoutException exception){
            server.logger().error("Timeout while processing chunk batches", exception);
        }catch (InterruptedException | ExecutionException exception){
            if (!Thread.currentThread().isInterrupted()){
                Thread.currentThread().interrupt();
            }
            server.logger().error("Error while processing chunk batches", exception);
        } finally {
            if (!Thread.currentThread().isInterrupted()){
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processBatch(SparklyChunk[] chunks, int start, int end){
        for (int i = start; i < end; i++) {
            chunks[i].tick();
        }
    }

    public int calculateBatchSize(int totalChunks, int aviableProcessors) {

        if (totalChunks <= aviableProcessors){
            return 1;
        }

        int batchSize = Math.max(1, totalChunks / aviableProcessors);

        return Math.min(batchSize, DEFAULT_BATCH_SIZE);
    }

    private int getAvailableProcessors(ExecutorService executorService) {
        if (executorService instanceof ThreadPoolExecutor executor){
            return executor.getMaximumPoolSize();
        }

        return Runtime.getRuntime().availableProcessors();
    }
}
