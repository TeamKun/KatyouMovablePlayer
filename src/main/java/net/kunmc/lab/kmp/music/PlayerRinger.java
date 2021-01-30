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
        CompoundNBT tag = getMusicPlayer().getTag();
        if (tag != null) {
            return tag.getString("MusicURL");
        }
        return "";
    }

    @Override
    public void musicPlayed() {
    }

    @Override
    public void musicStoped() {
        CompoundNBT tag = getMusicPlayer().getOrCreateTag();
        tag.putString("Mode", BoomboxMode.NONE.getName());
    }

    @Override
    public boolean canMusicPlay() {
        if (!ServerUtil.isOnlinePlayer(PlayerUtil.getUUID(playerEntity)))
            return false;
        if (playerEntity.getHealth() <= 0)
            return false;
        if (playerEntity.inventory.mainInventory.stream().filter(n -> n.getItem() == KMPItems.BOOMBOX).count() != 1)
            return false;
        CompoundNBT tag = getMusicPlayer().getTag();
        return tag != null && tag.getString("Mode").equalsIgnoreCase(BoomboxMode.PLAY.getName());
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
        return 1;
    }

    @Override
    public boolean isMusicLoop() {
        return true;
    }

    @Override
    public long getMusicDuration() {
        CompoundNBT tag = getMusicPlayer().getTag();
        if (tag != null) {
            return tag.getLong("Duration");
        }
        return 0;
    }


}
