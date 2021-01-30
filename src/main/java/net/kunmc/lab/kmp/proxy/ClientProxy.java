package net.kunmc.lab.kmp.proxy;

import net.kunmc.lab.kmp.client.gui.screen.BoomboxScreen;
import net.kunmc.lab.kmp.item.KMPItems;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;

public class ClientProxy extends CommonProxy {
    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void openBoomBoxGUI(PlayerEntity player, CommonProxy.IWhetherItem stack, Hand handIn) {
        Item item = stack.getStack().getItem();
        if (item == KMPItems.BOOMBOX) {
            this.mc.displayGuiScreen(new BoomboxScreen(player, stack, handIn));
        }
    }
}
