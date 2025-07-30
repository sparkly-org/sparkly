package net.sparkly.server.ticking;

import net.sparkly.api.world.World;
import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.network.NetworkManager;
import net.sparkly.server.world.chunk.SparklyChunk;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.locks.LockSupport;

public class GameLoopThread extends Thread {

    private final MinecraftServer server;

    public GameLoopThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        Runnable runnable = () -> {
            int tickRate = server.config().tickRate();
            long nanos = 1_000_000_000 / tickRate;

            while (server.running()) {
                LockSupport.parkNanos(nanos);
                tick();
            }
        };

        Thread.ofPlatform().name("game-loop-worker").daemon().start(runnable);
    }

    private void tick() {
        ExecutorService executor = server.tickingService();
        NetworkManager networkManager = server.networkManager();

        for (World world : server.worlds()) {
            tickWorld(world, executor);
        }

        networkManager.allChannels().flush();
    }

    private void tickWorld(World world, ExecutorService executor) {
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
