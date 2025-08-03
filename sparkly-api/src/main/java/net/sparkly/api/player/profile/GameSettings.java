package net.sparkly.api.player.profile;

import net.sparkly.api.enums.ChatMode;

public record GameSettings(
    String locale, ChatMode chatMode, boolean chatColors, int viewDistance, int skinParts
) {
}