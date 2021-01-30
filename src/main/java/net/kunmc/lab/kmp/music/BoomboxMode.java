package net.kunmc.lab.kmp.music;

import net.minecraft.util.IStringSerializable;

public enum BoomboxMode implements IStringSerializable {
    NONE("none"), PAUSE("pause"), PLAY("play");
    private final String name;

    private BoomboxMode(String name) {
        this.name = name;
    }

    public static BoomboxMode getModeByName(String name) {
        for (BoomboxMode sc : values()) {
            if (sc.getName().equals(name)) return sc;
        }
        return NONE;
    }

    @Override
    public String getName() {
        return name;
    }
}