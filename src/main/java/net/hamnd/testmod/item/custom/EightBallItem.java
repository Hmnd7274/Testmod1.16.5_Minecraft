package net.hamnd.testmod.item.custom;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class EightBallItem extends Item {
    public static Random RANDOM = new Random();
    public EightBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if(!world.isRemote() && hand == Hand.MAIN_HAND) {
            outputRandomNumber(player);
            player.getCooldownTracker().setCooldown(this, 5);
        }


        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World world, List<ITextComponent> iTextComponents, ITooltipFlag iTooltipFlag) {
        if(Screen.hasShiftDown()) {
            iTextComponents.add(new StringTextComponent("Right click to get a random number !").mergeStyle(TextFormatting.AQUA));
        } else {
            iTextComponents.add(new StringTextComponent("Press SHIFT for more info").mergeStyle(TextFormatting.YELLOW));
        }

        super.addInformation(itemStack, world, iTextComponents, iTooltipFlag);
    }

    private void outputRandomNumber(PlayerEntity player) {
        String message = "Your number is " + getRandomNumber();
        ITextComponent msg = new StringTextComponent(message);
        player.sendMessage(msg, player.getUniqueID());
    }

    private int getRandomNumber() {
        return RANDOM.nextInt(9);
    }
}
