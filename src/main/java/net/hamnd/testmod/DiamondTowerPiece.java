package net.hamnd.testmod;

import net.hamnd.testmod.world.gen.feature.ModConfiguredFeatures;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraftforge.common.Tags;

import java.time.Year;
import java.util.*;

public class DiamondTowerPiece extends StructurePiece {

    private final int centerX;
    private final int centerZ;

    public DiamondTowerPiece(IStructurePieceType type, int centerX, int centerZ, Set<Tuple<BlockPos, BlockState>> blockList) {
        super(type, 0);
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.boundingBox = new MutableBoundingBox(
                centerX - 50, 50, centerZ - 50,
                centerX + 50, 120, centerZ + 50);
    }

    public DiamondTowerPiece(IStructurePieceType type, CompoundNBT nbt, Set<Tuple<BlockPos, BlockState>> blockList) {
        super(type, nbt);
        this.centerX = nbt.getInt("CX");
        this.centerZ = nbt.getInt("CZ");
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

        int surfaceY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, centerX, centerZ);
        TestMod.LOGGER.info("[TestMod] StructurePiece placé à x,y,z: ({}, {}, {})", centerX, surfaceY, centerZ);
        
        Random fixedRandom = new Random(centerX + 67L + centerZ);
        
        // Chaque chunk nettoie ses propres arbres et lianes, rayon 23 blocs
        clearTreesInBox(world, surfaceY, 23, box);
        SpiritTreeSpawn.spawnTree(world, fixedRandom, box, new BlockPos(centerX, surfaceY, centerZ));

        return true;
    }

    private void clearTreesInBox(ISeedReader world, int surfaceY, int radius, MutableBoundingBox box) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {

                // Vérifie qu'on est dans le rayon circulaire autour du centre
                int x = dx + centerX;
                int z = dz + centerZ;
                if ((dx * dx) + (dz * dz) > radius * radius) continue;

                for (int y = surfaceY - 5; y <= surfaceY + 100; y++) {
                    BlockPos base = new BlockPos(x, y, z);
                    BlockState state = world.getBlockState(base);
                    if (state.is(BlockTags.LOGS)) {
                        fellTree(world, base, box);
                        break;
                    }
                    if (state.getBlock() == Blocks.VINE || state.getBlock() == Blocks.COCOA) {
                        placeBlock(world, base, Blocks.AIR.defaultBlockState(), box);
                    }
                }
            }
        }
    }

    private void fellTree(ISeedReader world, BlockPos start, MutableBoundingBox box) {
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos current = queue.poll();
            if (!logs.add(current)) continue;
            if (logs.size() > 200) break;

            for (BlockPos neighbor : getNeighbors(current)) {
                if (world.getBlockState(neighbor).is(BlockTags.LOGS)) {
                    queue.add(neighbor);
                }
            }
        }

        for (BlockPos log : logs) {
            placeBlock(world, log, Blocks.AIR.defaultBlockState(), box);
        }

        for (BlockPos log : logs) {
            for (int dx = -4; dx <= 4; dx++)
                for (int dy = -4; dy <= 4; dy++)
                    for (int dz = -4; dz <= 4; dz++) {
                        BlockPos leaf = log.offset(dx, dy, dz);
                        if (world.getBlockState(leaf).is(BlockTags.LEAVES))
                            placeBlock(world, leaf, Blocks.AIR.defaultBlockState(), box);
                    }
        }
    }

    private List<BlockPos> getNeighbors(BlockPos pos) {
        return Arrays.asList(
                pos.north(), pos.south(), pos.east(), pos.west(),
                pos.above(), pos.below()
        );
    }
    
    private void placeBlock(ISeedReader world, BlockPos pos, BlockState state, MutableBoundingBox box) {
        if (box.isInside(pos)) {
            world.setBlock(pos, state, 2);
        }
    }
}