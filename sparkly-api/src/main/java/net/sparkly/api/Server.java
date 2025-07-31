package net.sparkly.api;

import net.kyori.adventure.text.Component;
import net.sparkly.api.player.Player;
import net.sparkly.api.world.World;

import java.util.List;

public interface Server {
    void start();

    void stop();

    boolean running();

    void broadcast(Component message);
    
    void schedule(Runnable task);
    
    List<World> worlds();
    
    List<Player> players();
}
