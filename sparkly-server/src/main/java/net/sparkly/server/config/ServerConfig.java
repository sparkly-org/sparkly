package net.sparkly.server.config;

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
        ClassLoader classLoader = getClass().getClassLoader();
        
        for (Config config : Config.values()) {
            try (InputStream inputStream = classLoader.getResourceAsStream("configs/" + config.path())) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Configuration file " + config.path() + " was not found!");
                }
                
                config.loadValues(yaml.load(inputStream));
            } catch (Exception e) {
                server.logger().error("Exception while reading {}", config.path(), e);
            }
        }
    }
    
    private void createFromResources(File file) {
        String fileName = file.getName();
        
        try (InputStream resource = ServerConfig.class.getResourceAsStream("config/" + fileName);
             FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {
            
            if (resource == null) {
                throw new FileNotFoundException(fileName + " was not found.");
            }
            
            writer.write(new String(resource.readAllBytes()));
            writer.flush();
        } catch (Exception e) {
            server.logger().error("Exception while creating {}", fileName, e);
        }
    }
    
    public String brand() {
        return (String) Config.SERVER.configValues().get("brand");
    }
    
    public String motd() {
        return (String) Config.SERVER.configValues().get("motd");
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
    
    public int ticksPerSecond() {
        return (int) Config.SERVER.configValues().get("ticks_per_second");
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
