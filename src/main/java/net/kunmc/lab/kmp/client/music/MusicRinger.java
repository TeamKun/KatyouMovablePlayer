package net.kunmc.lab.kmp.client.music;

import net.kunmc.lab.kmp.client.music.player.IMusicPlayer;
import net.kunmc.lab.kmp.client.music.player.URLMp3MusicPlayer;
import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.data.ResponseSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.net.URL;
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

        ClientPlayerEntity cpe = Minecraft.getInstance().player;
        double partic = Minecraft.getInstance().getRenderPartialTicks();
        Vec3d pv = new Vec3d(MathHelper.lerp(partic, cpe.prevPosX, cpe.getPosX()), MathHelper.lerp(partic, cpe.prevPosY, cpe.getPosY()), MathHelper.lerp(partic, cpe.prevPosZ, cpe.getPosZ()));

        double d0 = pv.getX() - getPosition().x;
        double d1 = pv.getY() - getPosition().y;
        double d2 = pv.getZ() - getPosition().z;
        double md = d0 * d0 + d1 * d1 + d2 * d2;

        return Math.sqrt(md);
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
            float vl = volume;
            float rarnge = 30f * volume;
            float distance = (float) getDistance();
            float zure = (float) rarnge / 5f;
            float atzure = (float) rarnge / 10f;
            float nn = Math.min((vl / (distance - zure)) * 3f, 1f);
            float at = Math.min((vl / (rarnge - zure)) * 3f, 1f);
            float volume = distance <= zure ? 1f : distance <= (rarnge - atzure) ? nn : at * ((distance - rarnge) * -1 / atzure);

            musicPlayer.setVolume((float) (volume * ClientWorldMusicManager.instance().getEventuallyMusicVolume()));
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
                    musicPlayer = new URLMp3MusicPlayer(System.currentTimeMillis(), new URL(musicURL));
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
