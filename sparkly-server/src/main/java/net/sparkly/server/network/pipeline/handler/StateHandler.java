package net.sparkly.server.network.pipeline.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import net.sparkly.server.network.model.ConnectionState;

public class StateHandler extends ChannelInboundHandlerAdapter {
    
    private ConnectionState state = ConnectionState.HANDSHAKING;
    
    public ConnectionState state() {
        return state;
    }
    
    public void setState(ConnectionState state) {
        this.state = state;
    }
}
