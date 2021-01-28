package net.kunmc.lab.kmp.handler;

import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.event.common.ResponseEvent;
import net.kunmc.lab.kmp.music.ServerWorldMusicManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.UUID;

public class WorldRingerHandler {
    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(KMPWorldData.MUSIC_RINGD)) {
            if (e.getId() == 0) {
                ServerWorldMusicManager.instance().loadingFinish(UUID.fromString(e.getMessage()), e.getPlayer());
            }
        }
    }
}
