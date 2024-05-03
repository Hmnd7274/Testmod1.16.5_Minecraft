package net.hamnd.testmod.entity.renderer;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.entity.custom.BuffZombieEntity;
import net.hamnd.testmod.entity.model.BuffZombieModel;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BuffZombieRenderer extends MobRenderer<BuffZombieEntity, BuffZombieModel<BuffZombieEntity>> {
    protected static final ResourceLocation TEXTURE =
            new ResourceLocation(TestMod.MOD_ID, "textures/entity/buff_zombie.png");

    public BuffZombieRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new BuffZombieModel<>(), 0.7F);
    }

    @Override
    public ResourceLocation getEntityTexture(BuffZombieEntity entity) {
        return TEXTURE;
    }
}