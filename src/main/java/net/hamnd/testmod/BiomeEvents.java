package net.hamnd.testmod;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BiomeEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBiomeLoading(BiomeLoadingEvent event) {
        // N'ajoute la structure que dans les biomes de catégorie JUNGLE
        if (event.getCategory() != Biome.Category.JUNGLE) return;

        StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> configured =
                ModStructures.DIAMOND_TOWER.get().configured(NoFeatureConfig.INSTANCE);

        event.getGeneration().getStructures().add(() -> configured);

        TestMod.LOGGER.debug("[TestMod] Diamond Tower ajoutée au biome '{}'", event.getName());
    }
}
