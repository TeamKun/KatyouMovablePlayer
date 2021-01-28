package net.kunmc.lab.kmp.handler;

import net.kunmc.lab.kmp.music.ServerWorldMusicManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ServerHandler {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        ServerWorldMusicManager.instance().tick();
    }
}
