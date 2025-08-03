package net.sparkly.server.network.pipeline.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sparkly.api.player.Player;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;
import net.sparkly.server.player.SparklyPlayer;

import java.util.List;

public class PacketHandler extends SimpleChannelInboundHandler<Packet.Client> {

    private final MinecraftServer server;
    private PacketProcessor packetProcessor;

    public PacketHandler(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        Channel channel = context.channel();
        PlayerConnection connection = new PlayerConnection(server, channel, null);

        this.packetProcessor = new PacketProcessor(server, connection);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext context) {
        List<Player> players = server.players();

        for (Player player : players) {
            SparklyPlayer wrapped = (SparklyPlayer) player;

            if (wrapped.connection().channel().equals(context.channel())) {
                players.remove(wrapped);
                return;
            }
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet.Client packet) {
        packet.handle(packetProcessor);
    }
}
