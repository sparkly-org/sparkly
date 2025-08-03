package net.sparkly.server.network.model;

import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.PacketRegistry;
import net.sparkly.server.network.packets.impl.client.handshake.ClientHandshake;
import net.sparkly.server.network.packets.impl.client.status.ClientPing;
import net.sparkly.server.network.packets.impl.client.status.ClientStatusRequest;
import net.sparkly.server.network.packets.impl.client.login.ClientLoginStart;
import net.sparkly.server.network.packets.impl.client.play.*;
import net.sparkly.server.network.packets.impl.server.login.ServerLoginDisconnect;
import net.sparkly.server.network.packets.impl.server.login.ServerLoginSuccess;
import net.sparkly.server.network.packets.impl.server.play.*;
import net.sparkly.server.network.packets.impl.server.status.ServerPong;
import net.sparkly.server.network.packets.impl.server.status.ServerStatusResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.sparkly.server.network.model.PacketOwnership.CLIENT;
import static net.sparkly.server.network.model.PacketOwnership.SERVER;

public enum ConnectionState {
    
    HANDSHAKING {
        {
            register(CLIENT, 0x00, ClientHandshake::new);
        }
    },
    STATUS {
        {
            register(CLIENT, 0x00, ClientStatusRequest::new);
            register(CLIENT, 0x01, ClientPing::new);
            register(SERVER, 0x00, ServerStatusResponse::new);
            register(SERVER, 0x01, ServerPong::new);
        }
    },
    LOGIN {
        {
            register(CLIENT, 0x00, ClientLoginStart::new);
            register(SERVER, 0x00, ServerLoginDisconnect::new);
            register(SERVER, 0x02, ServerLoginSuccess::new);
        }
    },
    PLAY {
        {
            register(CLIENT, 0x00, ClientKeepAlive::new);
            register(CLIENT, 0x01, ClientChatMessage::new);
            register(CLIENT, 0x02, ClientUseEntity::new);
            register(CLIENT, 0x03, ClientPlayerIdle::new);
            register(CLIENT, 0x04, ClientPlayerPosition::new);
            register(CLIENT, 0x05, ClientPlayerLook::new);
            register(CLIENT, 0x06, ClientPlayerPositionAndLook::new);
            // register(CLIENT, 0x08, ClientBlockPlacement::new);
            register(CLIENT, 0x09, ClientHeldItemChange::new);
            register(CLIENT, 0x0A, ClientArmSwing::new);
            register(CLIENT, 0x13, ClientPlayerAbilities::new);
            register(CLIENT, 0x15, ClientSettings::new);
            register(CLIENT, 0x16, ClientStatus::new);
            register(CLIENT, 0x17, ClientPluginMessage::new);
            
            register(SERVER, 0x00, ServerKeepAlive::new);
            register(SERVER, 0x01, ServerJoinGame::new);
            register(SERVER, 0x02, ServerChatMessage::new);
            register(SERVER, 0x03, ServerTimeUpdate::new);
            register(SERVER, 0x05, ServerSpawnPosition::new);
            register(SERVER, 0x06, ServerUpdateHealth::new);
            register(SERVER, 0x07, ServerRespawn::new);
            register(SERVER, 0x08, ServerPositionAndLook::new);
            register(SERVER, 0x0F, ServerSpawnMob::new);
            register(SERVER, 0x21, ServerChunkData::new);
            register(SERVER, 0x26, ServerChunkDataBulk::new);
            register(SERVER, 0x40, ServerDisconnect::new);
        }
    };
    
    private final Map<PacketOwnership, PacketRegistry> registry;
    
    ConnectionState() {
        this.registry = new HashMap<>();
    }
    
    protected void register(PacketOwnership ownership, int id, Supplier<Packet> supplier) {
        PacketRegistry packetRegistry = registry.computeIfAbsent(ownership,
            k -> new PacketRegistry());
        packetRegistry.register(id, supplier);
    }
    
    public PacketRegistry registry(PacketOwnership ownership) {
        return registry.get(ownership);
    }
}
