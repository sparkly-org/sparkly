package net.sparkly.api.player;

import java.util.UUID;

public class GameProfile {

    private UUID uuid;
    private String name;

    public GameProfile(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public UUID uuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String username() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
