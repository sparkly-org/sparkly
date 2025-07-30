package net.sparkly.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.network.pipeline.MinecraftPipeline;

public class NetworkManager {

    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final MinecraftServer server;
    private final EventLoopGroup bossGroup;
    private final EventLoopGroup workerGroup;
    private Channel serverChannel;
    
    public NetworkManager(MinecraftServer server) {
        this.server = server;
        
        ServerConfig config = server.config();
        IoHandlerFactory nioHandler = NioIoHandler.newFactory();

        int threads = config.nettyThreads();

        if (threads == -1) {
            threads = Runtime.getRuntime().availableProcessors() * 2;
        }
        
        this.bossGroup = new MultiThreadIoEventLoopGroup(1, nioHandler);
        this.workerGroup = new MultiThreadIoEventLoopGroup(threads, nioHandler);
    }
    
    public void start() {
        ServerConfig config = server.config();
        
        try {
            ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new MinecraftPipeline(server, channelGroup));
            
            ChannelFuture future = bootstrap.bind(config.port()).sync();
            this.serverChannel = future.channel();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Netty bind interrupted", e);
        } catch (Exception e) {
            throw new RuntimeException("Netty failed to start", e);
        }
    }
    
    public void shutdown() {
        if (serverChannel != null) {
            serverChannel.close();
        }
        
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public ChannelGroup allChannels() {
        return channelGroup;
    }

    public MinecraftServer server() {
        return server;
    }

    public EventLoopGroup bossGroup() {
        return bossGroup;
    }

    public EventLoopGroup workerGroup() {
        return workerGroup;
    }

    public Channel serverChannel() {
        return serverChannel;
    }
}
