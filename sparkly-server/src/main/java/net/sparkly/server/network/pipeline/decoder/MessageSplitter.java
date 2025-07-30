package net.sparkly.server.network.pipeline.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.sparkly.server.network.NetworkBuffer;

import java.util.List;

public class MessageSplitter extends ByteToMessageDecoder {
    
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) {
        NetworkBuffer buffer = new NetworkBuffer(byteBuf);
        
        if (buffer.remaining() <= 0) return;
        
        buffer.markReader();
        int length = buffer.readVarInt();
        
        if (buffer.remaining() >= length) {
            byte[] bytes = buffer.readBytes(length);
            list.add(Unpooled.wrappedBuffer(bytes));
            return;
        }
        
        buffer.resetReader();
    }
}
