package net.hamnd.testmod.world.gen.feature;

import com.google.common.collect.ImmutableList;
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
//    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> REDWOOD =
//            register("redwood",Feature.TREE.configured((new BaseTreeFeatureConfig.Builder(
//                    new SimpleBlockStateProvider(ModBlocks.REDWOOD_LOG.get().defaultBlockState()),
//                    new SimpleBlockStateProvider(ModBlocks.REDWOOD_LEAVES.get().defaultBlockState()),
//                    new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 3),
//                    new StraightTrunkPlacer(6, 4, 0),
//                    new TwoLayerFeature(1, 0, 1))).ignoreVines().build()));

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE =
            register("tree", Feature.TREE.configured((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()),
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                            new JungleFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), 2),
                            new CustomTrunkPlacer(10, 2, 19),
                            new TwoLayerFeature(1, 1, 2)))
                    .decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE)).build()));

    public static final ConfiguredFeature<?, ?> TREEA =
            register("treea", Feature.TREE.configured((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()),
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                            new JungleFoliagePlacer(FeatureSpread.fixed(3), FeatureSpread.fixed(0), 0),
                            new CustomTrunkPlacer(11, 1, 1),
                            new TwoLayerFeature(1, 1, 2)))
                    .decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE)).build()));
    
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> TREEB =
            register("treeb", Feature.TREE.configured((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.defaultBlockState()),
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.defaultBlockState()),
                            new JungleFoliagePlacer(FeatureSpread.fixed(3), FeatureSpread.fixed(0), 0),
                            new CustomTrunkPlacer(11, 1, 1),
                            new TwoLayerFeature(1, 1, 2)))
                    .decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE)).build()));
    
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String string,
                                                                          ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, string, configuredFeature);
    }

}
