package net.hamnd.testmod.world.gen.feature;

import com.google.common.collect.ImmutableList;
import net.hamnd.testmod.block.ModBlocks;
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
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> REDWOOD =
            register("redwood",Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(
                    new SimpleBlockStateProvider(ModBlocks.REDWOOD_LOG.get().getDefaultState()),
                    new SimpleBlockStateProvider(ModBlocks.REDWOOD_LEAVES.get().getDefaultState()),
                    new BlobFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 3),
                    new StraightTrunkPlacer(6, 4, 0),
                    new TwoLayerFeature(1, 0, 1))).setIgnoreVines().build()));

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> TREE =
            register("tree", Feature.TREE.withConfiguration((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()),
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()),
                            new JungleFoliagePlacer(FeatureSpread.create(2), FeatureSpread.create(0), 2),
                            new CustomTrunkPlacer(10, 2, 10),
                            new TwoLayerFeature(1, 1, 2)))
                    .setDecorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.field_236871_b_)).build()));

    public static final ConfiguredFeature<?, ?> TREEA =
            register("treea", Feature.TREE.withConfiguration((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()),
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()),
                            new JungleFoliagePlacer(FeatureSpread.create(3), FeatureSpread.create(0), 0),
                            new CustomTrunkPlacer(14, 7, 1),
                            new TwoLayerFeature(1, 1, 2)))
                    .setDecorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.field_236871_b_)).build())
//                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).chance(100)
            );
    
    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> TREEB =
            register("treeb", Feature.TREE.withConfiguration((
                    new BaseTreeFeatureConfig.Builder(
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LOG.getDefaultState()),
                            new SimpleBlockStateProvider(Blocks.JUNGLE_LEAVES.getDefaultState()),
                            new JungleFoliagePlacer(FeatureSpread.create(3), FeatureSpread.create(0), 0),
                            new CustomTrunkPlacer(11, 1, 1),
                            new TwoLayerFeature(1, 1, 2)))
                    .setDecorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.field_236871_b_)).build()));
    
    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String string,
                                                                          ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, string, configuredFeature);
    }

}
