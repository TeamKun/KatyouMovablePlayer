package net.kunmc.lab.kmp.handler;

import net.kunmc.lab.kmp.command.KMPCommands;
import net.kunmc.lab.kmp.data.KMPWorldData;
import net.kunmc.lab.kmp.event.common.ResponseEvent;
import net.kunmc.lab.kmp.item.KMPItems;
import net.kunmc.lab.kmp.music.PlayerRinger;
import net.kunmc.lab.kmp.music.ServerWorldMusicManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

public class ServerHandler {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent e) {
        ServerWorldMusicManager.instance().tick();
    }

    @SubscribeEvent
    public static void onClientResponse(ResponseEvent.Client e) {
        if (e.getLocation().equals(KMPWorldData.BOOMBOX_INS)) {
            Hand hand = e.getData().getBoolean("hand") ? Hand.MAIN_HAND : Hand.OFF_HAND;

            ItemStack stack = e.getPlayer().getHeldItem(hand);

            if (stack.getItem() != KMPItems.BOOMBOX)
                return;

            CompoundNBT tag = stack.getOrCreateTag();
            if (e.getId() == 0) {
                tag.putString("Mode", e.getMessage());
            } else if (e.getId() == 1) {
                tag.putString("MusicURL", e.getMessage());
                tag.putLong("Duration", e.getData().getLong("duration"));
            } else if (e.getId() == 2) {
                PlayerRinger ringer = new PlayerRinger(e.getPlayer());
                ringer.musicPlay();
            } else if (e.getId() == 3) {
                tag.putLong("CurrentPosition", 0);
            }
        }
    }

    @SubscribeEvent
    public static void onServerStart(FMLServerStartingEvent e) {
        KMPCommands.registerCommand(e.getCommandDispatcher());
    }
}
