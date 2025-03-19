package net.hamnd.testmod.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;

public class DebugRenderer {

    public static void drawLine(MatrixStack matrixStack, Vector3d start, Vector3d end, float r, float g, float b, float alpha) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.lineWidth(4.0F);

        RenderSystem.disableDepthTest();

        Matrix4f matrix = matrixStack.getLast().getMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);

        // Convert world coordinates to local render coordinates
        Minecraft mc = Minecraft.getInstance();
        ActiveRenderInfo camera = mc.gameRenderer.getActiveRenderInfo();
        Vector3d view = camera.getProjectedView();

        buffer.pos(matrix, (float) (start.x - view.x), (float) (start.y - view.y), (float) (start.z - view.z))
                .color(r, g, b, alpha).endVertex();
        buffer.pos(matrix, (float) (end.x - view.x), (float) (end.y - view.y), (float) (end.z - view.z))
                .color(r, g, b, alpha).endVertex();

        tessellator.draw();

        RenderSystem.enableDepthTest();

        RenderSystem.enableTexture();
    }
}