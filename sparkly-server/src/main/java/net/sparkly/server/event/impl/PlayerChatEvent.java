package net.sparkly.server.event.impl;

import net.sparkly.api.player.Player;
import net.sparkly.api.event.Cancellable;
import net.sparkly.api.event.Event;

public class PlayerChatEvent extends Cancellable implements Event {
    
    private final Player player;
    private String message;
    
    public PlayerChatEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }
    
    public Player player() {
        return player;
    }
    
    public String message() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
}
