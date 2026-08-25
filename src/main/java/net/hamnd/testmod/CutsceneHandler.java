package net.hamnd.testmod;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.*;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Rotations;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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

    public static Vector3d centerPos = new Vector3d(x, 100, z);
    public static Vector3d pos = new Vector3d(x, 100, z);
    /**
     * Yaw, Pitch, Roll
     */
    public static Rotations rot = new Rotations((float) angle, 35, 0);
    
    public static void triggerCutscene(BlockPos scenePosition) {
        TestMod.LOGGER.info("Cutscene Triggered");
        CutsceneOn = true;
        width = 0;
        centerPos = new Vector3d(scenePosition.getX(), scenePosition.getY(), scenePosition.getZ());
    }

    private static final int BAR_HEIGHT = 35;
    private static int width = 0;
    
    @SubscribeEvent
    public static void onCameraSetup(EntityViewRenderEvent.CameraSetup event) {
        if (!CutsceneOn) return;
        angle += 40 * Math.PI / (360*60);
        angleRot += (float) (40 * Math.PI / 360);
        x = Math.cos(angle) * radius;
        z = Math.sin(angle) * radius;
        pos = new Vector3d(centerPos.x + x, centerPos.y + 1, centerPos.z + z);
        rot = new Rotations(angleRot + 90, 20, 0);
        TestMod.LOGGER.info("angle " + angle);
        event.getInfo().setPosition(pos);
        event.setYaw(rot.getX());
        event.setPitch(rot.getY());
        event.setRoll(rot.getZ());
    }

    public static boolean enabled = false;
    public static float testangle = 0f; // angle de rotation en degrés

    @SubscribeEvent
    public static void onPlayer(PlayerInteractEvent.LeftClickEmpty event){
        if (!event.getPlayer().level.isClientSide) return;
        TestMod.sendMessage("LEFT CLICK");
        CutsceneOn = false;
        width = 0;
    }
//    
//    @SubscribeEvent
//    public static void onPlayer2(PlayerInteractEvent.LeftClickBlock event){
//        if (!event.getPlayer().level.isClientSide) return;
//        TestMod.LOGGER.info("Item Picked Up");
//        enabled = !enabled;
//        TestMod.LOGGER.info(enabled);
//    }
    

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
    
    public static Set<Tuple<BlockPos, SpiritualLogBlock.TreeParts>> testdebugtreepos = Sets.newHashSet();
    
    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        
        MatrixStack matrixStack = event.getMatrixStack();
        Minecraft mc = Minecraft.getInstance();
        Vector3d camPos = mc.gameRenderer.getMainCamera().getPosition();

        IVertexBuilder linebuilder = PERSISTENT_BUFFER.getBuffer(RenderType.lines());
        BlockRendererDispatcher dispatcher = mc.getBlockRenderer();


        matrixStack.pushPose();
        matrixStack.translate(-camPos.x, -camPos.y, -camPos.z);
        
        for (Tuple<BlockPos, SpiritualLogBlock.TreeParts> tuple : testdebugtreepos) {
            BlockPos pos = tuple.getA();
            double x = pos.getX();
            double y = pos.getY();
            double z = pos.getZ();
            float r = 1f, g = 1f, b = 1f, a = 1f;
            switch (tuple.getB()) {
                case TRUNK:
                    r = 0f;
                    g = 0f;
                    break;
                case BRANCH:
                    r = 0f;
                    b = 0f;
                    break;
                case ROOT:
                    g = 0f;
                    b = 0f;
                    break;
                default:
                    break;
            }
            
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

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent event) {
        event.setCanceled(CutsceneOn);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.ALL) return;
        event.setCanceled(CutsceneOn);
    }
    
    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (!CutsceneOn) return;
        if (event.getType() != RenderGameOverlayEvent.ElementType.ALL) return;
        
        if (width < BAR_HEIGHT) width += 1;
        
        Minecraft mc = Minecraft.getInstance();
        MainWindow window = mc.getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        int color = 0xFF000000; // noir opaque (ARGB)

        // Bande du haut
        AbstractGui.fill(event.getMatrixStack(), 0, 0, screenWidth, width, color);

        // Bande du bas
        AbstractGui.fill(event.getMatrixStack(), 0, screenHeight - width, screenWidth, screenHeight, color);
    }
}