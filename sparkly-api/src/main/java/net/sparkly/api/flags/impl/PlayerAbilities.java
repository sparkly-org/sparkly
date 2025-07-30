package net.sparkly.api.flags.impl;

import net.sparkly.api.flags.RelativeFlag;

public class PlayerAbilities extends RelativeFlag {

    public static final PlayerAbilities IS_CREATIVE = new PlayerAbilities(0x01);
    public static final PlayerAbilities IS_FLYING = new PlayerAbilities(0x02);
    public static final PlayerAbilities CAN_FLY = new PlayerAbilities(0x04);
    public static final PlayerAbilities GOD_MODE = new PlayerAbilities(0x08);

    public PlayerAbilities(int mask) {
        super(mask);
    }
}
