package net.sparkly.server.network.pipeline;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.network.pipeline.decoder.MessageSplitter;
import net.sparkly.server.network.pipeline.decoder.PacketDecoder;
import net.sparkly.server.network.pipeline.encoder.MessagePrepender;
import net.sparkly.server.network.pipeline.encoder.PacketEncoder;
import net.sparkly.server.network.pipeline.handler.PacketHandler;
import net.sparkly.server.network.pipeline.handler.StateHandler;

public class MinecraftPipeline extends ChannelInitializer<SocketChannel> {

    private final MinecraftServer server;
    private final ChannelGroup channelGroup;

    public MinecraftPipeline(MinecraftServer server, ChannelGroup channelGroup) {
        this.server = server;
        this.channelGroup = channelGroup;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        channelGroup.add(channel);
        
        pipeline.addLast("timeout", new ReadTimeoutHandler(30))
            .addLast("state_handler", new StateHandler())
            // Reading packets
            .addLast("splitter", new MessageSplitter())
            .addLast("decoder", new PacketDecoder())
            // Writing packets
            .addLast("prepender", new MessagePrepender())
            .addLast("encoder", new PacketEncoder())
            // Handling
            .addLast("packet_handler", new PacketHandler(server));
    }
}
