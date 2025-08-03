package net.sparkly.server.ticking;

import net.sparkly.server.MinecraftServer;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.impl.server.play.ServerKeepAlive;
import net.sparkly.server.player.SparklyPlayer;

import java.util.Random;
import java.util.SplittableRandom;

public class KeepAliveThread implements Runnable {

    private final MinecraftServer server;
    private final Random random = Random.from(new SplittableRandom());
    private int tickCounter = 0;

    public KeepAliveThread(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        tickCounter++;

        if (tickCounter % 20 != 0) {
            return;
        }

        for (var player : server.players()) {
            if (!(player instanceof SparklyPlayer sparklyPlayer)) {
                continue;
            }

            PlayerConnection connection = sparklyPlayer.connection();

            if (connection == null || !connection.channel().isActive()) {
                continue;
            }

            int keepAliveId = this.random.nextInt();
            sparklyPlayer.setLastKeepAliveId(keepAliveId);
            sparklyPlayer.setLastKeepAliveTime(System.currentTimeMillis());

            connection.sendPacket(new ServerKeepAlive(keepAliveId));
        }
    }
}
