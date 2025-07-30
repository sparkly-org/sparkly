package net.sparkly.server.network.pipeline.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.model.ConnectionState;
import net.sparkly.server.network.model.PacketOwnership;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.PacketRegistry;
import net.sparkly.server.network.pipeline.handler.StateHandler;

import java.util.Optional;

public class PacketEncoder extends MessageToByteEncoder<Packet.Server> {
    
    private StateHandler stateHandler;
    
    @Override
    public void handlerAdded(ChannelHandlerContext context) {
        this.stateHandler = context.pipeline().get(StateHandler.class);
    }
    
    @Override
    protected void encode(ChannelHandlerContext context, Packet.Server packet, ByteBuf out) {
        NetworkBuffer buffer = new NetworkBuffer(out);
        ConnectionState state = stateHandler.state();
        
        PacketRegistry packetRegistry = state.registry(PacketOwnership.SERVER);
        Optional<Integer> optionalPacketId = packetRegistry.findId(packet);
        
        if (optionalPacketId.isEmpty()) {
            throw new IllegalArgumentException(
                "Packet " + packet.getClass().getSimpleName() + " is not registered for state " + state.name()
            );
        }

        buffer.writeVarInt(optionalPacketId.get());
        packet.write(buffer);
    }
}
