package net.sparkly.server.network.packets.processor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.sparkly.api.entity.Entity;
import net.sparkly.api.entity.MobType;
import net.sparkly.api.flags.impl.TeleportFlags;
import net.sparkly.api.enums.GameMode;
import net.sparkly.api.player.profile.GameProfile;
import net.sparkly.api.position.Location;
import net.sparkly.api.world.Difficulty;
import net.sparkly.api.world.Dimension;
import net.sparkly.api.world.LevelType;
import net.sparkly.api.world.World;
import net.sparkly.api.world.chunk.Chunk;
import net.sparkly.server.entity.SparklyEntity;
import net.sparkly.server.event.impl.PlayerChatEvent;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.event.impl.PlayerHandshakeEvent;
import net.sparkly.server.network.model.ConnectionState;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.impl.client.handshake.ClientHandshake;
import net.sparkly.server.network.packets.impl.client.status.ClientPing;
import net.sparkly.server.network.packets.impl.client.status.ClientStatusRequest;
import net.sparkly.server.network.packets.impl.client.login.ClientLoginStart;
import net.sparkly.server.network.packets.impl.client.play.*;
import net.sparkly.server.network.packets.impl.server.login.ServerLoginSuccess;
import net.sparkly.server.network.packets.impl.server.play.*;
import net.sparkly.server.network.packets.impl.server.status.ServerPong;
import net.sparkly.server.network.packets.impl.server.status.ServerStatusResponse;
import net.sparkly.server.player.SparklyPlayer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public record PacketProcessor(MinecraftServer server, PlayerConnection connection) {

    private static final ExecutorService LOGIN_THREAD = createService("Async Login Thread - #%d");
    private static final ExecutorService CHAT_THREAD = createService("Async Chat Thread - #%d");
    
    private static ExecutorService createService(String name) {
        return Executors.newFixedThreadPool(2, getThreadFactory(name));
    }
    
    private static ThreadFactory getThreadFactory(String name) {
        return new ThreadFactoryBuilder()
            .setDaemon(true)
            .setNameFormat(name)
            .build();
    }
    
    public void handleHandshake(ClientHandshake packet) {
        PlayerHandshakeEvent event = new PlayerHandshakeEvent(
            packet.address(),
            packet.port(),
            packet.protocolVersion(),
            packet.nextState()
        );

        server.eventHandler().call(event);

        if (event.cancelled()) return;

        ConnectionState nextConnectionState = switch (event.state()) {
            case LOGIN -> ConnectionState.LOGIN;
            case STATUS -> ConnectionState.STATUS;
        };

        connection.setConnectionState(nextConnectionState);
    }
    
    public void handlePing(ClientPing packet) {
        connection.flushPacket(new ServerPong(packet.id()));
    }
    
    public void handleStatusRequest(ClientStatusRequest packet) {
        ServerConfig config = server.config();

        ServerStatusResponse response = new ServerStatusResponse(
            new ServerStatusResponse.Players(config.maxPlayers(), 5),
            new ServerStatusResponse.Version(config.pingVersionHover(), 47),
            new ServerStatusResponse.Description(config.motd())
        );

        connection.sendPacket(response);
    }
    
    public void handleLoginStart(ClientLoginStart packet) {
        LOGIN_THREAD.submit(() -> {
            Channel channel = connection.channel();
            ServerConfig config = server.config();

            String name = packet.getName();
            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));

            if (!name.matches(config.usernameFilter())) {
                connection.close(Component.text("Invalid username!").color(NamedTextColor.RED));
                return;
            }

            World world = server.worlds().getFirst();

            if (world == null) {
                connection.close(config.message("missing_world").color(NamedTextColor.RED));
                return;
            }

            int entityId = server.random().nextInt(Integer.MAX_VALUE);
            Location location = new Location(world, 0, 64, 0, 0, 0);
            GameProfile profile = new GameProfile(uuid, name);
            SparklyPlayer player = new SparklyPlayer(profile, connection, server, location, entityId);

            server.logger().info("{} ({}) logged in", name, channel.remoteAddress());

            GenericFutureListener<? extends Future<? super Void>> postLoginSuccess = future -> {
                connection.setConnectionState(ConnectionState.PLAY);

                Difficulty difficulty = Difficulty.values()[config.difficulty()];

                connection.sendPacket(new ServerJoinGame(0, GameMode.CREATIVE, Dimension.OVERWORLD, difficulty,
                    config.maxPlayers(), LevelType.DEFAULT, false));
                connection.sendPacket(new ServerSpawnPosition(location));
                connection.sendPacket(new ServerPositionAndLook(location, TeleportFlags.EMPTY));

                server.players().add(player);

                List<Chunk> chunks = new ArrayList<>();
                List<ServerChunkDataBulk> chunkDataBulks = new ArrayList<>();

                int renderDistance = config.renderDistance() / 2;
                int totalSize = 0;
                int limit = 1800000;

                for (int x = -renderDistance; x < renderDistance; x++) {
                    for (int z = -renderDistance; z < renderDistance; z++) {
                        Chunk chunk = world.chunkAt(x, z);

                        if (chunk == null) continue;

                        ServerChunkData.Extracted extractedData = ServerChunkData.extractData(chunk,
                            true, true, 65535);

                        int dataSize = extractedData.data.length;

                        if (totalSize + dataSize < limit) {
                            totalSize += dataSize;
                        } else {
                            chunkDataBulks.add(new ServerChunkDataBulk(chunks));
                            chunks.clear();
                            totalSize = 0;
                        }

                        chunks.add(chunk);
                    }
                }

                if (!chunks.isEmpty()) {
                    chunkDataBulks.add(new ServerChunkDataBulk(chunks));
                }

                for (Packet.Server bulk : chunkDataBulks) {
                    connection.sendPacket(bulk);
                }

                Entity pig = new SparklyEntity(server, location, MobType.PIG, server.random().nextInt(Integer.MAX_VALUE));
                connection.sendPacket(new ServerSpawnMob(pig));
            };

            connection.sendPacket(new ServerLoginSuccess(uuid, name), postLoginSuccess);
        });
    }

    public void handleKeepAlive(ClientKeepAlive packet) {
        SparklyPlayer player = connection.player();
        if (player == null) return;

        if (packet.id() == player.lastKeepAliveId()) {
            player.setLastKeepAliveReceived(System.currentTimeMillis());
        }
    }

    public void handleChatMessage(ClientChatMessage packet) {
        CHAT_THREAD.submit(() -> {
            SparklyPlayer player = connection.player();
            ServerConfig config = server.config();
            
            PlayerChatEvent event = new PlayerChatEvent(player, packet.message());
            server.eventHandler().call(event);
            
            if (event.cancelled()) return;
            
            TextComponent component = Component.text(String.format(config.chatFormat(), player.name(), event.message()));
            server.broadcast(component);
        });
    }
    
    public void handleUseEntity(ClientUseEntity packet) {
    
    }

    public void handleIdle(ClientPlayerIdle packet) {
        SparklyPlayer player = connection.player();
        if (player == null) return;

        Location location = player.location();
        if (location != null) {
            // Update only the on ground status
            Location newLocation = new Location(location.world(), location.x(), location.y(), location.z(),
                    location.yaw(), location.pitch());
            player.setLocation(newLocation);
        }
    }

    public void handlePosition(ClientPlayerPosition packet) {
        SparklyPlayer player = connection.player();
        if (player == null) return;

        Location location = player.location();
        if (location == null) return;

        double x = location.x();
        double y = location.y();
        double z = location.z();

        double newX = packet.x();
        double newY = packet.y();
        double newZ = packet.z();

        if (Double.isInfinite(newX) || Double.isNaN(newX)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        if (Double.isInfinite(newY) || Double.isNaN(newY)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        if (Double.isInfinite(newZ) || Double.isNaN(newZ)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        double deltaX = Math.abs(newX - x);
        double deltaY = Math.abs(newY - y);
        double deltaZ = Math.abs(newZ - z);

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        if (distance > 10) {
            player.teleport(new Location(location.world(), x, y, z, location.yaw(), location.pitch()));
        } else {
            Location newLocation = new Location(location.world(), newX, newY, newZ, location.yaw(), location.pitch());
            player.setLocation(newLocation);
        }
    }

    public void handleLook(ClientPlayerLook packet) {
        SparklyPlayer player = connection.player();
        if (player == null) return;

        Location location = player.location();
        if (location == null) return;

        float yaw = packet.yaw();
        float pitch = packet.pitch();

        if (Double.isInfinite(yaw) || Double.isNaN(yaw)) {
            connection.close(Component.text("Invalid rotation.").color(NamedTextColor.RED));
            return;
        }

        if (Double.isInfinite(pitch) || Double.isNaN(pitch)) {
            connection.close(Component.text("Invalid rotation.").color(NamedTextColor.RED));
            return;
        }

        Location newLocation = new Location(location.world(), location.x(), location.y(), location.z(), yaw, pitch);
        player.setLocation(newLocation);
    }

    public void handlePositionAndLook(ClientPlayerPositionAndLook packet) {
        SparklyPlayer player = connection.player();
        if (player == null) return;

        Location location = player.location();
        if (location == null) return;

        double x = location.x();
        double y = location.y();
        double z = location.z();

        double newX = packet.x();
        double newY = packet.y();
        double newZ = packet.z();

        float yaw = packet.yaw();
        float pitch = packet.pitch();

        boolean invalidX = Double.isInfinite(newX) || Double.isNaN(newX);
        boolean invalidY = Double.isInfinite(newY) || Double.isNaN(newY);
        boolean invalidZ = Double.isInfinite(newZ) || Double.isNaN(newZ);
        boolean invalidYaw = Double.isInfinite(yaw) || Double.isNaN(yaw);
        boolean invalidPitch = Double.isInfinite(pitch) || Double.isNaN(pitch);

        if (invalidX || invalidY || invalidZ || invalidYaw || invalidPitch) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        double deltaX = Math.abs(newX - x);
        double deltaY = Math.abs(newY - y);
        double deltaZ = Math.abs(newZ - z);

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        // Basic anti-cheat: if player moves too fast, teleport them back
        if (distance > 10) {
            player.teleport(new Location(location.world(), x, y, z, yaw, pitch));
        } else {
            Location newLocation = new Location(location.world(), newX, newY, newZ, yaw, pitch);
            player.setLocation(newLocation);
        }
    }

    public void handleHeldItemChange(ClientHeldItemChange packet) {
//        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparklyPlayer player = connection.player();

        if (packet.slot() > 8 || packet.slot() < 0) {
            connection.close(Component.text("Invalid slot.").color(NamedTextColor.RED));
            return;
        }

//        player.getInventory().setHeldItemSlot(packet.getSlot());
    }
    
    public void handleArmSwing(ClientArmSwing packet) {
    
    }
    
    public void handlePlayerAbilities(ClientPlayerAbilities packet) {
    
    }
    
    public void handleSettings(ClientSettings packet) {
    
    }
    
    public void handleStatus(ClientStatus packet) {
    
    }
    
    public void handlePluginMessage(ClientPluginMessage packet) {
//        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;
//
//        SparkyPlayer player = connection.getPlayer();
//
//        if (packet.getChannel().equals("MC|Brand")) {
//            NetworkBuffer data = packet.getData();
//            String brand = data.readString();
//
//            player.setClientBrand(brand);
//        }
    }
}
