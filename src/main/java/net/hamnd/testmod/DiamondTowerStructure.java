package net.hamnd.testmod;

import net.minecraft.block.BlockState;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DiamondTowerStructure extends Structure<NoFeatureConfig> {

    public DiamondTowerStructure() {
        super(NoFeatureConfig.CODEC);
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    @Override
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeProvider biomeSource,
                                     long seed, SharedSeedRandom rand,
                                     int chunkX, int chunkZ, Biome biome,
                                     ChunkPos chunkPos, NoFeatureConfig config) {
        int x = chunkX * 16 + 8;
        int z = chunkZ * 16 + 8;
        
        // Vérifie que tous les biomes dans un rayon de 80 blocs sont de la jungle
        // → garantit qu'on est au centre d'une jungle, pas sur le bord
        for (Biome b : biomeSource.getBiomesWithin(
                x, generator.getSeaLevel(), z, 80)) {
            if (b.getBiomeCategory() != Biome.Category.JUNGLE) return false;
        }
        
        BlockPos centerOfChunk = new BlockPos((chunkX << 4) + 7, 0, (chunkZ << 4) + 7);
        int surfaceY = generator.getBaseHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
        BlockState topBlock = generator.getBaseColumn(x, z).getBlockState(centerOfChunk.above(surfaceY - 1));

        if (topBlock.getFluidState().is(FluidTags.WATER)) return false;
        TestMod.LOGGER.info("[TestMod] Found a place here, 3 = pas d'eau ? ({},{},{})", x, z, topBlock.getFluidState().isEmpty());
        
        return topBlock.getFluidState().isEmpty();
    }

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ,
                     MutableBoundingBox box, int ref, long seed) {
            super(structure, chunkX, chunkZ, box, ref, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries registries, ChunkGenerator generator,
                                   TemplateManager templates, int chunkX, int chunkZ,
                                   Biome biome, NoFeatureConfig config) {
            int x = chunkX * 16 + 8;
            int z = chunkZ * 16 + 8;
            this.pieces.add(new DiamondTowerPiece(ModStructures.DIAMOND_TOWER_PIECE, x, z));
            this.calculateBoundingBox();
            TestMod.LOGGER.info("[TestMod] generatePieces, coords ({},{})", x, z);
        }
    }
}