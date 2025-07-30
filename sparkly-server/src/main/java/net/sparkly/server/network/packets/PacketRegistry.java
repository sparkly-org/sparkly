package net.sparkly.server.network.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class PacketRegistry {
    
    private final Map<Integer, Supplier<Packet>> id2Packet;
    private final Map<Class<? extends Packet>, Integer> packet2Id;
    
    public PacketRegistry() {
        this.id2Packet = new HashMap<>();
        this.packet2Id = new HashMap<>();
    }
    
    public void register(int id, Supplier<Packet> supplier) {
        id2Packet.put(id, supplier);
        packet2Id.put(supplier.get().getClass(), id);
    }
    
    public Optional<Integer> findId(Packet packet) {
        Integer id = packet2Id.get(packet.getClass());
        
        if (id == null) return Optional.empty();
        
        return Optional.of(id);
    }
    
    public Optional<Packet> findPacket(int id) {
        Supplier<Packet> supplier = id2Packet.get(id);
        
        if (supplier == null) return Optional.empty();
        
        return Optional.of(supplier.get());
    }
}
