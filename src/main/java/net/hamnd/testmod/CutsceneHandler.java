package net.hamnd.testmod;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
public class CutsceneHandler {
    
    public static boolean CutsceneOn = false;
    
    public static double radius = 10.0;
    public static double angle = 0.0;
    public static float angleRot = 0;
    public static double x = Math.cos(angle) * radius;
    public static double z = Math.sin(angle) * radius;
    
    public static Vector3d pos = new Vector3d(x, 100, z);
    /**
     * Yaw, Pitch, Roll
     */
    public static Rotations rot = new Rotations((float) angle, 35, 0);

    @SubscribeEvent
    public static void onPlayer(PlayerInteractEvent.LeftClickEmpty event){
        if (!event.getPlayer().level.isClientSide) return;
        TestMod.LOGGER.info("LEFT CLICK");
        setCutsceneMode(!CutsceneOn);
    }
    
    public static void setCutsceneMode(boolean mode){
        TestMod.LOGGER.info("cutscene mode change to " + mode);
        CutsceneOn = mode;
    }
    
    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!CutsceneOn) return;
        event.getInfo().setPosition(pos);
        event.setYaw(rot.getX());
        event.setPitch(rot.getY());
        event.setRoll(rot.getZ());
        angle += 40 * Math.PI / (360*60);
        angleRot += (float) (40 * Math.PI / 360);
        x = Math.cos(angle) * radius;
        z = Math.sin(angle) * radius;
        pos = new Vector3d(x, 100, z);
        rot = new Rotations(angleRot + 90, 35, 0);
        TestMod.LOGGER.info("angle " + angle);
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        event.setCanceled(CutsceneOn);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        event.setCanceled(CutsceneOn);
    }

    public static boolean enabled = false;
    public static float testangle = 0f; // angle de rotation en degrés

    @SubscribeEvent
    public static void onPlayer2(PlayerInteractEvent.LeftClickBlock event){
        if (!event.getPlayer().level.isClientSide) return;
        TestMod.LOGGER.info("Item Picked Up");
        enabled = !enabled;
        TestMod.LOGGER.info(enabled);
    }

    // Static, créé une seule fois au lieu de chaque frame
    private static final IRenderTypeBuffer.Impl PERSISTENT_BUFFER =
            IRenderTypeBuffer.immediate(new BufferBuilder(256));
    private static final BlockPos pivot = new BlockPos(0, 100, 0);
    private static final List<BlockPos> treeBlocks = Lists.newArrayList(
            pivot,
            pivot.above(),
            pivot.above(2),
            pivot.above(3),
            pivot.above(3).north()
    );
    
    public static Set<BlockPos> testdebugtreepos = Sets.newHashSet();
    
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        
        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft mc = Minecraft.getInstance();
        Vector3d camPos = mc.gameRenderer.getMainCamera().getPosition();

        IVertexBuilder linebuilder = PERSISTENT_BUFFER.getBuffer(RenderType.lines());
        BlockRendererDispatcher dispatcher = mc.getBlockRenderer();


        matrixStack.pushPose();
        matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
        
        for (BlockPos pos : testdebugtreepos) {
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();

            // Bleu, opaque
            float r = 0f, g = 0.4f, b = 1f, a = 1f;

            WorldRenderer.renderLineBox(
                    matrixStack,
                    linebuilder,
                    x, y, z, x + 1, y + 1, z + 1,
                    r, g, b, a,
                    r, g, b  // <-- normale (réutilise juste la couleur, ça n'a pas d'impact visuel sur un wireframe)
            );
        }

        matrixStack.popPose();
        PERSISTENT_BUFFER.endBatch(RenderType.lines());
        
        if (!enabled) return;

        IVertexBuilder solidbuilder = PERSISTENT_BUFFER.getBuffer(RenderType.solid());
        
        matrixStack.pushPose();
        testangle += 1.0f; // fait tourner petit à petit, incrémente à chaque frame

        // 1. Se placer au pivot dans le monde (caméra-relatif)
        matrixStack.translate(pivot.getX() - camPos.x, pivot.getY() - camPos.y, pivot.getZ() - camPos.z);

        // 2. Tourner UNE SEULE FOIS autour de ce point pivot — tout ce qui est dessiné après hérite de cette rotation
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(testangle));

        // 3. Dessiner chaque bloc à sa position relative au pivot (pas de rotation individuelle !)
        for (BlockPos worldPos : treeBlocks) {
//            BlockState state = mc.level.getBlockState(worldPos); // ou une state stockée si le vrai bloc a déjà été retiré
            BlockState state = Blocks.GOLD_BLOCK.defaultBlockState(); // ou une state stockée si le vrai bloc a déjà été retiré

            matrixStack.pushPose();

            // Décalage relatif au pivot dans le repère déjà tourné
            BlockPos relative = worldPos.subtract(pivot);
            matrixStack.translate(relative.getX(), relative.getY(), relative.getZ());

            dispatcher.renderBatched(
                    state,
                    worldPos, // toujours la vraie position monde pour la lumière/AO
                    mc.level,
                    matrixStack,
                    solidbuilder,
                    false,
                    mc.level.random
            );

            matrixStack.popPose();
        }

        PERSISTENT_BUFFER.endBatch();

        matrixStack.popPose();
    }
}