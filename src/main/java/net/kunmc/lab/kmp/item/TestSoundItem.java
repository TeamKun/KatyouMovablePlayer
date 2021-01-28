package net.kunmc.lab.kmp.item;

import net.kunmc.lab.kmp.client.music.player.IMusicPlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class TestSoundItem extends Item {
    private static IMusicPlayer musicPlayer;

    public TestSoundItem(Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote()) {

            //  long ct = playerIn.inventory.mainInventory.stream().filter(n -> n.getItem() == KMPItems.BOOMBOX).count();
            playerIn.sendMessage(new StringTextComponent(worldIn.getDimension().getType().getRegistryName().toString()));
        }
        return ActionResult.resultSuccess(itemstack);
    }
}
