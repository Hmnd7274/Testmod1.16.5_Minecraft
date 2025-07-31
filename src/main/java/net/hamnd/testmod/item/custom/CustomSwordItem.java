package net.hamnd.testmod.item.custom;

import net.hamnd.testmod.item.ModItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CustomSwordItem extends SwordItem {
    public CustomSwordItem() {
        super(ItemTier.DIAMOND, 3, -2.4F, new Item.Properties().group(ModItemGroup.TEST_GROUP));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        worldIn.addParticle(ParticleTypes.HEART, playerIn.getPosX(), playerIn.getPosY() + 1, playerIn.getPosZ(), 0, 0, 0);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}