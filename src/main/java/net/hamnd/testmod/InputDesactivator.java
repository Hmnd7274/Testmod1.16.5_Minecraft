package net.hamnd.testmod;

import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.MovementInput;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class InputDesactivator {
    private static boolean disabled = false;
    private static float yBodyRot;
    private static float yHeadRot;
    private static float xHeadRot;
    
    public static void setInputsDisabled(boolean b, PlayerEntity player) {
        disabled = b;
        if (!b) return;
        yBodyRot = player.yBodyRot;
        yHeadRot = player.yHeadRot;
        xHeadRot = player.xRot;
    }

    @SubscribeEvent
    public static void onInputUpdate(InputUpdateEvent event) {
        if (!disabled) return;

        MovementInput input = event.getMovementInput();

        input.leftImpulse = 0;
        input.forwardImpulse = 0;

        input.up = false;
        input.down = false;
        input.left = false;
        input.right = false;

        input.jumping = false;
        input.shiftKeyDown = false;
    }

    @SubscribeEvent
    public static void onGuiOpen(GuiOpenEvent event) {
        if (!disabled) return;

        if (event.getGui() instanceof InventoryScreen) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        event.setCanceled(disabled);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        event.setCanceled(disabled);
    }

    @SubscribeEvent
    public static void onRightClickEntity(PlayerInteractEvent.EntityInteract event) {
        event.setCanceled(disabled);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        event.setCanceled(disabled);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        event.setCanceled(disabled);
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent event) {
        event.setCanceled(disabled);
    }
    
//    @SubscribeEvent
//    public static void onClientTick(TickEvent.ClientTickEvent event) {
//        if (event.phase != TickEvent.Phase.END || !disabled) return;
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null) return;
//        
//        // marche pas
//        mc.options.keyInventory.setDown(false);
//        mc.options.keyUp.setDown(false);
//        mc.options.keyDown.setDown(false);
//        mc.options.keyLeft.setDown(false);
//        mc.options.keyRight.setDown(false);
//        mc.options.keyJump.setDown(false);
//        mc.options.keyShift.setDown(false);
//        mc.options.keyAttack.setDown(false);
//        mc.options.keyUse.setDown(false);
//        mc.options.keyInventory.setDown(false);
//        mc.options.keyDrop.setDown(false);
//        mc.options.keyPickItem.setDown(false);
//
//        mc.player.yBodyRot = yBodyRot;
//        mc.player.yBodyRotO = yBodyRot;
//        mc.player.yHeadRot = yHeadRot;
//        mc.player.yHeadRotO = yHeadRot;
//        mc.player.xRot = xHeadRot;
//        mc.player.xRotO = xHeadRot;
//    }
}
