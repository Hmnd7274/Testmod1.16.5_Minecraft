package net.hamnd.testmod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.JungleFoliagePlacer;
import net.minecraft.world.gen.treedecorator.LeaveVineTreeDecorator;
import net.minecraft.world.gen.treedecorator.TrunkVineTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.*;

import static net.minecraft.world.gen.feature.TreeFeature.isAirOrLeaves;

public class SpiritTreeSpawn{
    private static final List<Integer> littleBranchWL = Arrays.asList(3,4,4,4);
    private static final List<Integer> mediumBranchWL = Arrays.asList(1,2,2,3,3,3);
    private static final List<Integer> largeBranchWL = Arrays.asList(1,1,1,1,2,2,3);
    private static final List<Integer> weightedListRoots = Arrays.asList(3,3,4,4,5);
    private static final List<Integer> weightedListRootsGoDown = Arrays.asList(3,4,4,5);
    
    private static final List<BlockPos> trunkOffsets = Arrays.asList(
            new BlockPos(0, 0, 0),
            new BlockPos(1,0,0),
            new BlockPos(1,0,1),
            new BlockPos(0,0,1)
    );
    
//    public static final BlockState logState = ModBlocks.SPIRIT_LOG.get().defaultBlockState();
//    public static final BlockState leaveState = ModBlocks.SPIRIT_LEAVES.get().defaultBlockState();

//    private static List<BlockPos> trunkBlocks = Collections.emptyList();
//    private static List<BlockPos> trunkLeaves = Collections.emptyList();

    private static BlockState getLogState() {
        return ModBlocks.SPIRIT_LOG.get().defaultBlockState();
    }

    private static BlockState getLeaveState() {
        return ModBlocks.SPIRIT_LEAVES.get().defaultBlockState();
    }

