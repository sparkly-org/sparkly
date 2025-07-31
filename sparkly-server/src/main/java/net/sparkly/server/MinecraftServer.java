package net.sparkly.server;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.sparkly.api.Server;
import net.sparkly.api.player.Player;
import net.sparkly.api.world.World;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.event.EventNode;
import net.sparkly.api.event.Event;
import net.sparkly.server.network.NetworkManager;
import net.sparkly.server.network.packets.impl.server.play.ServerChatMessage;
import net.sparkly.server.ticking.GameLoopThread;
import net.sparkly.server.world.SparklyWorld;
import net.sparkly.server.world.generator.unit.GenerationUnit;
import net.sparkly.server.world.generator.unit.impl.FlatWorldGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MinecraftServer implements Server {
    
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    private final Logger logger = LogManager.getLogger(MinecraftServer.class);
    private final List<World> worlds = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();

    private final ServerConfig config;
    private final EventNode<Event> eventHandler;
    private final GameLoopThread gameLoop;

    private Consumer<GenerationUnit> chunkGenerator;
    private NetworkManager networkManager;
    private ExecutorService tickingService;
    private boolean running;

    public MinecraftServer() {
        this.chunkGenerator = new FlatWorldGenerator(4);
        this.config = new ServerConfig(this);
        this.eventHandler = new EventNode<>();
        this.gameLoop = new GameLoopThread(this);
    }
    
    private void generateDefaultWorld() {
        logger.info("Generating the default world...");
        
        long start = System.currentTimeMillis();
        
        SparklyWorld world = new SparklyWorld("world");
        GenerationUnit unit = new GenerationUnit(world);
        
        chunkGenerator.accept(unit);
        worlds.add(world);
        
        long end = System.currentTimeMillis();
        double tookSeconds = (end - start) / 1000.0;
        
        logger.info("Generated world '{}' in {} seconds!", world.name(), String.format("%.3f", tookSeconds));
    }
    
    @Override
    public void start() {
        long start = System.nanoTime();

        this.running = true;
        logger.info("Booting up sparkly...");

        config.load();
        logger.info("Loaded the configuration");
        
        if (worlds.isEmpty()) {
            logger.info("No worlds found!");
            generateDefaultWorld();
        }
        
        this.tickingService = Executors.newFixedThreadPool(config.tickingThreads());
        this.networkManager = new NetworkManager(this);

        networkManager.start();
        gameLoop.start();

        long took = System.nanoTime() - start;
        double tookSeconds = took / 1e9;

        logger.info("Server bound on port {}", config.port());
        logger.info("Sparkly has loaded in {} seconds.", String.format("%.3f", tookSeconds));
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public boolean running() {
        return running;
    }

    @Override
    public void broadcast(Component message) {
        ServerChatMessage packet = new ServerChatMessage(message, ServerChatMessage.MessageType.CHAT);
        networkManager.allChannels().write(packet);
    }
    
    @Override
    public void schedule(Runnable task) {
        gameLoop.addTask(task);
    }
    
    @Override
    public List<World> worlds() {
        return worlds;
    }

    @Override
    public List<Player> players() {
        return players;
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
    
    public GameLoopThread gameLoop() {
        return gameLoop;
    }
    
    public Consumer<GenerationUnit> chunkGenerator() {
        return chunkGenerator;
    }
    
    public void setChunkGenerator(Consumer<GenerationUnit> chunkGenerator) {
        this.chunkGenerator = chunkGenerator;
    }
    
    public NetworkManager networkManager() {
        return networkManager;
    }
    
    public void setNetworkManager(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }
    
    public ExecutorService tickingService() {
        return tickingService;
    }
    
    public void setTickingService(ExecutorService tickingService) {
        this.tickingService = tickingService;
    }
    
    public void setRunning(boolean running) {
        this.running = running;
    }
}
