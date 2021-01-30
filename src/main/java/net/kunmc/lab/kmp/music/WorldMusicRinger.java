package net.kunmc.lab.kmp.music;

import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.data.ResponseSender;
import net.kunmc.lab.kmp.packet.MusicRingMessage;
import net.kunmc.lab.kmp.packet.MusicRingUpdateMessage;
import net.kunmc.lab.kmp.packet.PacketHandler;
import net.kunmc.lab.kmp.util.PlayerUtil;
import net.kunmc.lab.kmp.util.ServerUtil;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class WorldMusicRinger {
    private final List<UUID> playingPlayers = new ArrayList<>();
    private final List<UUID> loadingPlayers = new ArrayList<>();
    private final List<UUID> loadWaitingPlayers = new ArrayList<>();
    private final List<UUID> waitingMiddlePlayers = new ArrayList<>();
    private final List<UUID> regularConfirmationPlayers = new ArrayList<>();
    private final UUID uuid;
    private final ResourceLocation dimension;
    private final String playMusicURL;
    private final IWorldRingWhether whether;
    private long lastUpdateTime;
    private long ringStartTime;
    private long ringStartElapsedTime;
    private boolean playWaitingPrev;
    private boolean playWaiting;
    private long waitTime;
    private boolean playing;
    private boolean ringin;

    public WorldMusicRinger(UUID uuid, ResourceLocation dimension, String playMusicURL, IWorldRingWhether whether) {
        this.uuid = uuid;
        this.dimension = dimension;
        this.playMusicURL = playMusicURL;
        this.whether = whether;
        this.lastUpdateTime = System.currentTimeMillis();
    }

    public void play() {
        if (whether.canMusicPlay())
            whether.musicPlayed();

        ringStartElapsedTime = getCurrentMusicPlayPosition();

        if (ServerUtil.getOnlinePlayers().stream().anyMatch(this::canListen)) {
            playWaitingPrev = true;
            playWaiting = true;
        } else {
            playing = true;
            ringin = true;
            ringStartTime = System.currentTimeMillis();
        }
    }

    public void stop() {
        pause();
        whether.musicStoped();
    }

    public long getCurrentMusicPlayPosition() {
        return whether.getCurrentMusicPlayPosition();
    }

    public void pause() {
        playing = false;
        ringin = false;
        playWaitingPrev = false;
        playWaiting = false;
        playingPlayers.stream().filter(n -> ServerUtil.isOnlinePlayer(n.toString())).forEach(n -> {
            ResponseSender.sendToClient(n.toString(), ServerUtil.getMinecraftServer(), KMPWorldData.WORLD_RINGD, 1, uuid.toString(), new CompoundNBT());
        });
        playingPlayers.clear();
        loadingPlayers.clear();
        loadWaitingPlayers.clear();
        waitingMiddlePlayers.clear();
        regularConfirmationPlayers.clear();
    }

    public void playerPause(UUID plyaerUUID) {
        ResponseSender.sendToClient(plyaerUUID.toString(), ServerUtil.getMinecraftServer(), KMPWorldData.WORLD_RINGD, 1, uuid.toString(), new CompoundNBT());
        playingPlayers.remove(plyaerUUID);
        loadingPlayers.remove(plyaerUUID);
        loadWaitingPlayers.remove(plyaerUUID);
        waitingMiddlePlayers.remove(plyaerUUID);
        regularConfirmationPlayers.remove(plyaerUUID);
    }

    public boolean isRelatedPlayer(UUID playerUUID) {
        boolean flag1 = playingPlayers.contains(playerUUID);
        boolean flag2 = loadingPlayers.contains(playerUUID);
        boolean flag3 = loadWaitingPlayers.contains(playerUUID);
        boolean flag4 = waitingMiddlePlayers.contains(playerUUID);
        boolean flag5 = regularConfirmationPlayers.contains(playerUUID);
        return flag1 || flag2 || flag3 || flag4 || flag5;
    }

    public boolean tick() {

        if (playMusicURL == null) {
            stop();
        }

        if (!whether.canMusicPlay() || playMusicURL == null)
            return true;

        if (playWaiting)
            waitTime += System.currentTimeMillis() - lastUpdateTime;
        else
            waitTime = 0;

        Stream<ServerPlayerEntity> listenPlayers = ServerUtil.getOnlinePlayers().stream().filter(this::canListen);

        if (playWaiting && playWaitingPrev) {
            listenPlayers.filter(n -> !(loadingPlayers.contains(UUID.fromString(PlayerUtil.getUUID(n))) || loadWaitingPlayers.contains(UUID.fromString(PlayerUtil.getUUID(n))) || playingPlayers.contains(UUID.fromString(PlayerUtil.getUUID(n))) || waitingMiddlePlayers.contains(UUID.fromString(PlayerUtil.getUUID(n))))).forEach(n -> {
                loadingPlayers.add(UUID.fromString(PlayerUtil.getUUID(n)));
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> n), new MusicRingMessage(uuid, getMusicPos(), getPlayMusicURL(), getCurrentMusicPlayPosition()));
            });
            playWaitingPrev = false;
        } else if (playWaiting && loadingPlayers.isEmpty()) {
            loadWaitingPlayers.stream().filter(n -> canListen(PlayerUtil.getPlayerByUUID(n.toString()))).forEach(n -> {
                ResponseSender.sendToClient(n.toString(), ServerUtil.getMinecraftServer(), KMPWorldData.WORLD_RINGD, 0, uuid.toString(), new CompoundNBT());
            });
            playingPlayers.addAll(loadWaitingPlayers);
            playWaiting = false;
            ringin = true;
            ringStartTime = System.currentTimeMillis();
        }

        if (ringin) {
            long cur = ringStartElapsedTime + System.currentTimeMillis() - ringStartTime;
            if (cur >= whether.getMusicDuration()) {//長さ！
                if (whether.isMusicLoop()) {
                    pause();
                    whether.setCurrentMusicPlayPosition(0);
                    play();
                } else {
                    stop();
                    whether.setCurrentMusicPlayPosition(0);
                }
            } else {
                whether.setCurrentMusicPlayPosition(cur);
            }

            ServerUtil.getOnlinePlayers().stream().filter(this::canListen).forEach(n -> {
                if (!playingPlayers.contains(UUID.fromString(PlayerUtil.getUUID(n))) && !waitingMiddlePlayers.contains(UUID.fromString(PlayerUtil.getUUID(n)))) {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> n), new MusicRingMessage(uuid, getMusicPos(), getPlayMusicURL(), getCurrentMusicPlayPosition()));
                    waitingMiddlePlayers.add(UUID.fromString(PlayerUtil.getUUID(n)));
                }
                if (playingPlayers.contains(UUID.fromString(PlayerUtil.getUUID(n)))) {
                    PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> n), new MusicRingUpdateMessage(uuid, getMusicPos(), getMusicVolume()));
                }
            });

            ServerUtil.getOnlinePlayers().stream().filter(n -> isRelatedPlayer(UUID.fromString(PlayerUtil.getUUID(n)))).filter(n -> !canListen(n)).forEach(n -> playerPause(UUID.fromString(PlayerUtil.getUUID(n))));

        }

        lastUpdateTime = System.currentTimeMillis();

        return false;
    }

    public boolean isPlayWaiting() {
        return playWaiting;
    }

    public String getPlayMusicURL() {
        return playMusicURL;
    }

    public Vec3d getMusicPos() {
        return whether.getMusicPos();
    }

    public float getMusicVolume() {
        return whether.getMusicVolume();
    }

    public ResourceLocation getDimension() {
        return dimension;
    }

    public float getListenRange() {

        return 30f * getMusicVolume();
    }

    private boolean canListen(ServerPlayerEntity player) {
        return player.world.getDimension().getType().getRegistryName().equals(getDimension()) && Math.sqrt(player.getDistanceSq(getMusicPos())) <= getListenRange();
    }

    public void musicLoadingFinish(UUID playerUUID) {
        if (loadingPlayers.contains(playerUUID)) {
            loadingPlayers.remove(playerUUID);
            loadWaitingPlayers.add(playerUUID);
        } else {
            if (canListen(PlayerUtil.getPlayerByUUID(playerUUID.toString()))) {
                ResponseSender.sendToClient(playerUUID.toString(), ServerUtil.getMinecraftServer(), KMPWorldData.WORLD_RINGD, 2, uuid.toString(), new CompoundNBT());
                playingPlayers.add(playerUUID);
            }
        }
    }

    public void musicLoadingNotFinishRegularConfirmation(UUID playerUUID) {
        if (loadingPlayers.contains(playerUUID)) {
            loadingPlayers.remove(playerUUID);
        }
        regularConfirmationPlayers.add(playerUUID);
    }
}
