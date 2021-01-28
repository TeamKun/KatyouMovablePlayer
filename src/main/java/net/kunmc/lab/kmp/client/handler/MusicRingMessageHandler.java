package net.kunmc.lab.kmp.client.handler;

import net.kunmc.lab.kmp.client.music.ClientWorldMusicManager;
import net.kunmc.lab.kmp.client.music.MusicRinger;
import net.kunmc.lab.kmp.packet.MusicRingMessage;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MusicRingMessageHandler {
    public static void reversiveMessage(MusicRingMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);

        MusicRinger ringer = new MusicRinger(message.uuid, message.musicURL, message.musicPos);
        ClientWorldMusicManager.instance().addMusicPlayer(message.uuid, ringer);

        ringer.playWait(message.startPos);
    }
}
