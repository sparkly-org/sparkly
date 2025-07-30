package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientUseEntity implements Packet.Client {
    
    public enum InteractType {
        INTERACT, ATTACK, INTERACT_AT
    }
    
    private int entityId;
    private InteractType interactType;
    private float targetX;
    private float targetY;
    private float targetZ;

    public ClientUseEntity() {
    }

    public ClientUseEntity(int entityId, InteractType interactType, float targetX, float targetY, float targetZ) {
        this.entityId = entityId;
        this.interactType = interactType;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.entityId = buffer.readVarInt();
        this.interactType = InteractType.values()[buffer.readVarInt()];

        if (interactType == InteractType.INTERACT_AT) {
            this.targetX = buffer.readFloat();
            this.targetY = buffer.readFloat();
            this.targetZ = buffer.readFloat();
        }
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleUseEntity(this);
    }
    
    public int entityId() {
        return entityId;
    }
    
    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }
    
    public InteractType interactType() {
        return interactType;
    }
    
    public void setInteractType(InteractType interactType) {
        this.interactType = interactType;
    }
    
    public float targetX() {
        return targetX;
    }
    
    public void setTargetX(float targetX) {
        this.targetX = targetX;
    }
    
    public float targetY() {
        return targetY;
    }
    
    public void setTargetY(float targetY) {
        this.targetY = targetY;
    }
    
    public float targetZ() {
        return targetZ;
    }
    
    public void setTargetZ(float targetZ) {
        this.targetZ = targetZ;
    }
}
