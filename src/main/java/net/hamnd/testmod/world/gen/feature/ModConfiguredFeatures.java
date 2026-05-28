package net.hamnd.testmod.world.gen.feature;

import com.google.common.collect.ImmutableList;
import net.hamnd.testmod.ModBlocks;
import net.hamnd.testmod.world.gen.trunkplacer.CustomTrunkPlacer;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.JungleFoliagePlacer;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraft.world.gen.trunkplacer.*;

public class ModConfiguredFeatures {
    
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE =
            register("tree", Feature.TREE.configured((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(ModBlocks.SPIRIT_LOG.get().defaultBlockState()),
                            new SimpleBlockStateProvider(ModBlocks.SPIRIT_LEAVES.get().defaultBlockState()),
                            new JungleFoliagePlacer(FeatureSpread.fixed(3), FeatureSpread.fixed(0), 2),
                            new CustomTrunkPlacer(13, 2, 0),
                            new TwoLayerFeature(1, 1, 2)))
                    .decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE)).build()));
    
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String string,
                                                                          ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, string, configuredFeature);
    }
}
