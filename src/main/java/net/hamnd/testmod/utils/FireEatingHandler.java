package net.hamnd.testmod.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.item.ModItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.entity.projectile.SmallFireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.datafix.fixes.ChunkPaletteFormat;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID)
public class FireEatingHandler {

    // Map pour suivre les joueurs qui ont mangé du feu
    private static final Map<UUID, Integer> fireTimers = new HashMap<>();

    @SubscribeEvent
    public static void onFinishEating(LivingEntityUseItemEvent.Finish event) {
        if (event.getEntityLiving().getEntityWorld() instanceof ServerWorld && event.getItem().getItem() == ModItems.FIRE_ITEM.get()) {
            if (event.getEntityLiving() instanceof PlayerEntity) {
                event.getEntity().setFire(5);
                fireTimers.put(event.getEntityLiving().getUniqueID(), 300);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        UUID id = player.getUniqueID();

        if (fireTimers.containsKey(id)) {
            int timer = fireTimers.get(id);

            if (timer > 0) {
                // Toutes les 40 ticks = 0.2 secondes
                int randNumb = player.getRNG().nextInt(3);
                if (timer == 1) {
                    shootBigFireball(player);
                }
                else if (timer % (1 + randNumb) == 0) {
                    shootFireball(player);
                }

                fireTimers.put(id, timer - 1);
            } else {
                // Timer fini, on arrête
                fireTimers.remove(id);
            }
        }
    }

    private static void shootFireball(PlayerEntity player) {
        World world = player.getEntityWorld();

        if (world instanceof ServerWorld) {
            // Orientation horizontale du joueur
            float yaw = player.renderYawOffset;
            double xDir = -Math.sin(Math.toRadians(yaw));
            double zDir =  Math.cos(Math.toRadians(yaw));
            double yDir = -0.1;

            double speed = 2.0; // 1.0 = normal, >1 plus rapide
            // On inverse pour tirer derrière
            Vector3d opposite = new Vector3d(xDir * speed, yDir * speed, zDir * speed).scale(-1);

            // Position de spawn du projectile (devant ou derrière le mob)
            double offset = 0.45;
            if (player.isSneaking()) offset = 0.3;
            double x =  player.getPosX();
            double y =  player.getPosY() + offset;
            double z =  player.getPosZ();
            
            double spread = 0.8; // plus tu augmentes, plus ça "s'écarte"
            
            double x1 = player.getRNG().nextDouble() * spread;
            double y1 = player.getRNG().nextDouble() * spread;
            double z1 = player.getRNG().nextDouble() * spread;
            SmallFireballEntity smallfireballentity = new SmallFireballEntity(player.world,
                    player,
                    opposite.x + x1,
                    opposite.y + y1,
                    opposite.z + z1
            );
            // Place la boule au niveau du mob
            smallfireballentity.setPosition(x + (opposite.x) * 0.2 , y + (opposite.y) * 0.2, z + (opposite.z) * 0.2);
            player.getEntityWorld().addEntity(smallfireballentity);
        }
    }
    
    private static void shootBigFireball(PlayerEntity player) {
        float yaw = player.renderYawOffset;
        double xDir = -Math.sin(Math.toRadians(yaw));
        double zDir =  Math.cos(Math.toRadians(yaw));
        double yDir = -0.1;

        double speed = 2.0;

        Vector3d opposite = new Vector3d(xDir * speed, yDir * speed, zDir * speed).scale(-1);

        double offset = 0.45;
        if (player.isSneaking()) offset = 0.3;
        double x =  player.getPosX();
        double y =  player.getPosY() + offset;
        double z =  player.getPosZ();

        FireballEntity fireballentity = new FireballEntity(player.world,
                player,
                opposite.x,
                opposite.y,
                opposite.z
        );
        // Place la boule au niveau du mob
        fireballentity.setPosition(x + (opposite.x) * 0.2 , y + (opposite.y) * 0.2, z + (opposite.z) * 0.2);
        player.getEntityWorld().addEntity(fireballentity);
    }
}