    public static void spawnTree(ISeedReader world, Random rand,
                                 MutableBoundingBox box,
                                 BlockPos pos, int treeHeight) {
//        int treeHeight = 14;
        int foliageHeight = 3;
        Set<BlockPos> vineSetFoliage = Sets.newHashSet(); // placeLeavesRow() -> tous les blocs de feuilles
        Set<BlockPos> vineSetTrunk = Sets.newHashSet(); // placeTrunk() -> tous les blocs de log
        BlobFoliagePlacer myBlobFolPlacer = new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), foliageHeight);
        BaseTreeFeatureConfig baseTreeFeatureConfig = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(getLogState()),
                new SimpleBlockStateProvider(getLeaveState()),
                new JungleFoliagePlacer(
                        FeatureSpread.fixed(3),
                        FeatureSpread.fixed(0),
                        0
                ),
                new StraightTrunkPlacer(4, 8, 0),
                new TwoLayerFeature(1, 1, 2)
        ).decorators(ImmutableList.of(TrunkVineTreeDecorator.INSTANCE, LeaveVineTreeDecorator.INSTANCE)).build();
        
        // Place tronc, branches, racines, et récupère les foliage positions
        getFoliages(world, rand, pos, vineSetTrunk, treeHeight)
                .forEach((foliage) -> {
                    myBlobFolPlacer.createFoliage(world, rand, baseTreeFeatureConfig, treeHeight, foliage,
                            myBlobFolPlacer.foliageRadius(rand, 0), foliageHeight, vineSetFoliage, box); // le 0 du foliage radius sert à rien il n'est pas utilisé par blob...
        });
        
        // Place les vignes
        baseTreeFeatureConfig.decorators.forEach((treeDecorator) -> {
            treeDecorator.place(world, rand, Lists.newArrayList(vineSetTrunk), Lists.newArrayList(vineSetFoliage), new HashSet<>(), box);
        });
        TestMod.LOGGER.info("[TestMod] SpiritTree generated at: {}; {}; {}", pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static List<JungleFoliagePlacer.Foliage> getFoliages(ISeedReader world, Random rand, BlockPos pos,
                                                                Set<BlockPos> vineSetTrunk, int treeHeight) {
        
        List<JungleFoliagePlacer.Foliage> foliagePlacerList = Lists.newArrayList();

        // 1️⃣ Générer le tronc principal
        for (int y = 0; y < treeHeight; y++) {
            BlockPos logPos = pos.above(y);
            placeLog(world, logPos, getLogState(), vineSetTrunk); // pose un bloc de bois
            placeLog(world, logPos.offset(1,0,0), getLogState(), vineSetTrunk); // pose un bloc de bois
            placeLog(world, logPos.offset(1,0,1), getLogState(), vineSetTrunk); // pose un bloc de bois
            placeLog(world, logPos.offset(0,0,1), getLogState(), vineSetTrunk); // pose un bloc de bois
        }

//        // Générer les raçines
//        placeRoots(world, rand, pos, vineSetTrunk);

        // 2️⃣ Générer des petites branches qui montent
        placeBranches(world, rand, pos, vineSetTrunk, treeHeight, foliagePlacerList);
        
////         3️⃣ Couronne de feuillage au sommet
//        placeCanopy(world, pos.above(height - 2),9, 4);
//        foliagePlacerList.above(new JungleFoliagePlacer.Foliage(pos.above(height), 2 + (height / 7), false));
        return foliagePlacerList;
    }

    private static void placeRoots(ISeedReader world, Random rand, BlockPos pos, Set<BlockPos> vineSetTrunk) {

        int random = weightedListRoots.get(rand.nextInt(weightedListRoots.size()));

        float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);

        for (int w = 0; w < random; w += 1) {

            float spacing = ((float)(2 * Math.PI) / random) * w;

            // Ajoute une variation aléatoire entre -12° et +12°
            float angleOffset = (rand.nextFloat() - 0.5f) * 0.4f;

            float angle = baseAngle + spacing + angleOffset;

            int length = 10 + rand.nextInt(4); // longueur de la racine

            BlockPos branchIni = getClosestTrunkBlock(length, pos.getY(), angle, pos);

            int dy = 0;
            int dx = (int) Math.round(MathHelper.cos(angle) * (length / 3.0) * 2);
            int dz = (int) Math.round(MathHelper.sin(angle) * (length / 3.0) * 2);
            BlockPos branchEnd = branchIni.offset(dx, dy, dz);

//            Calculator.sendMsg("start= " + branchIni + "\nend= " + branchEnd + "\n");
            // Placer les blocs de la racine entre currentPos et nextPos
            BlockPos branchStart = placeRootSegment(world, rand, branchIni, branchEnd, false, vineSetTrunk);

            dy = -3;
            dx = (int) Math.round(MathHelper.cos(angle) * (length / 3.0));
            dz = (int) Math.round(MathHelper.sin(angle) * (length / 3.0));
            branchEnd = branchStart.offset(dx, dy, dz);

//            Calculator.sendMsg("start= " + branchIni + "\nend= " + branchEnd + "\n");
            placeRootSegment(world, rand, branchStart, branchEnd, true, vineSetTrunk);
        }
    }

    private static BlockPos placeRootSegment(ISeedReader world, Random rand, BlockPos start,
                                             BlockPos end, boolean canGoUnderground, Set<BlockPos> vineSetTrunk) {
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

            BlockState rotatedLogState = getLogState(start, end);
            placeLog(world, pos, rotatedLogState, vineSetTrunk);

            x += xStep;
            y += yStep;
            z += zStep;
        }
        return pos;
    }
    
    private static void placeBranches(ISeedReader world, Random rand, BlockPos treeInitPos, Set<BlockPos> vineSetTrunk,
                                      int treeHeight, List<JungleFoliagePlacer.Foliage> foliagePlacerList) {
        int minHeight = 3;
        int maxHeight = treeHeight - 1;
        int yAvailable = treeHeight - minHeight;
        for (int startBranchY = minHeight; startBranchY < maxHeight; startBranchY += 2) {
            // Petite branche en haut
            if (startBranchY - minHeight >= (yAvailable / 10) * 8) {
                int length = 3 + rand.nextInt(2);
                int numberOfBranches = littleBranchWL.get(rand.nextInt(littleBranchWL.size()));
                int DY = treeHeight - startBranchY;
                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
                
                for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
                    foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList),
                            -2,
                            false
                    ));
                }
            }
            // Moyenne branche au milieu
            if (startBranchY - minHeight >= (yAvailable / 10) * 4) {
                int length = 5 + rand.nextInt(3);
                int numberOfBranches = mediumBranchWL.get(rand.nextInt(mediumBranchWL.size()));
                int DY = treeHeight - startBranchY - 1 - rand.nextInt(2);
                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);

                for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
                    foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, startBranchY, DY, treeInitPos, treeHeight, foliagePlacerList),
                            -1,
                            false
                    ));
                }
            }
            // Grande branche en bas
            else {
                int length = 8 + rand.nextInt(2);
                int numberOfBranches = largeBranchWL.get(rand.nextInt(largeBranchWL.size()));
                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
                int DY = treeHeight - startBranchY - 2 - rand.nextInt(2);

                for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
                    foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList),
                            -1,
                            false
                    ));
                }
            }
        }
    }
    
    private static BlockPos placeOneBranch(ISeedReader world, Random rand, Set<BlockPos> vineSetTrunk,
                                       int numberOfBranches, int branchNum, int length, float baseAngle, int DY, int startBranchY,
                                       BlockPos treeInitPos, int treeHeight, List<JungleFoliagePlacer.Foliage> foliagePlacerList) {
        // Build les pos de fin et début
        float spacing = ((float)(2 * Math.PI) / numberOfBranches) * branchNum;
        float angle = baseAngle + spacing;

        BlockPos branchStart = getClosestTrunkBlock(length, startBranchY, angle, treeInitPos);
        BlockPos branchEnd = treeInitPos.offset(
                (MathHelper.cos(angle) * length),
                startBranchY + DY,
                (MathHelper.sin(angle) * length)
        );
        
        // Build la list des offsets
        int DXtot = branchEnd.getX() - branchStart.getX();
        int DYtot = branchEnd.getY() - branchStart.getY();
        int DZtot = branchEnd.getZ() - branchStart.getZ();

        List<Float> branchPercentagesXZ = Arrays.asList(0.4f, 0.8f, 1f);
        List<Float> branchPercentagesY = Arrays.asList(0.2f, 0.7f, 1f);
        
        List<BlockPos> branchOffsets = new ArrayList<>(Arrays.asList(new BlockPos(0, 0, 0)));
        for (int i = 0; i<3; i++) {
            BlockPos newOffset = new BlockPos(
                    DXtot * branchPercentagesXZ.get(i),
                    DYtot * branchPercentagesY.get(i),
                    DZtot * branchPercentagesXZ.get(i)
            );
            branchOffsets.add(newOffset);
        }

        // Place chaque segment
        for (int branchPartNum = 0; branchPartNum < 3; branchPartNum++) {
            BlockPos branchSeg1 = branchStart.offset(branchOffsets.get(branchPartNum));
            TestMod.LOGGER.info("[TestMod] partnum: {}, world y {}", branchPartNum, branchSeg1.getY());
            BlockPos branchSeg2 = branchStart.offset(branchOffsets.get(branchPartNum + 1));

            placeBranchSegment(world, rand, branchSeg1, branchSeg2, foliagePlacerList, vineSetTrunk);
        }
        
        return branchEnd;
    }


    private static void placeBranchSegment(ISeedReader world, Random rand, BlockPos start, BlockPos end,
                                    List<JungleFoliagePlacer.Foliage> foliagePlacerList, Set<BlockPos> vineSetTrunk) {
        int dx = end.getX() - start.getX();
        int dy = end.getY() - start.getY();
        int dz = end.getZ() - start.getZ();

        int steps = Math.max(Math.abs(dx), Math.max(Math.abs(dy), Math.abs(dz)));

        float xStep = dx / (float) steps;
        float yStep = dy / (float) steps;
        float zStep = dz / (float) steps;

        int foliageStepNum = -1;
        boolean foliagePlaced = false;
        
        if (steps > 5) {
            foliageStepNum = rand.nextInt(steps - 3) + 2;
        }
        
        float x = start.getX();
        float y = start.getY();
        float z = start.getZ();

        BlockState rotatedLogState = getLogState(start, end);

        for (int i = 0; i < steps; i++) {
            BlockPos pos = new BlockPos(Math.round(x), Math.round(y), Math.round(z));
            placeLog(world, pos, rotatedLogState, vineSetTrunk);

            // si un foliage doit être posé, qu'il n'est pas posé et que le block est la bonne pos
            if (!foliagePlaced && foliageStepNum != -1 && foliageStepNum == i) {
                foliagePlacerList.add(new JungleFoliagePlacer.Foliage(pos, -3, false));
                foliagePlaced = true;
            }

            x += xStep;
            y += yStep;
            z += zStep;
        }
    }
    
    private static void placeCanopy(ISeedReader world, BlockPos start, int radius, int height) {
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
                placeLeave(world, pos, getLeaveState());
            }
        }
    }
    
    public static BlockPos getClosestTrunkBlock(int length, int height, float angle, BlockPos treeInitPos) {
        BlockPos closestOffset = new BlockPos(0, 0, 0);
        
        BlockPos target = new BlockPos(
                length * Math.round(MathHelper.cos(angle)),
                0,
                length * Math.round(MathHelper.sin(angle))
        );
        double minDistance = trunkOffsets.get(0).distSqr(target);
        
        for (int i = 1; i < trunkOffsets.size(); i++) {
            double dist = trunkOffsets.get(i).distSqr(target);
            if (dist < minDistance) {
                closestOffset = trunkOffsets.get(i);
                minDistance = dist;
            }
        }
        
        return treeInitPos.offset(closestOffset.above(height));
    }

    /**
     * Retourne le BlockState d'une bûche orientée selon la direction start → end
     */
    public static BlockState getLogState(BlockPos start, BlockPos end) {
        int dx = end.getX() - start.getX();
        int dz = end.getZ() - start.getZ();

        Direction.Axis axis;
        if (Math.abs(dx) > Math.abs(dz)) axis = Direction.Axis.X;  // horizontale selon X
        else if (Math.abs(dz) > Math.abs(dx)) axis = Direction.Axis.Z; // horizontale selon Z
        else axis = Direction.Axis.Y; // vertical par défaut

        return getLogState().setValue(RotatedPillarBlock.AXIS, axis);
    }


    private static void placeLog(ISeedReader world, BlockPos pos, BlockState state, Set<BlockPos> vineSetTrunk) {
        world.setBlock(pos, state, 3);
        vineSetTrunk.add(pos);
    }

    private static void placeLeave(ISeedReader world, BlockPos pos, BlockState state) {
        world.setBlock(pos, state, 3);
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