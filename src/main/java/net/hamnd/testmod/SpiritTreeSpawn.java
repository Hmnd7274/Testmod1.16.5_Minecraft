package net.hamnd.testmod;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import java.sql.Array;
import java.util.*;

import static net.minecraft.world.gen.feature.TreeFeature.isAirOrLeaves;

public class SpiritTreeSpawn{
    public static final List<Integer> weightedList = Arrays.asList(1,2,2,3,3,3);
    public static final List<Integer> weightedListTwo = Arrays.asList(3,4,4,4);
    public static final List<Integer> weightedListFoliage = Arrays.asList(0,0,1);
    public static final List<Integer> weightedListRoots = Arrays.asList(3,3,4,4,5);
    public static final List<Integer> weightedListRootsGoDown = Arrays.asList(3,4,4,5);
    public static final BlockState logBlock = Blocks.JUNGLE_LOG.defaultBlockState();
    
    private List<BlockPos> trunkBlocks = Collections.emptyList();

    public void spawnTree(ISeedReader world, StructureManager manager,
                          ChunkGenerator generator, Random rand,
                          MutableBoundingBox box, ChunkPos chunkPos,
                          BlockPos pos) {
        
        
    }
    
    public List<FoliagePlacer.Foliage> getFoliages(ISeedReader world, StructureManager manager,
                                                   ChunkGenerator generator, Random rand,
                                                   MutableBoundingBox box, ChunkPos chunkPos,
                                                   BlockPos pos) {
        
        List<FoliagePlacer.Foliage> foliagePlacerList = Lists.newArrayList();
        int nbBranch = 0;
        int height = rand.nextInt(3) + 10;

        // 1️⃣ Générer le tronc principal
        for (int y = 0; y < height; y++) {
            BlockPos logPos = pos.above(y);
            placeLog(world, logPos, logBlock); // pose un bloc de bois
            placeLog(world, logPos.offset(1,0,0), logBlock); // pose un bloc de bois
            placeLog(world, logPos.offset(1,0,1), logBlock); // pose un bloc de bois
            placeLog(world, logPos.offset(0,0,1), logBlock); // pose un bloc de bois
        }

        // Générer les raçines
        placeRoots(world, rand, pos);

        // 2️⃣ Générer des petites branches qui montent
        int start = 3;
        for (int y = start; y < height - 1; y += 2) {

            if (y == height - 2) {
                int length = 3 + rand.nextInt(2);
                int random = weightedListTwo.get(rand.nextInt(weightedListTwo.size()));
                int hauteur = (height - y - 1 - rand.nextInt(2));
                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);

                for (int w = 0; w < random; w += 1) {
                    nbBranch++;

                    float spacing = ((float)(2 * Math.PI) / random) * w;
                    float angle = baseAngle + spacing;
                    int dy;
                    int dx;
                    int dz;

                    BlockPos branchIni = getClosestTrunkBlock(new BlockPos[]{pos.above(y), pos.above(y).offset(1,0,0), pos.above(y).offset(1,0,1), pos.above(y).offset(0,0,1)}, new BlockPos(Math.round(MathHelper.cos(angle) * length), hauteur, Math.round(MathHelper.sin(angle) * length)));
                    BlockPos branchStart;
                    BlockPos branchEnd = null;

                    for (int tkt = 0; tkt < 3; tkt += 1) {
                        if (tkt == 0) {
                            branchStart = branchIni;
                            dy = (int) (hauteur / 5.5);
                            dx = (int) Math.round(MathHelper.cos(angle) * (length / 2.5));
                            dz = (int) Math.round(MathHelper.sin(angle) * (length / 2.5));
                            branchEnd = branchIni.offset(dx, dy, dz);
                        } else if (tkt == 1) {
                            branchStart = branchEnd;
                            dy = ((hauteur / 3) * 2);
                            dx = (int) Math.round(MathHelper.cos(angle) * ((length / 7.0)) * 5);
                            dz = (int) Math.round(MathHelper.sin(angle) * ((length / 7.0)) * 5);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        } else {
                            branchStart = branchEnd;
                            dy = hauteur;
                            dx = Math.round(MathHelper.cos(angle) * length);
                            dz = Math.round(MathHelper.sin(angle) * length);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        }
                        placeBranch(world, rand, branchStart, branchEnd, foliagePlacerList);
                    }
                    foliagePlacerList.add(new FoliagePlacer.Foliage(branchEnd, -1, false));
                }
            } else {

                int length = 7 + rand.nextInt(3);
//            Calculator.sendMsg("length = " + length);

                int random = weightedList.get(rand.nextInt(weightedList.size()));
                if ((random == 1 || random == 2 ) && y < height - 5) {
                    int hauteur = (height - y - 4 - rand.nextInt(2));
                    float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);

                    nbBranch++;

                    float angle = baseAngle;
                    int dy;
                    int dx;
                    int dz;

                    BlockPos branchIni = getClosestTrunkBlock(new BlockPos[]{pos.above(y+1), pos.above(y+1).offset(1,0,0), pos.above(y+1).offset(1,0,1), pos.above(y+1).offset(0,0,1)}, new BlockPos(Math.round(MathHelper.cos(angle) * length), hauteur, Math.round(MathHelper.sin(angle) * length)));
                    BlockPos branchStart;
                    BlockPos branchEnd = null;

                    for (int tkt = 0; tkt < 3; tkt += 1) {
                        if (tkt == 0) {
                            branchStart = branchIni;
                            dy = (int) (hauteur / 5.5);
                            dx = (int) Math.round(MathHelper.cos(angle) * (length / 2.5));
                            dz = (int) Math.round(MathHelper.sin(angle) * (length / 2.5));
                            branchEnd = branchIni.offset(dx, dy, dz);
                        } else if (tkt == 1) {
                            branchStart = branchEnd;
                            dy = ((hauteur / 3) * 2);
                            dx = (int) Math.round(MathHelper.cos(angle) * ((length / 7.0)) * 5);
                            dz = (int) Math.round(MathHelper.sin(angle) * ((length / 7.0)) * 5);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        } else {
                            branchStart = branchEnd;
                            dy = hauteur;
                            dx = Math.round(MathHelper.cos(angle) * length);
                            dz = Math.round(MathHelper.sin(angle) * length);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        }
                        placeBranch(world, rand, branchStart, branchEnd, foliagePlacerList);
                    }
                    foliagePlacerList.add(new FoliagePlacer.Foliage(branchEnd, -1, false));
                }
//            int random = new Random().nextInt(3);
//            random = 1;
//            Calculator.sendMsg("random = " + random);
                // entre 1 et 2 branches par block de hauteur

                int hauteur = (height - y - 2 - rand.nextInt(2));
                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);

