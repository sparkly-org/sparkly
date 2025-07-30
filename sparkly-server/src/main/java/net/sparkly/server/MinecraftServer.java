package net.sparkly.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sparkly.api.Server;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.event.EventNode;
import net.sparkly.api.event.Event;
import net.sparkly.server.network.NetworkManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinecraftServer implements Server {
    
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    private final Logger logger = LogManager.getLogger(MinecraftServer.class);
    private final ServerConfig config;
    private final EventNode<Event> eventHandler;
    private NetworkManager networkManager;
    private ExecutorService tickingService;

    public MinecraftServer() {
        this.config = new ServerConfig(this);
        this.eventHandler = new EventNode<>();
    }
    
    public void start() {
        
        long start = System.nanoTime();
        logger.info("Booting up sparkly...");
        
        config.load();
        logger.info("Configuration loaded");
        
        tickingService = Executors.newFixedThreadPool(config.tickingThreads());
        logger.info("Created thread pool for ticking");
        
        networkManager = new NetworkManager(this);
        networkManager.start();
        
        logger.info("Server listening on port {}", config.port());

        long took = System.nanoTime() - start;
        double tookSeconds = took / 1e9;
        
        logger.info("Sparkly has started in {} seconds.", String.format("%.3f", tookSeconds));
    }
    
    public Logger logger() {
        return logger;
    }
    
    public ServerConfig config() {
        return config;
    }
    
    public EventNode<Event> eventHandler() {
        return eventHandler;
    }
    
    public ExecutorService tickingService() {
        return tickingService;
    }
    
    @Override
    public void broadcast(Component message) {
    
    }
}
