package net.kunmc.lab.kmp.client.music;

import net.kunmc.lab.kmp.client.music.player.IMusicPlayer;
import net.kunmc.lab.kmp.client.music.player.URLMp3MusicPlayer;
import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.data.ResponseSender;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.Vec3d;

import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class MusicRinger {
    private final UUID uuid;
    private final String musicURL;
    private Vec3d positionVec;
    private IMusicPlayer musicPlayer;
    private boolean readyPlay;
    private float volume;

    public MusicRinger(UUID uuid, String musicURL, Vec3d positionVec) {
        this.uuid = uuid;
        this.musicURL = musicURL;
        this.positionVec = positionVec;
    }

    public Vec3d getPosition() {
        return positionVec;
    }

    public double getDistance() {
        return Math.sqrt(Objects.requireNonNull(Minecraft.getInstance().player).getDistanceSq(getPosition()));
    }

    public void playWait(long startpos) {
        PlayWaitThread pwt = new PlayWaitThread(startpos);
        pwt.start();
    }

    public void playStart() {
        if (readyPlay && musicPlayer != null) {
            musicPlayer.play();
        }
    }

    public void playStartAutoMisalignment() {
        if (readyPlay && musicPlayer != null) {
            musicPlayer.playAutoMisalignment();
        }
    }

    public void playStop() {
        if (readyPlay && musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public void setPositionVec(Vec3d position) {
        this.positionVec = position;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void volumeUpdate() {
        if (musicPlayer != null) {
            float vol = 1f - (float) getDistance() / (30f * volume);
            musicPlayer.setVolume((float) (Math.max(vol, 0f) * ClientWorldMusicManager.instance().getEventuallyMusicVolume()));
        }
    }

    public IMusicPlayer getMusicPlayer() {
        return musicPlayer;
    }

    public class PlayWaitThread extends Thread {
        private final long startPos;

        public PlayWaitThread(long pos) {
            this.setName("Play Wait Thread");
            this.startPos = pos;
        }

        @Override
        public void run() {
            if (!readyPlay) {
                try {
                    musicPlayer = new URLMp3MusicPlayer(new URL(musicURL));
                    musicPlayer.ready(startPos);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            ResponseSender.sendToServer(KMPWorldData.MUSIC_RINGD, 0, uuid.toString(), new CompoundNBT());
            readyPlay = true;
        }
    }
}
