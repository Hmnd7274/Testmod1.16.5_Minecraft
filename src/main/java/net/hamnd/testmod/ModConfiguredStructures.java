package net.hamnd.testmod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class ModConfiguredStructures {

    public static StructureFeature<?, ?> CONFIGURED_DIAMOND_TOWER = ModStructures.DIAMOND_TOWER.get()
            .configured(IFeatureConfig.NONE);

    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> CONFIGURED_STRUCTURES = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;

        Registry.register(CONFIGURED_STRUCTURES, new ResourceLocation(TestMod.MOD_ID, "configured_diamond_tower"), CONFIGURED_DIAMOND_TOWER);
    }
}
