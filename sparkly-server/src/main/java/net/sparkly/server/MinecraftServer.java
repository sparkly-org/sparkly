package net.sparkly.server;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sparkly.server.config.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinecraftServer {
    
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    private final Logger logger = LogManager.getLogger(MinecraftServer.class);
    private final ServerConfig config;

    private ExecutorService tickingService;

    public MinecraftServer() {
        this.config = new ServerConfig(this);
    }
    
    public void start() {
        long start = System.nanoTime();
        logger.info("Booting up sparkly...");
        
        config.load();
        logger.info("Configuration loaded");

        tickingService = Executors.newFixedThreadPool(config.tickingThreads());
        logger.info("Created thread pool for ticking");

        long took = System.nanoTime() - start;
        double tookSeconds = took / 1e9;
        
        logger.info("Sparkly has started in {} seconds.", String.format("%.3f", tookSeconds));
    }
    
    public Logger logger() {
        return logger;
    }
    
    public ExecutorService tickingService() {
        return tickingService;
    }
    
    public ServerConfig config() {
        return config;
    }
}
