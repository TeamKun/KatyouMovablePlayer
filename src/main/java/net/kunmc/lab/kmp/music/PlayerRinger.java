package net.kunmc.lab.kmp.music;

import net.kunmc.lab.kmp.item.KMPItems;
import net.kunmc.lab.kmp.util.PlayerUtil;
import net.kunmc.lab.kmp.util.ServerUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.Vec3d;

public class PlayerRinger implements IWorldRingWhether {
    private final ServerPlayerEntity playerEntity;

    public PlayerRinger(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public ItemStack getMusicPlayer() {
        return playerEntity.inventory.mainInventory.stream().filter(n -> n.getItem() == KMPItems.BOOMBOX).findFirst().orElse(ItemStack.EMPTY);
    }

    public void musicPlay() {
        ServerWorldMusicManager.instance().play(playerEntity.getGameProfile().getId(), playerEntity.world.dimension.getType().getRegistryName(), getMusicURL(), this);
    }

    public boolean isMusicPlaying() {
        return ServerWorldMusicManager.instance().isPlaying(playerEntity.getGameProfile().getId());
    }

    public String getMusicURL() {
        String str = "https://www.dropbox.com/s/qt9rn67jziubk46/%E3%82%B5%E3%82%AB%E3%83%8A%E3%82%AF%E3%82%B7%E3%83%A7%E3%83%B3%20%20%E6%96%B0%E5%AE%9D%E5%B3%B6.mp3?dl=1";
        return str;
    }

    @Override
    public void musicPlayed() {
    }

    @Override
    public void musicStoped() {
        CompoundNBT tag = getMusicPlayer().getOrCreateTag();
        tag.putBoolean("Play", false);
    }

    @Override
    public boolean canMusicPlay() {
        if (!ServerUtil.isOnlinePlayer(PlayerUtil.getUUID(playerEntity)))
            return false;
        if (!playerEntity.isLiving())
            return false;
        if (playerEntity.inventory.mainInventory.stream().filter(n -> n.getItem() == KMPItems.BOOMBOX).count() != 1)
            return false;
        CompoundNBT tag = getMusicPlayer().getTag();
        return tag != null && tag.getBoolean("Play");
    }

    @Override
    public long getCurrentMusicPlayPosition() {
        CompoundNBT tag = getMusicPlayer().getTag();
        if (tag != null) {
            return tag.getLong("CurrentPosition");
        }
        return 0;
    }

    @Override
    public void setCurrentMusicPlayPosition(long position) {
        CompoundNBT tag = getMusicPlayer().getOrCreateTag();
        tag.putLong("CurrentPosition", position);
    }

    @Override
    public Vec3d getMusicPos() {
        return playerEntity.getPositionVec();
    }

    @Override
    public float getMusicVolume() {
        return 30;
    }

    @Override
    public boolean isMusicLoop() {
        return false;
    }
}
