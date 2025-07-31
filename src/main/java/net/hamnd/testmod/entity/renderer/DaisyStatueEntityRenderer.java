package net.hamnd.testmod.entity.renderer;

import net.hamnd.testmod.entity.model.DaisyStatueEntityModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;

public class DaisyStatueEntityRenderer extends IronGolemRenderer {
    private static final ResourceLocation DAISY_STATUE_TEXTURES = new ResourceLocation("testmod", "textures/entity/daisy.png");

    public DaisyStatueEntityRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.entityModel = new DaisyStatueEntityModel<>();
        this.shadowSize = 0f;
    }

    public ResourceLocation getEntityTexture(IronGolemEntity entity) {
        return DAISY_STATUE_TEXTURES;
    }
}
