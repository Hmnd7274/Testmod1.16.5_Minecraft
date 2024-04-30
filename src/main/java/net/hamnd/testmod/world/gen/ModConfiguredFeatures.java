package net.hamnd.testmod.world.gen;

import net.hamnd.testmod.block.ModBlocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

public class ModConfiguredFeatures {
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> REDWOOD =
            register("redwood",Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(ModBlocks.REDWOOD_LOG.get().defaultBlockState()),
                    new SimpleBlockStateProvider(ModBlocks.REDWOOD_LEAVES.get().defaultBlockState()),
                    new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
                    new StraightTrunkPlacer(6, 4, 0),
                    new TwoLayerFeature(1, 0, 1))).ignoreVines().build()));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String string,
                                                                          ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, string, configuredFeature);
    }

}
