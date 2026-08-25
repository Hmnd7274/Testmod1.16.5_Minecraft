package net.hamnd.testmod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.EnumProperty;
import net.minecraft.util.Direction;
import net.minecraft.util.Tuple;
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
    private static final List<Integer> littleBranchWL = Arrays.asList(3,4,4,4,5,5);
    private static final List<Integer> mediumBranchWL = Arrays.asList(2,2,3,3,3);
    private static final List<Integer> largeBranchWL = Arrays.asList(1,1,1,2,2,2,3);
    private static final List<Integer> weightedListRoots = Arrays.asList(3,3,4,4,5);
    private static final List<Integer> weightedListRootsGoDown = Arrays.asList(3,4,4,5);
    
    private static final List<BlockPos> trunkOffsets = Arrays.asList(
            new BlockPos(0,0,0),
            new BlockPos(0,0,1),
            new BlockPos(0,0,-1),
            
            new BlockPos(1,0,0),
            new BlockPos(1,0,1),
            new BlockPos(1,0,-1),
            
            new BlockPos(-1,0,0),
            new BlockPos(-1,0,1),
            new BlockPos(-1,0,-1)
    );

    private static int nextId = 0;
    private static int getNewTreeDataIndex() {
        nextId++;
        return nextId;
    }
//    public static final BlockState logState = ModBlocks.SPIRIT_LOG.get().defaultBlockState();
//    public static final BlockState leaveState = ModBlocks.SPIRIT_LEAVES.get().defaultBlockState();

