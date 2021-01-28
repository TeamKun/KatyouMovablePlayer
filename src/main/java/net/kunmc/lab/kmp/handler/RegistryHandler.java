package net.kunmc.lab.kmp.handler;

import net.kunmc.lab.kmp.item.KMPItems;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
    @SubscribeEvent
    public static void onItemsRegistry(final RegistryEvent.Register<Item> e) {
        KMPItems.MOD_ITEMS.forEach(n -> e.getRegistry().register(n));
    }
}
