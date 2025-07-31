package net.hamnd.testmod.tileentity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.hamnd.testmod.block.custom.DaisyStatueBlock;
import net.hamnd.testmod.tileentity.DaisyStatueTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class DaisyStatueTileRenderer extends TileEntityRenderer<DaisyStatueTile> {

    public DaisyStatueTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(DaisyStatueTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn.getWorld() == null || !tileEntityIn.show) return;
        if (tileEntityIn.getEntity() == null) {
            tileEntityIn.createEntity();
        }
        float yaw = tileEntityIn.getBlockState().get(DaisyStatueBlock.FACING).getOpposite().getHorizontalAngle();
        tileEntityIn.getEntity().rotationYaw = yaw;
        tileEntityIn.getEntity().prevRotationYaw = yaw;
        tileEntityIn.getEntity().renderYawOffset = yaw;
        tileEntityIn.getEntity().prevRenderYawOffset = yaw;
        matrixStackIn.push();
        Minecraft.getInstance().getRenderManager().renderEntityStatic(
                tileEntityIn.getEntity(), 0.5f, 0, 0.5f, yaw, partialTicks, matrixStackIn, bufferIn, combinedLightIn
        );
        matrixStackIn.pop();
    }
}