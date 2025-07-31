package net.hamnd.testmod.entity.custom;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.world.World;

public class DaisyStatueEntity extends IronGolemEntity {
    public DaisyStatueEntity(EntityType<? extends IronGolemEntity> type, World world) {
        super(type, world);
        this.ignoreFrustumCheck = true;
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 100.0D).createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D).createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0D).createMutableAttribute(Attributes.ATTACK_DAMAGE, 15.0D);
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z) {
        int chunkRenderDistance = Minecraft.getInstance().gameSettings.renderDistanceChunks;
        double maxDistance = chunkRenderDistance * 16; // distance en blocs
        return this.getDistanceSq(x, y, z) < (maxDistance * maxDistance);
    }
}
