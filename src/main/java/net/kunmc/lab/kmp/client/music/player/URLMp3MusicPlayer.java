package net.kunmc.lab.kmp.client.music.player;

import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import net.kunmc.lab.kmp.util.MusicUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class URLMp3MusicPlayer implements IMusicPlayer {
    private final URL url;
    private final InputStream stream;
    private final long duration;
    private final float frameSecond;

    private AdvancedPlayer player;
    private long readyTime;
    private boolean isReady;
    private long startPosition;
    private long startPlayTime;

    public URLMp3MusicPlayer(URL url) throws IOException, BitstreamException {
        this.url = url;
        this.stream = url.openStream();
        HttpURLConnection httpConnection = (HttpURLConnection) (url.openConnection());
        this.duration = MusicUtils.getMP3Duration(stream, httpConnection.getContentLength());
        this.frameSecond = MusicUtils.getMP3MillisecondPerFrame(stream);
    }

    @Override
    public void ready(long startMiliSecond) {
        try {
            if (!this.isReady && player == null) {
                this.readyTime = System.currentTimeMillis();
                this.startPosition = startMiliSecond;
                this.player = new AdvancedPlayer(url.openStream());
                this.isReady = true;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.isReady = false;
        }
    }

    @Override
    public void play() {
        playMisalignment(0);
    }

    @Override
    public void playMisalignment(long zure) {
        try {
            if (this.isReady && player != null) {
                this.startPosition += zure;
                if (duration <= startPosition) {
                    this.player = null;
                    this.isReady = false;
                    return;
                }
                MusicPlayThread playThread = new MusicPlayThread((int) (startPosition / frameSecond));
                playThread.start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.player = null;
            this.isReady = false;
        }
    }

    @Override
    public void playAutoMisalignment() {
        long zure = System.currentTimeMillis() - readyTime;
        if (getMaxMisalignment() > zure) {
            playMisalignment(zure);
        }
    }

    @Override
    public void playAndReady(long startMiliSecond) {
        ready(startMiliSecond);
        play();
    }

    @Override
    public void stop() {
        if (player != null) {
            player.close();
        }
    }

    @Override
    public boolean isPlaying() {
        return player != null;
    }

    @Override
    public long getMaxMisalignment() {
        return Integer.MAX_VALUE;
    }

    @Override
    public long getCureentElapsed() {
        if (player == null)
            return 0;

        return System.currentTimeMillis() - startPlayTime + startPosition;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public Object getMusicSource() {
        return url;
    }

    @Override
    public void setVolume(float vol) {
        if (player != null)
            player.setVolume(vol);
    }

    @Override
    public float getVolume() {
        if (player != null)
            return player.getVolume();
        return 0;
    }

    private class MusicPlayThread extends Thread {
        private final int startFrame;

        public MusicPlayThread(int startFrame) {
            this.startFrame = startFrame;
        }

        @Override
        public void run() {
            try {
                startPlayTime = System.currentTimeMillis();
                player.play(startFrame, Integer.MAX_VALUE);
                player = null;
            } catch (JavaLayerException e) {
                e.printStackTrace();
            }
        }
    }
}
