package net.hamnd.testmod;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.ScatteredStructurePiece;
import net.minecraft.world.gen.feature.structure.StructureManager;

import java.util.Random;

public class DiamondTowerPiece extends ScatteredStructurePiece {

    private final int centerX;
    private final int centerZ;

    // Constructeur appelé à la génération
    public DiamondTowerPiece(IStructurePieceType type, int centerX, int centerZ) {
        super(type, buildNbt(centerX, centerZ));
        this.centerX = centerX;
        this.centerZ = centerZ;
        // BoundingBox approximatif — sera recalculé après placement
        this.boundingBox = new MutableBoundingBox(
                centerX - 2, 50, centerZ - 2,
                centerX + 2, 120, centerZ + 2);
    }

    // Constructeur appelé lors du chargement depuis NBT (sauvegarde)
    public DiamondTowerPiece(IStructurePieceType type, CompoundNBT nbt) {
        super(type, nbt);
        this.centerX = nbt.getInt("CX");
        this.centerZ = nbt.getInt("CZ");
    }

    private static CompoundNBT buildNbt(int cx, int cz) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt("CX", cx);
        nbt.putInt("CZ", cz);
        return nbt;
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT nbt) {
        nbt.putInt("CX", centerX);
        nbt.putInt("CZ", centerZ);
    }

    @Override
    public boolean postProcess(ISeedReader world, StructureManager manager,
                                   ChunkGenerator generator, Random rand,
                                   MutableBoundingBox box, ChunkPos chunkPos,
                                   BlockPos pos) {
        // Trouve la surface au centre de la tour
        int surfaceY = world.getHeight(
                net.minecraft.world.gen.Heightmap.Type.WORLD_SURFACE_WG,
                centerX, centerZ);

        TestMod.LOGGER.info("[TestMod] DiamondTowerPiece generate() à ({}, {}, {})",
                centerX, surfaceY, centerZ);

        placeTower(world, centerX, surfaceY, centerZ);
        return true;
    }

    private void placeTower(ISeedReader world, int x, int baseY, int z) {
        int height = 20; // hauteur de la tour

        for (int y = 0; y < height; y++) {
            BlockPos current = new BlockPos(x, baseY + y, z);

            if (y == 0 || y == height - 1) {
                // Base et sommet : couche pleine 3x3
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        world.setBlock(current.offset(dx, 0, dz),
                                Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
                    }
                }
            } else {
                // Corps : murs creux (3x3 creux = juste les bords)
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        // Ne place que les bords, pas le centre
                        if (Math.abs(dx) == 1 || Math.abs(dz) == 1) {
                            world.setBlock(current.offset(dx, 0, dz),
                                    Blocks.DIAMOND_BLOCK.defaultBlockState(), 3);
                        }
                    }
                }
            }
        }

        // Phare au sommet : une torche sur chaque face
        BlockPos top = new BlockPos(x, baseY + height, z);
        world.setBlock(top.north(), Blocks.TORCH.defaultBlockState(), 3);
        world.setBlock(top.south(), Blocks.TORCH.defaultBlockState(), 3);
        world.setBlock(top.east(), Blocks.TORCH.defaultBlockState(), 3);
        world.setBlock(top.west(), Blocks.TORCH.defaultBlockState(), 3);
    }
}