                // choisir si plusieurs branches autour du même block
                for (int w = 0; w < random; w += 1) {
                    nbBranch++;
//                float angle = ((rand.nextFloat() * ((float) Math.PI * 2F)) + ((360 /random) * w)) % 360; // angle autour du tronc
//                float angle = (rand.nextFloat() * ((float) Math.PI * 2F))
//                        + (((float) (2 * Math.PI) / random) * w);
                    float spacing = ((float)(2 * Math.PI) / random) * w;
                    float angle = baseAngle + spacing;
                    int dy;
                    int dx;
                    int dz;

                    BlockPos branchIni = getClosestTrunkBlock(
                            new BlockPos[]{
                                    pos.above(y),
                                    pos.above(y).offset(1,0,0),
                                    pos.above(y).offset(1,0,1),
                                    pos.above(y).offset(0,0,1)
                            },
                            new BlockPos(Math.round(MathHelper.cos(angle) * length), hauteur, Math.round(MathHelper.sin(angle) * length))
                    );
                    BlockPos branchStart;
                    BlockPos branchEnd = null;

                    // 3 parties de la branche pour la curve
                    for (int tkt = 0; tkt < 3; tkt += 1) {
                        if (tkt == 0) {
                            branchStart = branchIni;
                            dy = (int) (hauteur / 5.5);
                            dx = (int) Math.round(MathHelper.cos(angle) * (length / 2.5));
                            dz = (int) Math.round(MathHelper.sin(angle) * (length / 2.5));
//                        Calculator.sendMsg("dx et dz et dy " + dx + "; " + dz + "; " + dy);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        } else if (tkt == 1) {
                            branchStart = branchEnd;
                            dy = ((hauteur / 3) * 2);
                            dx = (int) Math.round(MathHelper.cos(angle) * ((length / 7.0)) * 5);
                            dz = (int) Math.round(MathHelper.sin(angle) * ((length / 7.0)) * 5);
//                        Calculator.sendMsg("dx et dz et dy " + dx + "; " + dz + "; " + dy);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        } else {
                            branchStart = branchEnd;
                            dy = hauteur;
                            dx = Math.round(MathHelper.cos(angle) * length);
                            dz = Math.round(MathHelper.sin(angle) * length);
//                        Calculator.sendMsg("dx et dz et dy " + dx + "; " + dz + "; " + dy);
                            branchEnd = branchIni.offset(dx, dy, dz);
                        }
//                    Calculator.sendMsg("start= " + branchStart + "\nend= " + branchEnd + "\n");
                        placeBranch(world, rand, branchStart, branchEnd, foliagePlacerList);
                    }

                    // feuillage au bout
                    foliagePlacerList.add(new FoliagePlacer.Foliage(branchEnd, -1, false));
//                // dessine une petite branche qui monte
//                for (int i = 0; i < length; i++) {
//                    BlockPos branchPos = pos.above(dx, y + i / 2, dz);
//                    func_236911_a_(world, rand, branchPos, logBlock, 3);
//                }

                    // ajoute du feuillage au bout
//                foliageList.above(new FoliagePlacer.Foliage(pos.above(dx, y + length, dz), -2, false));
                }
            }

        }
