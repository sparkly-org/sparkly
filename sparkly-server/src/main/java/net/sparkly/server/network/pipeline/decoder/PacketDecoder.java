package net.sparkly.server.network.pipeline.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.model.ConnectionState;
import net.sparkly.server.network.model.PacketOwnership;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.pipeline.handler.StateHandler;

import java.util.List;
import java.util.Optional;

public class PacketDecoder extends ByteToMessageDecoder {
    
    private StateHandler stateHandler;
    
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        this.stateHandler = context.pipeline().get(StateHandler.class);
    }
    
    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> list) {
        NetworkBuffer buffer = new NetworkBuffer(byteBuf);
        ConnectionState state = stateHandler.state();
        
        int id = buffer.readVarInt();
        Optional<Packet> optionalPacket = state.registry(PacketOwnership.CLIENT).findPacket(id);
    
        if (optionalPacket.isEmpty()) {
            throw new IllegalStateException("Packet with id " + id + " at state " + state.name() + " not found!");
        }

        Packet.Client packet = (Packet.Client) optionalPacket.get();
        packet.read(buffer);

        if (buffer.remaining() > 0) {
            String message = String.format(
                "Packet %s/%d (%s) was larger than expected, found %d bytes extra whilst reading packet %d",
                state.name(), id, packet.getClass().getSimpleName(), buffer.remaining(), id
            );
            throw new IllegalStateException(message);
        }
        
        list.add(packet);
    }
}
