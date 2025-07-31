package net.sparkly.server.config;

import net.kyori.adventure.text.Component;
import net.sparkly.server.MinecraftServer;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

public class ServerConfig {
    
    private final MinecraftServer server;
    private final Yaml yaml = new Yaml();
    
    public ServerConfig(MinecraftServer server) {
        this.server = server;
    }

    public void load() {
        File configDir = new File("configs");

        if (!configDir.exists()) configDir.mkdirs();

        for (Config config : Config.values()) {
            File file = new File(configDir, config.path());

            if (!file.exists()) {
                createFromResources(file);
            }

            try (InputStream fileStream = new FileInputStream(file)) {
                config.loadValues(yaml.load(fileStream));
            } catch (Exception e) {
                server.logger().error("Exception while reading {}", config.path(), e);
            }
        }
    }

    private void createFromResources(File file) {
        String resourcePath = "/configs/" + file.getName();

        try (InputStream resource = getClass().getResourceAsStream(resourcePath)) {
            if (resource == null) {
                throw new FileNotFoundException("Resource not found: " + resourcePath);
            }

            try (OutputStream out = new FileOutputStream(file)) {
                resource.transferTo(out);
            }

            server.logger().info("Created config file from resources: {}", file.getName());
        } catch (Exception e) {
            server.logger().error("Exception while creating {}", file.getName(), e);
        }
    }
    
    public Component message(String path) {
        String message = (String) Config.MESSAGES.configValues().get(path);
        return MinecraftServer.MINI_MESSAGE.deserialize(message);
    }
    
    public String brand() {
        return (String) Config.SERVER.configValues().get("brand");
    }
    
    public Component motd() {
        String motd = (String) Config.SERVER.configValues().get("motd");
        return MinecraftServer.MINI_MESSAGE.deserialize(motd);
    }
    
    public int port() {
        return (int) Config.SERVER.configValues().get("port");
    }
    
    public int tickingThreads() {
        return (int) Config.SERVER.configValues().get("ticking_threads");
    }
    
    public int nettyThreads() {
        return (int) Config.SERVER.configValues().get("netty_threads");
    }
    
    public String usernameFilter() {
        return (String) Config.SERVER.configValues().get("username_filter");
    }
    
    public String chatFormat() {
        return (String) Config.SERVER.configValues().get("chat_format");
    }
    
    public int tickRate() {
        return (int) Config.SERVER.configValues().get("tick_rate");
    }
    
    public String pingVersionHover() {
        return (String) Config.SERVER.configValues().get("ping_version_hover");
    }
    
    public int difficulty() {
        return (int) Config.SERVER.configValues().get("difficulty");
    }
    
    public int maxPlayers() {
        return (int) Config.SERVER.configValues().get("max_players");
    }
    
    public int renderDistance() {
        return (int) Config.SERVER.configValues().get("render_distance");
    }
}
