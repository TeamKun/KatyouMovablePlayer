package net.kunmc.lab.kmp.item;

import net.kunmc.lab.kmp.KatyouMovablePlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;

public class KMPItems {
    public static List<Item> MOD_ITEMS = new ArrayList<Item>();
    public static final Item TEST_SOUND = register("test_sound", new TestSoundItem(new Item.Properties().group(ItemGroup.TOOLS)));
    public static final Item BOOMBOX = register("boombox", new BoomBoxItem(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1)));

    private static Item register(String name, Item item) {
        MOD_ITEMS.add(item.setRegistryName(KatyouMovablePlayer.MODID, name));
        return item;
    }
}
