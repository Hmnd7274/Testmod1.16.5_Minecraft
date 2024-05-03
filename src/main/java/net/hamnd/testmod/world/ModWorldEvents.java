package net.hamnd.testmod.world;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.world.gen.ModEntityGeneration;
import net.hamnd.testmod.world.gen.ModOreGeneration;
import net.hamnd.testmod.world.gen.ModTreeGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID)
public class ModWorldEvents {

    @SubscribeEvent
    public static void biomeLoadingEvnt(final BiomeLoadingEvent event) {
        ModOreGeneration.generateOres(event);
        ModTreeGeneration.generateTrees(event);

        ModEntityGeneration.onEntitySpawn(event);
    }
}
