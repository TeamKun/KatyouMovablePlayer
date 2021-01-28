package net.kunmc.lab.kmp.item;

import net.kunmc.lab.kmp.music.PlayerRinger;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class BoomBoxItem extends Item {
    public BoomBoxItem(Item.Properties properties) {
        super(properties);
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote) {
            CompoundNBT tag = itemstack.getOrCreateTag();
            tag.putBoolean("Play", true);
            PlayerRinger ringer = new PlayerRinger((ServerPlayerEntity) playerIn);
            ringer.musicPlay();
        }
        return ActionResult.resultSuccess(itemstack);
    }
}
