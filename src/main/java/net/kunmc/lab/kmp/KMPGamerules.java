package net.kunmc.lab.kmp;

import net.minecraft.world.GameRules;

public class KMPGamerules {
    public static GameRules.RuleKey<GameRules.IntegerValue> MUSICPLAYER_RANGE;

    public static void register() {
        MUSICPLAYER_RANGE = GameRules.register("musicPlayerRange", GameRules.IntegerValue.create(30));
    }
}
