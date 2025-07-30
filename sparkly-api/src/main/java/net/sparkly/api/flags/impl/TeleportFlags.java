package net.sparkly.api.flags.impl;

import net.sparkly.api.flags.RelativeFlag;

public class TeleportFlags extends RelativeFlag {

    public static final TeleportFlags EMPTY = new TeleportFlags(0x0);
    public static final TeleportFlags X = new TeleportFlags(0x1);
    public static final TeleportFlags Y = new TeleportFlags(0x2);
    public static final TeleportFlags Z = new TeleportFlags(0x4);
    public static final TeleportFlags YAW = new TeleportFlags(0x8);
    public static final TeleportFlags PITCH = new TeleportFlags(0x10);

    public TeleportFlags(int mask) {
        super(mask);
    }
}