//    private static List<BlockPos> trunkBlocks = Collections.emptyList();
//    private static List<BlockPos> trunkLeaves = Collections.emptyList();

    private static BlockState getLogState(SpiritualLogBlock.TreeParts part) {
        return ModBlocks.SPIRIT_LOG.get().defaultBlockState().setValue(SpiritualLogBlock.PART, part);
    }

    private static BlockState getLeaveState() {
        return ModBlocks.SPIRIT_LEAVES.get().defaultBlockState();
    }

    public static void spawnTree(ISeedReader world, Random rand,
                                 MutableBoundingBox box,
                                 BlockPos pos, int treeHeight, int branch) {
//        int treeHeight = 14;
        int foliageHeight = 3;
        Set<BlockPos> vineSetFoliage = Sets.newHashSet(); // placeLeavesRow() -> tous les blocs de feuilles
        Set<BlockPos> vineSetTrunk = Sets.newHashSet(); // placeTrunk() -> tous les blocs de log
        BlobFoliagePlacer myBlobFolPlacer = new BlobFoliagePlacer(FeatureSpread.fixed(2), FeatureSpread.fixed(0), foliageHeight);
        BaseTreeFeatureConfig baseTreeFeatureConfig = new BaseTreeFeatureConfig.Builder(
                new SimpleBlockStateProvider(getLogState(SpiritualLogBlock.TreeParts.UK)),
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
        getFoliages(world, rand, pos, vineSetTrunk, treeHeight, branch)
                .forEach((foliage) -> {
                    myBlobFolPlacer.createFoliage(world, rand, baseTreeFeatureConfig, treeHeight, foliage,
                            myBlobFolPlacer.foliageRadius(rand, 0), foliageHeight, vineSetFoliage, box); // le 0 du foliage radius sert à rien il n'est pas utilisé par blob...
        });
        
        // Place les vignes
        baseTreeFeatureConfig.decorators.forEach((treeDecorator) -> {
            treeDecorator.place(world, rand, Lists.newArrayList(vineSetTrunk), Lists.newArrayList(vineSetFoliage), new HashSet<>(), box);
        });
        TestMod.LOGGER.info("[TestMod] SpiritTree generated at: {}; {}; {}", pos.getX(), pos.getY(), pos.getZ());
        Set<Tuple<BlockPos, SpiritualLogBlock.TreeParts>> setLogs = Sets.newHashSet();
        vineSetTrunk.forEach(position -> setLogs.add(new Tuple<>(position, world.getBlockState(position).getValue(SpiritualLogBlock.PART))));
        CutsceneHandler.testdebugtreepos = setLogs;
    }
    
    public static List<JungleFoliagePlacer.Foliage> getFoliages(ISeedReader world, Random rand, BlockPos pos,
                                                                Set<BlockPos> vineSetTrunk, int treeHeight, int branch) {
        
        List<JungleFoliagePlacer.Foliage> foliagePlacerList = Lists.newArrayList();
        
        TreeData treeData = new TreeData();
        int treeDataId = getNewTreeDataIndex();
        
        // 1️⃣ Générer le tronc principal
        for (int y = 0; y < treeHeight; y++) {
            BlockPos logPos = pos.above(y);
            
            treeData.remainingPerLayer.put(logPos.getY(), 9);
            
            for (BlockPos trunkOffset : trunkOffsets) {
                BlockPos newPos = logPos.offset(trunkOffset);
                placeLog(world, newPos, getLogState(SpiritualLogBlock.TreeParts.TRUNK), vineSetTrunk); // pose un bloc de bois
                ((SpiritualLogBlock.SpiritualLogTileEntity) world.getBlockEntity(newPos)).setTreeId(treeDataId);
            }
            TreeSaveData.get(world.getLevel()).registerNewTree(treeDataId, treeData);
        }

        // Générer les raçines
        placeRoots(world, rand, pos, vineSetTrunk);

        // 2️⃣ Générer des petites branches qui montent
        placeBranches(branch, world, rand, pos, vineSetTrunk, treeHeight, foliagePlacerList);
        
        // Couronne de feuillage au sommet
//        placeCanopy(world, pos.above(height - 2),9, 4);
        foliagePlacerList.add(new JungleFoliagePlacer.Foliage(pos.above(treeHeight+1), 3, false));
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

            BlockPos branchIni = getClosestTrunkBlock(length, 0, angle, pos);

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
                pos = moveToSurface(world, pos);
            }
            // si true et encore des blocks à descendre, descendre
            if (needGoDown && rootGoDownNumber > 0) {
                pos = pos.below();
                rootGoDownNumber--;
//                Calculator.sendMsg("OMG DOWN = " + pos);
            }

            BlockState rotatedLogState = getLogState(start, end, SpiritualLogBlock.TreeParts.ROOT);
            placeLog(world, pos, rotatedLogState, vineSetTrunk);

            x += xStep;
            y += yStep;
            z += zStep;
        }
        return pos;
    }
    
    private static void placeBranches(int branch, ISeedReader world, Random rand, BlockPos treeInitPos, Set<BlockPos> vineSetTrunk,
                                      int treeHeight, List<JungleFoliagePlacer.Foliage> foliagePlacerList) {
        int minHeight = 4;
        int yAvailable = treeHeight - minHeight;
        int bottomY = ((yAvailable * 4) / 10);
        int middleY = ((yAvailable * 8) / 10);
        int topY = yAvailable;
        if (bottomY % 2 == 1)
            bottomY--;
        if (middleY % 2 == 1)
            middleY--;
        if (topY % 2 == 1)
            topY--;
        // faut encore vérifier si un étage commence bien 2 blocs au dessus de celui du dessous
        
        switch (branch) {
            case 4:
                placeBottomBranches(world, rand, treeInitPos, vineSetTrunk, foliagePlacerList,
                        treeHeight, minHeight, minHeight + bottomY);
                placeMiddleBranches(world, rand, treeInitPos, vineSetTrunk, foliagePlacerList,
                        treeHeight, minHeight + bottomY, minHeight + middleY);
                placeTopBranches(world, rand, treeInitPos, vineSetTrunk, foliagePlacerList,
                        treeHeight, minHeight + middleY, minHeight + topY);
                break;
            case 3:
                placeTopBranches(world, rand, treeInitPos, vineSetTrunk, foliagePlacerList,
                        treeHeight, minHeight + middleY, minHeight + topY);
                break;
            case 2:
                placeMiddleBranches(world, rand, treeInitPos, vineSetTrunk, foliagePlacerList,
                        treeHeight, minHeight + bottomY, minHeight + middleY);
                break;
            case 1:
                placeBottomBranches(world, rand, treeInitPos, vineSetTrunk, foliagePlacerList,
                        treeHeight, minHeight, minHeight + bottomY);
                break;
        }
        
//        int minHeight = 3;
//        int maxHeight = treeHeight - 1;
//        int yAvailable = treeHeight - minHeight;
//        for (int startBranchY = minHeight; startBranchY < maxHeight; startBranchY += 2) {
//            // Petite branche en haut
//            if (startBranchY - minHeight >= (yAvailable / 10) * 8) {
//                if (!(branch == 3 || branch == 4)) return;
//                int length = 3 + rand.nextInt(2);
//                int numberOfBranches = littleBranchWL.get(rand.nextInt(littleBranchWL.size()));
//                int DY = treeHeight - startBranchY;
//                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
//                
//                for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
////                    foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
////                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList),
////                            -1,
////                            false
////                    ));
//                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList);
//                }
//            }
//            // Moyenne branche au milieu
//            else if (startBranchY - minHeight >= (yAvailable / 10) * 4) {
//                if (!(branch == 2 || branch == 4)) return;
//                int length = 5 + rand.nextInt(3);
//                int numberOfBranches = mediumBranchWL.get(rand.nextInt(mediumBranchWL.size()));
//                int DY = treeHeight - startBranchY - 1 - rand.nextInt(2);
//                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
//
//                for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
////                    foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
////                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList),
////                            0,
////                            false
////                    ));
//                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList);
//                }
//            }
//            // Grande branche en bas
//            else {
//                if (!(branch == 1 || branch == 4)) return;
//                int length = 8 + rand.nextInt(2);
//                int numberOfBranches = largeBranchWL.get(rand.nextInt(largeBranchWL.size()));
//                float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
//                int DY = treeHeight - startBranchY - 2 - rand.nextInt(2);
//
//                for (int branchNumber = 0; branchNumber < numberOfBranches; branchNumber++) {
////                    foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
////                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList),
////                            0,
////                            false
////                    ));
//                            placeOneBranch(world, rand, vineSetTrunk, numberOfBranches, branchNumber, length, baseAngle, DY, startBranchY, treeInitPos, treeHeight, foliagePlacerList);
//                }
//                return;
//            }
//        }
    }
    
    private static void placeBottomBranches(ISeedReader world, Random rand, BlockPos treeInitPos, Set<BlockPos> vineSetTrunk,
                                            List<JungleFoliagePlacer.Foliage> foliagePlacerList, int treeHeight, int minHeight, int maxHeight) {
        // Petite branche en haut
        float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
        float lastSpacing = 0;
        for (int startBranchY = minHeight; startBranchY < maxHeight; startBranchY += 2) {

            int length = 9 + rand.nextInt(5);
            int numberOfBranches = largeBranchWL.get(rand.nextInt(largeBranchWL.size()));
            int DY = treeHeight - startBranchY - 2 - rand.nextInt(2);
            baseAngle = baseAngle + (lastSpacing / 2);
            float spacing = (float)(2F * Math.PI) / numberOfBranches;
            
            for (int branchNum = 0; branchNum < numberOfBranches; branchNum++) {
                float angle = baseAngle + spacing * branchNum;
                   foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
                           placeOneBranch(world, rand, vineSetTrunk, foliagePlacerList, length, angle, DY, startBranchY, treeInitPos),
                           0,
                           false
                   ));
//                placeOneBranch(world, rand, vineSetTrunk, foliagePlacerList, length, angle, DY, startBranchY, treeInitPos);
            }
            lastSpacing = spacing;
        }
    }

    private static void placeMiddleBranches(ISeedReader world, Random rand, BlockPos treeInitPos, Set<BlockPos> vineSetTrunk,
                                            List<JungleFoliagePlacer.Foliage> foliagePlacerList, int treeHeight, int minHeight, int maxHeight) {
        // Petite branche en haut
        float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
        float lastSpacing = 0;
        for (int startBranchY = minHeight; startBranchY < maxHeight; startBranchY += 2) {

            int length = 6 + rand.nextInt(4);
            int numberOfBranches = mediumBranchWL.get(rand.nextInt(mediumBranchWL.size()));
            int DY = treeHeight - startBranchY - 1 - rand.nextInt(2);
            baseAngle = baseAngle + (lastSpacing / 2);
            float spacing = (float)(2F * Math.PI) / numberOfBranches;

            for (int branchNum = 0; branchNum < numberOfBranches; branchNum++) {
                float angle = baseAngle + spacing * branchNum;
                   foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
                           placeOneBranch(world, rand, vineSetTrunk, foliagePlacerList, length, angle, DY, startBranchY, treeInitPos),
                           0,
                           false
                   ));
////                placeOneBranch(world, rand, vineSetTrunk, foliagePlacerList, length, angle, DY, startBranchY, treeInitPos);
            }
            lastSpacing = spacing;
        }
    }

    private static void placeTopBranches(ISeedReader world, Random rand, BlockPos treeInitPos, Set<BlockPos> vineSetTrunk,
                                            List<JungleFoliagePlacer.Foliage> foliagePlacerList, int treeHeight, int minHeight, int maxHeight) {
        // Petite branche en haut
        float baseAngle = rand.nextFloat() * ((float) Math.PI * 2F);
        float lastSpacing = 0;
        for (int startBranchY = minHeight; startBranchY < maxHeight; startBranchY += 2) {

            int length = 5 + rand.nextInt(2);
            int numberOfBranches = littleBranchWL.get(rand.nextInt(littleBranchWL.size()));
            int DY = treeHeight - startBranchY;
            baseAngle = baseAngle + (lastSpacing / 2);
            float spacing = (float)(2F * Math.PI) / numberOfBranches;

            for (int branchNum = 0; branchNum < numberOfBranches; branchNum++) {
                float angle = baseAngle + spacing * branchNum;
                   foliagePlacerList.add(new JungleFoliagePlacer.Foliage(
                           placeOneBranch(world, rand, vineSetTrunk, foliagePlacerList, length, angle, DY, startBranchY, treeInitPos),
                           0,
                           false
                   ));
//                placeOneBranch(world, rand, vineSetTrunk, foliagePlacerList, length, angle, DY, startBranchY, treeInitPos);
            }
            lastSpacing = spacing;
        }
    }
    
    private static BlockPos placeOneBranch(ISeedReader world, Random rand, Set<BlockPos> vineSetTrunk,
                                           List<JungleFoliagePlacer.Foliage> foliagePlacerList,
                                           int length, float angle, int DY, int startBranchY, BlockPos treeInitPos) {
        // Build les pos de fin et début
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
        BlockState lastRotatedBlockState = null;
        
        for (int branchPartNum = 0; branchPartNum < 3; branchPartNum++) {
            BlockPos branchSeg1 = branchStart.offset(branchOffsets.get(branchPartNum));
            TestMod.LOGGER.info("[TestMod] partnum: {}, world y {}", branchPartNum, branchSeg1.getY());
            BlockPos branchSeg2 = branchStart.offset(branchOffsets.get(branchPartNum + 1));

            lastRotatedBlockState = placeBranchSegment(world, rand, branchSeg1, branchSeg2, foliagePlacerList, vineSetTrunk, lastRotatedBlockState);
        }
        
        return branchEnd;
    }


    private static BlockState placeBranchSegment(ISeedReader world, Random rand, BlockPos start, BlockPos end,
                                    List<JungleFoliagePlacer.Foliage> foliagePlacerList, Set<BlockPos> vineSetTrunk,
                                                 BlockState lastRotatedBlockState) {
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

        BlockState rotatedLogState = getLogState(start, end, SpiritualLogBlock.TreeParts.BRANCH);

        for (int i = 0; i < steps; i++) {
            BlockPos pos = new BlockPos(Math.round(x), Math.round(y), Math.round(z));
            
            if (i == 0 && lastRotatedBlockState != null) {
                placeLog(world, pos, lastRotatedBlockState, vineSetTrunk);
            }
            else {
                placeLog(world, pos, rotatedLogState, vineSetTrunk);
            }

            // si un foliage doit être posé, qu'il n'est pas posé et que le block est la bonne pos
            if (!foliagePlaced && foliageStepNum != -1 && foliageStepNum == i) {
                foliagePlacerList.add(new BlobFoliagePlacer.Foliage(pos, -3, false));
                foliagePlaced = true;
            }

            x += xStep;
            y += yStep;
            z += zStep;
        }
        return rotatedLogState;
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
                Math.round(MathHelper.cos(angle) * length),
                0,
                Math.round(MathHelper.sin(angle) * length)
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
    public static BlockState getLogState(BlockPos start, BlockPos end, SpiritualLogBlock.TreeParts part) {
        int dx = MathHelper.abs(end.getX() - start.getX());
        int dy = MathHelper.abs(end.getY() - start.getY());
        int dz = MathHelper.abs(end.getZ() - start.getZ());

        int max = Collections.max(Arrays.asList(dx, dy, dz));
        
        Direction.Axis axis = Direction.Axis.Z;  // horizontale selon Z cas de base
        if (max == dx) axis = Direction.Axis.X;  // horizontale selon X
        if (max == dy) axis = Direction.Axis.Y;  // horizontale selon Y

        return getLogState(part).setValue(RotatedPillarBlock.AXIS, axis);
    }


    private static void placeLog(ISeedReader world, BlockPos pos, BlockState state, Set<BlockPos> vineSetTrunk) {
        if (world.getBlockState(pos).getBlock() != ModBlocks.SPIRIT_LOG.get()) {
            world.setBlock(pos, state, 3);
            vineSetTrunk.add(pos);
        }
    }

    private static void placeLeave(ISeedReader world, BlockPos pos, BlockState state) {
        if (world.getBlockState(pos) != ModBlocks.SPIRIT_LOG.get().defaultBlockState()) {
            world.setBlock(pos, state, 3);
        }
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

    public static BlockPos moveToSurface(ISeedReader world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.mutable();

        // climb up until we find air
        while (!world.isStateAtPosition(mutable, BlockState::isAir)) {
            mutable.move(Direction.UP);
        }

        return mutable.immutable();
    }
}