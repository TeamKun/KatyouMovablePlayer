package net.kunmc.lab.kmp.client.handler;

import net.kunmc.lab.kmp.client.music.ClientWorldMusicManager;
import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.event.common.ResponseEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class MusicRingerHandler {
    @SubscribeEvent
    public static void onResopnse(ResponseEvent.Server e) {
        if (e.getLocation().equals(KMPWorldData.WORLD_RINGD)) {
            if (e.getId() == 0) {
                ClientWorldMusicManager.instance().playMusicPlayer(UUID.fromString(e.getMessage()));
            } else if (e.getId() == 1) {
                ClientWorldMusicManager.instance().stopMusicPlayer(UUID.fromString(e.getMessage()));
            } else if (e.getId() == 2) {
                ClientWorldMusicManager.instance().playMusicMiddlePlayer(UUID.fromString(e.getMessage()));
            }
        }
    }
}
