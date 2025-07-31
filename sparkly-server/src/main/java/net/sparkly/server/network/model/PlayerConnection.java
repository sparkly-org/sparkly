package net.sparkly.server.network.model;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.event.impl.PacketSendEvent;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.impl.server.login.ServerLoginDisconnect;
import net.sparkly.server.network.packets.impl.server.play.ServerDisconnect;
import net.sparkly.server.network.pipeline.handler.StateHandler;
import net.sparkly.server.player.SparklyPlayer;

import java.util.concurrent.ExecutionException;

public final class PlayerConnection {
    
    private final MinecraftServer server;
    private final Channel channel;
    private final StateHandler stateHandler;
    private SparklyPlayer player;

    public PlayerConnection(MinecraftServer server, Channel channel, SparklyPlayer player) {
        this.server = server;
        this.channel = channel;
        this.player = player;
        this.stateHandler = channel.pipeline().get(StateHandler.class);
    }
    
    public void sendPacket(Packet.Server packet, GenericFutureListener<? extends Future<? super Void>> callback) {
        if (channel == null || !channel.isActive()) return;
        
        PacketSendEvent event = new PacketSendEvent(this, packet);
        server.eventHandler().call(event);
        
        if (event.cancelled()) return;
        
        channel.write(packet).addListener(callback);
    }
    
    public void sendPacket(Packet.Server packet) {
        if (channel == null || !channel.isActive()) return;
        
        PacketSendEvent event = new PacketSendEvent(this, packet);
        server.eventHandler().call(event);
        
        if (event.cancelled()) return;
        
        channel.write(packet);
    }
    
    public void close(Component reason) {
        StateHandler handler = channel.pipeline().get(StateHandler.class);
        ConnectionState state = handler.state();
        
        Packet.Server packet = state == ConnectionState.LOGIN
            ? new ServerLoginDisconnect(reason)
            : new ServerDisconnect(reason);
        
        if (channel.isOpen()) {
            channel.writeAndFlush(packet);
        }
        
        channel.close();
    }
    
    public void flushPacket(Packet.Server packet) {
        if (channel == null || !channel.isOpen()) return;
        
        try {
            channel.writeAndFlush(packet).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void setConnectionState(ConnectionState state) {
        stateHandler.setState(state);
    }
    
    public MinecraftServer server() {
        return server;
    }
    
    public Channel channel() {
        return channel;
    }

    public StateHandler stateHandler() {
        return stateHandler;
    }

    public SparklyPlayer player() {
        return player;
    }

    public void setPlayer(SparklyPlayer player) {
        this.player = player;
    }
}
