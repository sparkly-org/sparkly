package net.sparkly.server.network.packets.impl.client.play;

import net.sparkly.api.flags.impl.PlayerAbilities;
import net.sparkly.server.network.NetworkBuffer;
import net.sparkly.server.network.packets.Packet;
import net.sparkly.server.network.packets.processor.PacketProcessor;

public class ClientPlayerAbilities implements Packet.Client {

    private PlayerAbilities abilities;
    private float flySpeed;
    private float walkingSpeed;

    public ClientPlayerAbilities() {
    }

    public ClientPlayerAbilities(PlayerAbilities abilities, float flySpeed, float walkingSpeed) {
        this.abilities = abilities;
        this.flySpeed = flySpeed;
        this.walkingSpeed = walkingSpeed;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.abilities = new PlayerAbilities(buffer.readByte());
        this.flySpeed = buffer.readFloat();
        this.walkingSpeed = buffer.readFloat();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handlePlayerAbilities(this);
    }
    
    public PlayerAbilities abilities() {
        return abilities;
    }
    
    public void setAbilities(PlayerAbilities abilities) {
        this.abilities = abilities;
    }
    
    public float flySpeed() {
        return flySpeed;
    }
    
    public void setFlySpeed(float flySpeed) {
        this.flySpeed = flySpeed;
    }
    
    public float walkingSpeed() {
        return walkingSpeed;
    }
    
    public void setWalkingSpeed(float walkingSpeed) {
        this.walkingSpeed = walkingSpeed;
    }
}
