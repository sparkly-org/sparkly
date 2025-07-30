package net.sparkly.server.network.pipeline;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.sparkly.server.network.pipeline.decoder.MessageSplitter;
import net.sparkly.server.network.pipeline.decoder.PacketDecoder;
import net.sparkly.server.network.pipeline.encoder.MessagePrepender;
import net.sparkly.server.network.pipeline.encoder.PacketEncoder;
import net.sparkly.server.network.pipeline.handler.PacketHandler;
import net.sparkly.server.network.pipeline.handler.StateHandler;

public class MinecraftPipeline extends ChannelInitializer<SocketChannel> {
    
    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelPipeline pipeline = channel.pipeline();
        
        pipeline.addLast("timeout", new ReadTimeoutHandler(30));
        pipeline.addLast("state_handler", new StateHandler());
        
        // Reading packets
        pipeline.addLast("splitter", new MessageSplitter());
        pipeline.addLast("decoder", new PacketDecoder());
        
        // Writing packets
        pipeline.addLast("prepender", new MessagePrepender());
        pipeline.addLast("encoder", new PacketEncoder());
        
        pipeline.addLast("packet_handler", new PacketHandler());
    }
}
