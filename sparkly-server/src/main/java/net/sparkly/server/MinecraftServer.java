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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MinecraftServer implements Server {
    
    public static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    
    private final Logger logger = LogManager.getLogger(MinecraftServer.class);
    private final List<World> worlds = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();

    private final ServerConfig config;
    private final EventNode<Event> eventHandler;
    private final GameLoopThread gameLoop;

    private NetworkManager networkManager;
    private ExecutorService tickingService;
    private boolean running;

    public MinecraftServer() {
        this.config = new ServerConfig(this);
        this.eventHandler = new EventNode<>();
        this.gameLoop = new GameLoopThread(this);
    }
    
    public void start() {
        long start = System.nanoTime();

        this.running = true;
        logger.info("Booting up sparkly...");

        config.load();
        logger.info("Loaded the configuration");
        
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

    public Logger logger() {
        return logger;
    }

    public ServerConfig config() {
        return config;
    }

    public EventNode<Event> eventHandler() {
        return eventHandler;
    }

    public NetworkManager networkManager() {
        return networkManager;
    }

    public ExecutorService tickingService() {
        return tickingService;
    }

    @Override
    public void broadcast(Component message) {
        ServerChatMessage packet = new ServerChatMessage(message, ServerChatMessage.MessageType.CHAT);
        networkManager.allChannels().write(packet);
    }

    @Override
    public Collection<World> worlds() {
        return worlds;
    }

    @Override
    public Collection<Player> players() {
        return players;
    }
}
