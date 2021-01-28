package net.kunmc.lab.kmp.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.UUID;

public class PlayerUtil {
    public static String getUserName(PlayerEntity pl) {
        return pl.getGameProfile().getName();
    }

    public static String getUUID(PlayerEntity pl) {
        return PlayerEntity.getUUID(pl.getGameProfile()).toString();
    }

    public static ServerPlayerEntity getPlayerByUUID(String uuid) {
        return ServerUtil.getMinecraftServer().getPlayerList().getPlayerByUUID(UUID.fromString(uuid));
    }
}
