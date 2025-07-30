package net.sparkly.server.network.pipeline.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sparkly.server.network.packets.Packet;

public class PacketHandler extends SimpleChannelInboundHandler<Packet> {
    
    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
    
    }
}
