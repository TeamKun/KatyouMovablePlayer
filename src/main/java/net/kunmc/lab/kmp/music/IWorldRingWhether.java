package net.kunmc.lab.kmp.music;


import net.minecraft.util.math.Vec3d;

public interface IWorldRingWhether {
    void musicPlayed();

    void musicStoped();

    boolean canMusicPlay();

    long getCurrentMusicPlayPosition();

    void setCurrentMusicPlayPosition(long position);

    Vec3d getMusicPos();

    float getMusicVolume();

    boolean isMusicLoop();

    long getMusicDuration();
}
