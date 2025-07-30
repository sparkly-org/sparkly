package net.sparkly.server.network.packets.processor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.sparkly.server.event.impl.PlayerChatEvent;
import net.sparkly.server.MinecraftServer;
import net.sparkly.server.config.ServerConfig;
import net.sparkly.server.event.impl.PlayerHandshakeEvent;
import net.sparkly.server.network.model.ConnectionState;
import net.sparkly.server.network.model.PlayerConnection;
import net.sparkly.server.network.packets.impl.client.handshake.ClientHandshake;
import net.sparkly.server.network.packets.impl.client.status.ClientPing;
import net.sparkly.server.network.packets.impl.client.status.ClientStatusRequest;
import net.sparkly.server.network.packets.impl.client.login.ClientLoginStart;
import net.sparkly.server.network.packets.impl.client.play.*;
import net.sparkly.server.network.packets.impl.server.status.ServerPong;
import net.sparkly.server.network.packets.impl.server.status.ServerStatusResponse;
import net.sparkly.server.player.SparkyPlayer;

import java.awt.*;
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
//            Channel channel = connection.getChannel();
//            ServerConfig config = server.getConfig();
//
//            String name = packet.getName();
//            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));
//
//            AsyncPreLoginEvent event = new AsyncPreLoginEvent(name, uuid);
//            server.getEventHandler().call(event);
//
//            if (event.isCancelled()) {
//                connection.close(null);
//                return;
//            }
//
//            if (!name.matches(config.getUsernameFormat())) {
//                connection.close(Component.text("Invalid username!").color(NamedTextColor.RED));
//                return;
//            }
//
//            SparkyPlayer player = connection.getPlayer();
//            GameProfile profile = new GameProfile(event.getName(), event.getUuid());
//
//            player.setGameProfile(profile);
//
//            server.getLogger().info("{} ({}) logged in", event.getName(), connection.getChannel().remoteAddress());
//
//            SparkyWorld world = server.getWorlds().getFirst();
//
//            if (world == null) {
//                TextComponent reason = Component.text("Missing a default world!");
//                connection.close(reason.color(NamedTextColor.RED));
//                return;
//            }
//
//            connection.flushPacket(new ServerLoginSuccess(event.getUuid(), event.getName()));
//
//            channel.setAttribute(NetworkManager.CONNECTION_STATE, ConnectionState.PLAY);
//
//            Difficulty difficulty = config.getDifficulty();
//
//            Location location = new Location(world, -64, 64, 0, 0, 0);
//
//            connection.sendPacket(new ServerJoinGame(0, GameMode.CREATIVE, Dimension.OVERWORLD, difficulty, config.getMaxPlayers(), LevelType.DEFAULT, false));
//            connection.sendPacket(new ServerSpawnPosition(new Vector3i(-64, 0, 0)));
//            player.teleport(location);
//
//            server.getPlayerList().add(player);
//
//            LoginEvent.LoginResult result = new LoginEvent.LoginResult(LoginEvent.LoginResultType.ALLOWED, "");
//            LoginEvent loginEvent = new LoginEvent(player, result);
//
//            server.getEventHandler().call(loginEvent);
//
//            if (loginEvent.getResult().getType() != LoginEvent.LoginResultType.ALLOWED) {
//                TextComponent reason = Component.text(loginEvent.getResult().getReason());
//                connection.close(reason.color(NamedTextColor.RED));
//
//                server.getPlayerList().remove(player);
//                return;
//            }
//
//            List<Chunk> chunks = new ArrayList<>();
//
//            int renderDistance = config.renderDistance() / 2;
//            for (int x = -renderDistance; x < renderDistance; x++) {
//                for (int z = -renderDistance; z < renderDistance; z++) {
//                    Chunk column = world.getChunkAt(x, z);
//
//                    if (column == null) continue;
//
//                    chunks.add(column);
//                }
//            }
//
//            connection.sendPacket(new ServerChunkDataBulk(chunks));
        });
    }
    
    public void handleKeepAlive(ClientKeepAlive packet) {
    }
    
    public void handleChatMessage(ClientChatMessage packet) {
        CHAT_THREAD.submit(() -> {
            SparkyPlayer player = connection.player();
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
    
    }
    
    public void handlePosition(ClientPlayerPosition packet) {
//        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;
//
//        SparkyPlayer player = connection.getPlayer();
//        Location location = player.getLocation();
//
//        double x = location.getX();
//        double y = location.getY();
//        double z = location.getZ();
//
//        double newX = packet.getX();
//        double newY = packet.getY();
//        double newZ = packet.getZ();
//
//        if (Double.isInfinite(newX) || Double.isNaN(newX)) {
//            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
//            return;
//        }
//
//        if (Double.isInfinite(newY) || Double.isNaN(newY)) {
//            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
//            return;
//        }
//
//        if (Double.isInfinite(newZ) || Double.isNaN(newZ)) {
//            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
//            return;
//        }
//
//        double deltaX = Math.abs(newX - x);
//        double deltaY = Math.abs(newY - y);
//        double deltaZ = Math.abs(newZ - z);
//
//        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
//
//        // TODO: Check for server added velocity
//        if (distance > 5) {
//            player.teleport(new Location(location.getWorld(), x, y, z, location.getYaw(), location.getPitch()));
//        } else {
//            location.setX(newX);
//            location.setY(newY);
//            location.setZ(newZ);
//        }
    }
    
    public void handleLook(ClientPlayerLook packet) {
//        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;
//
//        SparkyPlayer player = connection.player();
//        Location location = player.location();
//
//        float yaw = packet.yaw();
//        float pitch = packet.pitch();
//
//        if (Double.isInfinite(yaw) || Double.isNaN(yaw)) {
//            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
//            return;
//        }
//
//        if (Double.isInfinite(pitch) || Double.isNaN(pitch)) {
//            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
//            return;
//        }
//
//        location.setYaw(yaw);
//        location.setPitch(pitch);
    }
    
    public void handlePositionAndLook(ClientPlayerPositionAndLook packet) {
//        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;
//
//        SparkyPlayer player = connection.player();
//        Location location = player.location();
//
//        double x = location.x();
//        double y = location.y();
//        double z = location.z();
//
//        double newX = packet.x();
//        double newY = packet.y();
//        double newZ = packet.z();
//
//        float yaw = packet.yaw();
//        float pitch = packet.pitch();
//
//        boolean invalidX = Double.isInfinite(newX) || Double.isNaN(newX);
//        boolean invalidY = Double.isInfinite(newY) || Double.isNaN(newY);
//        boolean invalidZ = Double.isInfinite(newZ) || Double.isNaN(newZ);
//        boolean invalidYaw = Double.isInfinite(yaw) || Double.isNaN(yaw);
//        boolean invalidPitch = Double.isInfinite(pitch) || Double.isNaN(pitch);
//
//        if (invalidX || invalidY || invalidZ || invalidYaw || invalidPitch) {
//            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
//            return;
//        }
//
//        double deltaX = Math.abs(newX - x);
//        double deltaY = Math.abs(newY - y);
//        double deltaZ = Math.abs(newZ - z);
//
//        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
//
//        // TODO: Check for server added velocity
//        if (distance > 5) {
//            player.teleport(new Location(location.world(), x, y, z, yaw, pitch));
//        } else {
//            location.setX(newX);
//            location.setY(newY);
//            location.setZ(newZ);
//            location.setYaw(yaw);
//            location.setPitch(pitch);
//        }
    }
    
    public void handleHeldItemChange(ClientHeldItemChange packet) {
//        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparkyPlayer player = connection.player();

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
