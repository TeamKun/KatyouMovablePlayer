package net.kunmc.lab.kmp.client.music;

import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;

import java.util.*;

public class ClientWorldMusicManager {
    private static final Minecraft mc = Minecraft.getInstance();
    private static ClientWorldMusicManager INSTANCE;
    private double musicVolume;
    private boolean stereoEnabled;

    private Map<UUID, MusicRinger> mplayers = new HashMap<>();

    private boolean allStop;

    public static void init() {
        INSTANCE = new ClientWorldMusicManager();
     //   INSTANCE.setMusicVolume(ClientConfig.MusicVolume.get());
     //   INSTANCE.setStereoEnabled(ClientConfig.StereoEnabled.get());
    }

    public static ClientWorldMusicManager instance() {
        return INSTANCE;
    }

    public void addMusicPlayer(UUID uuid, MusicRinger ringer) {
        stopMusicPlayer(uuid);
        mplayers.put(uuid, ringer);
    }

    public void stopMusicPlayer(UUID uuid) {
        if (mplayers.containsKey(uuid)) {
            mplayers.get(uuid).playStop();
            mplayers.remove(uuid);
        }
    }

    public void stopAllMusicPlayer() {
        allStop = true;
    }

    public void playMusicPlayer(UUID uuid) {
        if (mplayers.containsKey(uuid))
            mplayers.get(uuid).playStart();
    }

    public void playMusicMiddlePlayer(UUID uuid) {
        if (mplayers.containsKey(uuid))
            mplayers.get(uuid).playStartAutoMisalignment();
    }

    public void loop() {
        if (mc.player == null) {
            if (!mplayers.isEmpty()) {
                stopAllMusicPlayer();
            }
        }
        if (allStop) {
            allStop = false;
            if (!mplayers.isEmpty()) {
                List<UUID> stopedUUID = new ArrayList<>(new ArrayList<>(mplayers.keySet()));
                stopedUUID.forEach(this::stopMusicPlayer);
            }
        }
        mplayers.values().forEach(MusicRinger::volumeUpdate);
    }

    public MusicRinger getMusicRinger(UUID uuid) {
        if (mplayers.containsKey(uuid))
            return mplayers.get(uuid);

        return null;
    }

    public double getMusicVolume() {
        return musicVolume;
    }

    public double getEventuallyMusicVolume() {
        return 1f * Minecraft.getInstance().gameSettings.getSoundLevel(SoundCategory.MASTER);
    }

    public void setMusicVolume(double musicVolume) {
        this.musicVolume = musicVolume;
    }

    public boolean isStereoEnabled() {
        return stereoEnabled;
    }

    public void setStereoEnabled(boolean stereoEnabled) {
        this.stereoEnabled = stereoEnabled;
    }
}
