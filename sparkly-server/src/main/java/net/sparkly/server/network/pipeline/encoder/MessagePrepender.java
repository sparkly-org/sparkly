package net.sparkly.server.network.pipeline.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.sparkly.server.network.NetworkBuffer;

public class MessagePrepender extends MessageToByteEncoder<ByteBuf> {
    
    @Override
    protected void encode(ChannelHandlerContext context, ByteBuf inputBuf, ByteBuf out) {
        int length = inputBuf.readableBytes();
        NetworkBuffer buffer = new NetworkBuffer(out);
        
        buffer.writeVarInt(length);
        buffer.writeBytes(inputBuf.array());
    }
}
