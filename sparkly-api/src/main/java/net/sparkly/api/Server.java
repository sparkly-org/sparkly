package net.sparkly.api;

import net.kyori.adventure.text.Component;
import net.sparkly.api.player.Player;
import net.sparkly.api.world.World;

import java.util.Collection;

public interface Server {
    void start();

    void stop();

    boolean running();

    void broadcast(Component message);

    Collection<World> worlds();

    Collection<Player> players();
}
