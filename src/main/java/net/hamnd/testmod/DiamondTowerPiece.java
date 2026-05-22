package net.hamnd.testmod;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.nbt.CompoundNBT;
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

    public DiamondTowerPiece(IStructurePieceType type, int centerX, int centerZ) {
        super(type, 0);
        this.centerX = centerX;
        this.centerZ = centerZ;
        this.boundingBox = new MutableBoundingBox(
                centerX - 50, 50, centerZ - 50,
                centerX + 50, 120, centerZ + 50);
    }

    public DiamondTowerPiece(IStructurePieceType type, CompoundNBT nbt) {
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
        
        // Chaque chunk nettoie ses propres arbres et lianes
        clearTreesInBox(world, surfaceY, 23, box);
        TestMod.LOGGER.info("[TestMod] postProcess tour a ({}, {}, {})", centerX, surfaceY, centerZ);
        placeDome(world, centerX, surfaceY, centerZ, 23, box);
        SpiritTreeSpawn.spawnTree(world, rand, box, new BlockPos(centerX, surfaceY, centerZ));

        // Seul le chunk central place la tour
//        if (chunkPos.x == centerX >> 4 && chunkPos.z == centerZ >> 4) {
//            TestMod.LOGGER.info("[TestMod] postProcess tour a ({}, {}, {})", centerX, surfaceY, centerZ);
////            placeTower(world, centerX, surfaceY, centerZ);
//            placeDome(world, centerX, surfaceY, centerZ, 13);
//            SpiritTreeSpawn.spawnTree(world, rand, box, new BlockPos(centerX, surfaceY, centerZ));
//        }

        return true;
    }

    // ---- Suppression des arbres dans le chunk courant ----

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

    // ---- Placement de la tour ----

    private void placeTower(ISeedReader world, int x, int baseY, int z, MutableBoundingBox box) {
        int height = 100;

        for (int y = 0; y < height; y++) {
            if (y == 0 || y == height - 1) {
                for (int dx = -1; dx <= 1; dx++)
                    for (int dz = -1; dz <= 1; dz++)
                        placeBlock(world, new BlockPos(x + dx, baseY + y, z + dz),
                                Blocks.DIAMOND_BLOCK.defaultBlockState(), box);
            } else {
                for (int dx = -1; dx <= 1; dx++)
                    for (int dz = -1; dz <= 1; dz++)
                        if (Math.abs(dx) == 1 || Math.abs(dz) == 1)
                            placeBlock(world, new BlockPos(x + dx, baseY + y, z + dz),
                                    Blocks.DIAMOND_BLOCK.defaultBlockState(), box);
            }
        }

        BlockPos top = new BlockPos(x, baseY + height, z);
        placeBlock(world, top.north(), Blocks.TORCH.defaultBlockState(), box);
        placeBlock(world, top.south(), Blocks.TORCH.defaultBlockState(), box);
        placeBlock(world, top.east(), Blocks.TORCH.defaultBlockState(), box);
        placeBlock(world, top.west(), Blocks.TORCH.defaultBlockState(), box);
    }

    private void placeDome(ISeedReader world, int x, int baseY, int z, int radius, MutableBoundingBox box) {
//        // Troncs qui montent du sol jusqu'au dôme — placés aléatoirement dans le cercle
//        Random rand = new Random(x * 31L + z);
//        int numTrunks = 8;
//        for (int i = 0; i < numTrunks; i++) {
//            // Position aléatoire dans le cercle
//            double angle = rand.nextDouble() * Math.PI * 2;
//            int dist = 8 + rand.nextInt(10); // entre 8 et 18 blocs du centre
//            int tx = x + (int)(Math.cos(angle) * dist);
//            int tz = z + (int)(Math.sin(angle) * dist);
//            int ty = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, tx, tz);
//
//            // Monte jusqu'à la hauteur du dôme à cette distance
//            double distFromCenter = Math.sqrt((tx - x) * (tx - x) + (tz - z) * (tz - z));
//            int domeY = baseY + (int)Math.sqrt(Math.max(0, radius * radius - distFromCenter * distFromCenter));
//
//            for (int y = ty; y <= domeY; y++) {
//                placeBlock(world, new BlockPos(tx, y, tz),
//                        Blocks.JUNGLE_LOG.defaultBlockState(), box);
//            }
//        }

        //Pics
        List<Tuple<Integer, Integer>> leafList = Arrays.asList(
                new Tuple<>(0, 0),            // haut, droite
                new Tuple<>(1, 0),
                new Tuple<>(0, 1),
                new Tuple<>(0, -1)
        );
        Random rand = new Random(x * 67L + z);
        float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
        
        for (int w = 0; w < 8; w++) {
            float spacing = ((float)(2 * Math.PI) / 8) * w;
            float angle = baseAngle + spacing;
//
//            for (int i = 0; i<leafList.size(); i++) {
//
//                // Vecteur relatif avant rotation
//                float vx = radius + leafList.get(i).getA();
//                float vz = leafList.get(i).getB();
//
//                // Applique la rotation
//                float rx = vx * (float) Math.cos(angle) - vz * (float) Math.sin(angle);
//                float rz = vx * (float) Math.sin(angle) + vz * (float) Math.cos(angle);
//
//                // Ajoute le centre
//                int finalX = x + (int) rx;
//                int finalZ = z + (int) rz;
//                int finalY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, finalX, finalZ);
//                BlockPos woodPos = new BlockPos(finalX, finalY, finalZ);
//                
//                if (i == 0) {
//                    placeBlock(world, woodPos,
//                            Blocks.GOLD_BLOCK.defaultBlockState(), box);
//                } else

                // Vecteur relatif avant rotation
                float vx = radius - 1;
                float vz = 0;

                // Applique la rotation
                float rx = vx * (float) Math.cos(angle) - vz * (float) Math.sin(angle);
                float rz = vx * (float) Math.sin(angle) + vz * (float) Math.cos(angle);

                // Ajoute le centre
                int finalX = x + (int) rx;
                int finalZ = z + (int) rz;
                int finalY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, finalX, finalZ);
                BlockPos woodPos = new BlockPos(finalX, finalY, finalZ);
                    placeBlock(world, woodPos,
                            Blocks.REDSTONE_BLOCK.defaultBlockState(), box);
//            }
        }

        // Dôme de feuilles — place un bloc si il est dans la sphère
        // ET qu'au moins un voisin est hors de la sphère (= surface)
