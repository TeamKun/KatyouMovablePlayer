package net.kunmc.lab.kmp.item;

import net.kunmc.lab.kmp.KatyouMovablePlayer;
import net.kunmc.lab.kmp.music.BoomboxMode;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class BoomBoxItem extends Item {
    public BoomBoxItem(Item.Properties properties) {
        super(properties);
        this.addPropertyOverride(new ResourceLocation("on"), (stack, world, entity) -> {
            CompoundNBT tag = stack.getTag();
            if (tag != null) {
                return BoomboxMode.getModeByName(tag.getString("Mode")) == BoomboxMode.PLAY ? 1.0F : 0.0F;
            } else {
                return 0.0F;
            }
        });
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (worldIn.isRemote) {
            if (getPlayerItems(playerIn).stream().filter(n -> n.getItem() == KMPItems.BOOMBOX).count() <= 1) {
                KatyouMovablePlayer.proxy.openBoomBoxGUI(playerIn, () -> playerIn.getHeldItem(handIn), handIn);
                playerIn.addStat(Stats.ITEM_USED.get(this));
            } else {
                playerIn.sendStatusMessage(new TranslationTextComponent("message.boombox.notonly"), true);
            }
        }
        return ActionResult.resultSuccess(itemstack);
    }

    private NonNullList<ItemStack> getPlayerItems(PlayerEntity playerEntity) {
        PlayerInventory inventory = playerEntity.inventory;
        NonNullList<ItemStack> il = NonNullList.create();
        il.addAll(inventory.mainInventory);
        il.addAll(inventory.armorInventory);
        il.addAll(inventory.offHandInventory);
        return il;
    }
}
