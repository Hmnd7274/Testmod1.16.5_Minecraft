package net.hamnd.testmod.entity.renderer;

import net.hamnd.testmod.entity.model.DaisyModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IronGolemRenderer;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.util.ResourceLocation;

public class DaisyRenderer extends IronGolemRenderer {
    private static final ResourceLocation DAISY_TEXTURES = new ResourceLocation("testmod", "textures/entity/daisy.png");

    public DaisyRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        this.entityModel = new DaisyModel<>();
    }

    public ResourceLocation getEntityTexture(IronGolemEntity entity) {
        return DAISY_TEXTURES;
    }
}
