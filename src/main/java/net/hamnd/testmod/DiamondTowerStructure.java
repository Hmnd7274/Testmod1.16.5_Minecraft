package net.hamnd.testmod;

import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;

public class DiamondTowerStructure extends Structure<NoFeatureConfig> {

    public DiamondTowerStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public GenerationStage.Decoration step() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }

    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return Start::new;
    }

    // Pas besoin d'override isFeatureChunk : le filtre biome est dans BiomeEvents.
    // La méthode parente retourne true par défaut → la structure spawn partout
    // où elle est ajoutée, i.e. uniquement dans les jungles.

    public static class Start extends StructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkZ,
                     MutableBoundingBox box, int ref, long seed) {
            super(structure, chunkX, chunkZ, box, ref, seed);
        }

        @Override
        public void generatePieces(DynamicRegistries registries,
                                    ChunkGenerator generator,
                                    TemplateManager templates,
                                    int chunkX, int chunkZ,
                                    Biome biome,
                                    NoFeatureConfig config) {
            // Centre du chunk
            int x = chunkX * 16 + 8;
            int z = chunkZ * 16 + 8;

            this.pieces.add(new DiamondTowerPiece(
                    ModStructures.DIAMOND_TOWER_PIECE, x, z));
            this.calculateBoundingBox();

            TestMod.LOGGER.info("[TestMod] DiamondTower Start créé à chunk ({},{})", chunkX, chunkZ);
        }
    }
}