//        for (int dx = -20; dx <= 20; dx++) {
//            for (int dy = 0; dy <= 4; dy++) {
//                for (int dz = -20; dz <= 20; dz++) {
//                    int dist = (dx * dx) + ((dy + 4) * (dy + 4)) + (dz * dz);
//                    int radiusSquared = radius*radius;
//                    int finalX = x + dx;
//                    int finalZ = z + dz;
//                    int finalY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, finalX, finalZ) + dy;
//                    
//                    BlockPos currBlockPos = new BlockPos(finalX, finalY, finalZ);
//                    if (dist <= radiusSquared + 20 && dist >= radiusSquared - 20
//                        && world.getBlockState(currBlockPos).getMaterial() != Material.DIRT
//                        && world.getBlockState(currBlockPos) != Blocks.REDSTONE_BLOCK.defaultBlockState()) {
////                      
//                        placeBlock(world, currBlockPos,
//                                Blocks.GLASS.defaultBlockState(), box);
//
//                    }
//                }
//            }
//        }

        for (int w = 0; w < 8; w++) {
            float angle = baseAngle + ((float)(2 * Math.PI) / 8) * w;

            // Chaque pic va du bord (radius) vers le centre (0)
            // en montant progressivement
            for (int dist = radius; dist >= 0; dist--) {
                int height = (radius - dist) / 3; // monte de 1 bloc tous les 3 blocs vers le centre

                // Largeur du pic — plus large à la base, plus fin au centre
                int width = dist / 6;

                for (int dw = -width; dw <= width; dw++) {
                    // Vecteur principal
                    float vx = dist;
                    float vz = dw; // élargit perpendiculairement au bras

                    // Rotation
                    int finalX = x + (int)(vx * Math.cos(angle) - vz * Math.sin(angle));
                    int finalZ = z + (int)(vx * Math.sin(angle) + vz * Math.cos(angle));
                    int finalY = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, finalX, finalZ) + height;
                    BlockPos currBlockPos = new BlockPos(finalX, finalY, finalZ);
                    
                    if (world.getBlockState(currBlockPos).getMaterial() != Material.DIRT
                        && world.getBlockState(currBlockPos) != Blocks.REDSTONE_BLOCK.defaultBlockState()) {
                    
                        placeBlock(world, currBlockPos,
                                Blocks.GLASS.defaultBlockState(), box);
                    }
                }
            }
        }
    }
    
    private void placeBlock(ISeedReader world, BlockPos pos, BlockState state, MutableBoundingBox box) {
        if (box.isInside(pos))
            world.setBlock(pos, state, 3);
    }
}