//         3️⃣ Couronne de feuillage au sommet
        placeCanopy(world, rand, pos.above(height - 2),9, 4, 3);
//        foliagePlacerList.above(new FoliagePlacer.Foliage(pos.above(height), 2 + (height / 7), false));
        return foliagePlacerList;
    }

    private void placeBranch(ISeedReader world, Random rand, BlockPos start, BlockPos end, List<FoliagePlacer.Foliage> foliagePlacerList) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int dz = end.getZ() - start.getZ();

        int steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz)));

        int foliage = weightedListFoliage.get(rand.nextInt(weightedListFoliage.size()));
        int foliagePos = steps;
        if (steps > 2) {
            foliagePos = rand.nextInt(steps - 2) + 1;
        }
//        Calculator.sendMsg("posFoliage = " + foliagePos);
        boolean foliagePlaced = false;

        float xStep = dx / (float) steps;
        float yStep = dy / (float) steps;
        float zStep = dz / (float) steps;

        float x = start.getX();
        float y = start.getY();
        float z = start.getZ();

        BlockState logState = getLogState(config, rand, start, end);

        for (int i = 0; i <= steps; i++) {
            BlockPos pos = new BlockPos(Math.round(x), Math.round(y), Math.round(z));
            placeLog(world, pos, logState);

            // si un foliage doit être posé, qu'il n'est pas posé et que le block est la bonne pos
            if (!foliagePlaced && foliage == 1 && foliagePos == i) {
                int fx = Math.round(start.getX() + foliagePos * xStep);
                int fy = Math.round(start.getY() + foliagePos * yStep);
                int fz = Math.round(start.getZ() + foliagePos * zStep);
                BlockPos foliageBlock = new BlockPos(fx, fy, fz);
                foliagePlacerList.add(new FoliagePlacer.Foliage(foliageBlock, -4, false));
                foliagePlaced = true;
            }

            x += xStep;
            y += yStep;
            z += zStep;
        }
    }

    /**
     * Retourne le bloc parmi "logBlock" le plus proche de "target"
     */
    public static BlockPos getClosestTrunkBlock(BlockPos[] blocks, BlockPos target) {
        if (blocks == null || blocks.length == 0) return null;

        BlockPos closest = blocks[0];
        double minDistance = closest.distSqr(target);

        for (int i = 1; i < blocks.length; i++) {
            double dist = blocks[i].distSqr(target);
            if (dist < minDistance) {
                closest = blocks[i];
                minDistance = dist;
            }
        }
        return closest;
    }

    private void placeRoots(ISeedReader world, Random rand, BlockPos pos) {

        int random = weightedListRoots.get(rand.nextInt(weightedListRoots.size()));

        float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);

        for (int w = 0; w < random; w += 1) {

            float spacing = ((float)(2 * Math.PI) / random) * w;

            // Ajoute une variation aléatoire entre -12° et +12°
            float angleOffset = (rand.nextFloat() - 0.5f) * 0.4f;

            float angle = baseAngle + spacing + angleOffset;

            int length = 10 + rand.nextInt(4); // longueur de la racine

            BlockPos branchIni = getClosestTrunkBlock(
                    new BlockPos[]{
                            pos,
                            pos.offset(1,0,0),
                            pos.offset(1,0,1),
                            pos.offset(0,0,1)
                    },
                    new BlockPos(Math.round(MathHelper.sin(angle)) * length, pos.getY(), Math.round(MathHelper.sin(angle) * length))
            );
//
//            for (int s = 0; s < segments; s++) {
//                // Variation horizontale
//                int dx = Math.round(MathHelper.cos(angle) * (length / (float) segments));
//                int dz = Math.round(MathHelper.sin(angle) * (length / (float) segments));
//
//                // Variation verticale
//                int dy;
//                BlockPos below = branchIni.down();
//                if (world.hasBlockState(below, state -> state.getMaterial().isSolid())) {
//                    // Sol en dessous : descend légèrement pour s'enfoncer
//                    dy = -1;
//                } else {
//                    // Vide ou sol plus bas : descend plus ou monte légèrement
//                    dy = rand.nextBoolean() ? -1 : 0;
//                }
//            
//                BlockPos nextPos = branchIni.above(dx, dy, dz);
//                Calculator.sendMsg("nextpos = " + nextPos.toString());

            int dy = 0;
            int dx = (int) Math.round(MathHelper.cos(angle) * (length / 3.0) * 2);
            int dz = (int) Math.round(MathHelper.sin(angle) * (length / 3.0) * 2);
            BlockPos branchEnd = branchIni.offset(dx, dy, dz);

//            Calculator.sendMsg("start= " + branchIni + "\nend= " + branchEnd + "\n");
            // Placer les blocs de la racine entre currentPos et nextPos
            BlockPos branchStart = placeRootSegment(world, rand, branchIni, branchEnd, false);

            dy = -3;
            dx = (int) Math.round(MathHelper.cos(angle) * (length / 3.0));
            dz = (int) Math.round(MathHelper.sin(angle) * (length / 3.0));
            branchEnd = branchStart.offset(dx, dy, dz);

//            Calculator.sendMsg("start= " + branchIni + "\nend= " + branchEnd + "\n");
            placeRootSegment(world, rand, branchStart, branchEnd, true);
        }
    }

    private BlockPos placeRootSegment(ISeedReader world, Random rand, BlockPos start,
                                      BlockPos end, boolean canGoUnderground) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int dz = end.getZ() - start.getZ();

        int steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz)));
        if (steps == 0) return start;

        float xStep = dx / (float) steps;
        float yStep = dy / (float) steps;
        float zStep = dz / (float) steps;

        float x = start.getX();
        float y = start.getY();
        float z = start.getZ();

        boolean randomGoDown = true;
        int rootGoDownNumber = 0;
        if (randomGoDown) {
            rootGoDownNumber = weightedListRootsGoDown.get(rand.nextInt(weightedListRootsGoDown.size()));
        }
        boolean needGoDown = false;
        int randomNumberForGoDown = rand.nextInt(3);

        float angle = (float) Math.atan2(dz, dx); // angle de départ en radians
        float curve = (rand.nextBoolean() ? 1 : -1) * 0.4f;
        // courbure aléatoire : positif = tourne à droite, négatif = à gauche
        BlockPos pos = null;

        for (int i = 0; i <= steps; i++) {
            // true avec une chance sur 3
            if (!needGoDown && i > 3 && randomNumberForGoDown == 0) {
                needGoDown = true;
            }

            float curAngle = angle + curve * (i / (float) steps);

            int px = Math.toIntExact(Math.round(start.getX() + i * Math.cos(curAngle)));
            int py = Math.round(start.getY() + i * yStep);
            int pz = Math.toIntExact(Math.round(start.getZ() + i * Math.sin(curAngle)));


//            BlockPos pos = new BlockPos(Math.round(x), Math.round(y), Math.round(z));
            pos = new BlockPos(px, py, pz);
            if (!canGoUnderground && !isReplaceableAtCustom(world, pos)) {
                pos = moveUpToSurface(world, pos);
            }
            // si true et encore des blocks à descendre, descendre
            if (needGoDown && rootGoDownNumber > 0) {
                pos = pos.below();
                rootGoDownNumber--;
//                Calculator.sendMsg("OMG DOWN = " + pos);
            }

            BlockState logState = getLogState(config, rand, start, end);
            placeLog(world, pos, logState);

            x += xStep;
            y += yStep;
            z += zStep;
        }
        return pos;
    }

    /**
     * Retourne le BlockState d'une bûche orientée selon la direction start → end
     */
    public static BlockState getLogState(BaseTreeFeatureConfig config, Random rand,
                                         BlockPos start, BlockPos end) {
        BlockState logState = config.trunkProvider.getBlockState(rand, start);

        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();

        Direction.Axis axis;
        if (Math.abs(dx) > Math.abs(dz)) axis = Direction.Axis.X;  // horizontale selon X
        else if (Math.abs(dz) > Math.abs(dx)) axis = Direction.Axis.Z; // horizontale selon Z
        else axis = Direction.Axis.Y; // vertical par défaut

        return logState.with(RotatedPillarBlock.AXIS, axis);
    }

    private void placeCanopy(IWorldGenerationReader world, Random rand, BlockPos start, int radius, int height,
                             MutableBoundingBox box,
                             BaseTreeFeatureConfig config) {
        List<BlockPos> positions = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            // Calcul du rayon actuel à ce niveau
            double currentRadius = radius * Math.cos((Math.PI/2) * (y / (double)height));

            int intRadius = (int)Math.ceil(currentRadius);

            for (int dx = -intRadius; dx <= intRadius; dx++) {
                for (int dz = -intRadius; dz <= intRadius; dz++) {
                    // Vérifie si le point est dans le cercle
                    if (dx * dx + dz * dz <= currentRadius * currentRadius) {
                        BlockPos pos = start.offset(dx, y, dz);
                        positions.add(pos);
                    }
                }
            }
        }
        for (BlockPos pos : positions) {
            // place une feuille si l'endroit est libre
            if (isAirOrLeaves(world, pos)) {
                func_236913_a_(world, pos, config.leavesProvider.getBlockState(rand, pos), box);
            }
        }
    }

    private void placeLog(ISeedReader world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 3);
        trunkBlocks.add(pos.immutable());
    }

    public static boolean isReplaceableAtCustom(IWorldGenerationBaseReader reader, BlockPos pos) {
        return isAirOrLeaves(reader, pos) || isPlantAt(reader, pos) || isWaterAt(reader, pos) || isLogAt(reader, pos);
    }

    private static boolean isLogAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, (state) -> state.getBlock() == Blocks.JUNGLE_LOG);
    }

    private static boolean isPlantAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, (state) -> {
            Material material = state.getMaterial();
            return material == Material.PLANT;
        });
    }

    private static boolean isWaterAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, (state) -> state.is(Blocks.WATER));
    }

    public static BlockPos moveUpToSurface(IWorldGenerationReader world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutable();

        // climb up until we find air
        while (!world.isStateAtPosition(mutable, BlockState::isAir)) {
            mutable.move(Direction.UP);
        }

        return mutable.immutable();
    }
}