package net.kunmc.lab.kmp.util;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

import java.io.InputStream;

public class MusicUtils {
    public static float getMP3MillisecondPerFrame(InputStream stream) throws BitstreamException {
        Bitstream bitstream = new Bitstream(stream);
        Header h = bitstream.readFrame();
        return h.ms_per_frame();
    }

    public static int getMP3Duration(InputStream stream, int size) throws BitstreamException {
        Bitstream bitstream = new Bitstream(stream);
        Header h = bitstream.readFrame();
        return (int) h.total_ms(size);
    }
}
