package net.kunmc.lab.kmp.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class CommonProxy {
    public void openBoomBoxGUI(PlayerEntity player, IWhetherItem stack, Hand handIn) {

    }

    public interface IWhetherItem {
        ItemStack getStack();
    }
}
