package net.kunmc.lab.kmp.client.handler;

import net.kunmc.lab.kmp.client.music.ClientWorldMusicManager;
import net.kunmc.lab.kmp.client.music.MusicRinger;
import net.kunmc.lab.kmp.packet.MusicRingUpdateMessage;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MusicRingUpdateMessageHandler {
    public static void reversiveMessage(MusicRingUpdateMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        MusicRinger ringer = ClientWorldMusicManager.instance().getMusicRinger(message.uuid);
        if (ringer != null) {
            ringer.setVolume(message.musicVolume);
        }
    }
}
