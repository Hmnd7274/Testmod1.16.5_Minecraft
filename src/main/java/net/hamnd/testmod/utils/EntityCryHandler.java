package net.hamnd.testmod.utils;

import net.hamnd.testmod.event.ModEvents;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.Set;

import static net.hamnd.testmod.utils.Calculator.getSmoothAllEye;

public class EntityCryHandler {
    public static void makeEntityCry(Set<SheepEntity> set){
        World world = ModEvents.world;
        for (Iterator<SheepEntity> i = set.iterator(); i.hasNext();){
            SheepEntity entity = i.next();
            Vector3d entityLeftEyeVec = getSmoothAllEye(entity, -1);
            Vector3d entityRightEyeVec = getSmoothAllEye(entity, 1);
            world.addParticle(ParticleTypes.FALLING_WATER,
                    entityLeftEyeVec.x,
                    entityLeftEyeVec.y,
                    entityLeftEyeVec.z, 0, 0, 0);
            world.addParticle(ParticleTypes.FALLING_WATER,
                    entityRightEyeVec.x,
                    entityRightEyeVec.y,
                    entityRightEyeVec.z, 0, 0, 0);
        }
    }
}