package net.sparkly.api.player;

public record GameSettings(
    String locale, ChatMode chatMode, boolean chatColors, int viewDistance, int skinParts
) {
}