package net.sparkly.server.event.impl;

import net.sparkly.api.event.Cancellable;
import net.sparkly.api.event.Event;

public class PlayerHandshakeEvent extends Cancellable implements Event {
    
    public enum HandshakeState {
        STATUS, LOGIN
    }
    
    private String address;
    private int port;
    private int protocolVersion;
    private HandshakeState state;
    
    public PlayerHandshakeEvent(String address, int port, int protocolVersion, HandshakeState state) {
        this.address = address;
        this.port = port;
        this.protocolVersion = protocolVersion;
        this.state = state;
    }
    
    public String address() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public int port() {
        return port;
    }
    
    public void setPort(int port) {
        this.port = port;
    }
    
    public int protocolVersion() {
        return protocolVersion;
    }
    
    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
    
    public HandshakeState state() {
        return state;
    }
    
    public void setState(HandshakeState state) {
        this.state = state;
    }
}
