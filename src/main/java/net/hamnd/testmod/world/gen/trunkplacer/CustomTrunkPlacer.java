package net.hamnd.testmod.world.gen.trunkplacer;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.hamnd.testmod.utils.Calculator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import java.util.*;

import static net.minecraft.world.gen.feature.TreeFeature.isAirOrLeavesAt;

public class CustomTrunkPlacer extends AbstractTrunkPlacer {
    public static final Codec<CustomTrunkPlacer> CODEC =
            RecordCodecBuilder.create(instance -> AbstractTrunkPlacer.getAbstractTrunkCodec(instance)
                    .apply(instance, CustomTrunkPlacer::new));

    public static final List<Integer> weightedList = Arrays.asList(1,2,2,3,3,3);
    public static final List<Integer> weightedListTwo = Arrays.asList(3,4,4,4);
    public static final List<Integer> weightedListFoliage = Arrays.asList(0,0,1);
    public static final List<Integer> weightedListRoots = Arrays.asList(3,3,4,4,5);
    public static final List<Integer> weightedListRootsGoDown = Arrays.asList(3,4,4,5);
    
    public CustomTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }
    
    @Override
    protected TrunkPlacerType<?> getPlacerType() {
        return ModTrunkPlacerTypes.CUSTOM_TRUNK_PLACER; // ton registry custom
    }

    @Override
    public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader world, Random rand, int height,
                                                   BlockPos pos, Set<BlockPos> trunkBlocks,
                                                   MutableBoundingBox bbox, BaseTreeFeatureConfig config) {
        List<FoliagePlacer.Foliage> foliagePlacerList = Lists.newArrayList();
        int nbBranch = 0;
        Calculator.sendMsg("height = " + height);
        
        // 1️⃣ Générer le tronc principal
        for (int y = 0; y < height; y++) {
            BlockPos logPos = pos.up(y);
            func_236911_a_(world, rand, logPos, trunkBlocks, bbox, config); // pose un bloc de bois
            func_236911_a_(world, rand, logPos.add(1,0,0), trunkBlocks, bbox, config); // pose un bloc de bois
            func_236911_a_(world, rand, logPos.add(1,0,1), trunkBlocks, bbox, config); // pose un bloc de bois
            func_236911_a_(world, rand, logPos.add(0,0,1), trunkBlocks, bbox, config); // pose un bloc de bois
        }

        // Générer les raçines
        placeRoots(world, rand, pos, trunkBlocks, bbox, config);
        
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

                    BlockPos branchIni = getClosestTrunkBlock(new BlockPos[]{pos.up(y), pos.up(y).add(1,0,0), pos.up(y).add(1,0,1), pos.up(y).add(0,0,1)}, new BlockPos(Math.round(MathHelper.cos(angle) * length), hauteur, Math.round(MathHelper.sin(angle) * length)));
                    BlockPos branchStart;
                    BlockPos branchEnd = null;

                    for (int tkt = 0; tkt < 3; tkt += 1) {
                        if (tkt == 0) {
                            branchStart = branchIni;
                            dy = (int) (hauteur / 5.5);
                            dx = (int) Math.round(MathHelper.cos(angle) * (length / 2.5));
                            dz = (int) Math.round(MathHelper.sin(angle) * (length / 2.5));
                            branchEnd = branchIni.add(dx, dy, dz);
                        } else if (tkt == 1) {
                            branchStart = branchEnd;
                            dy = ((hauteur / 3) * 2);
                            dx = (int) Math.round(MathHelper.cos(angle) * ((length / 7.0)) * 5);
                            dz = (int) Math.round(MathHelper.sin(angle) * ((length / 7.0)) * 5);
                            branchEnd = branchIni.add(dx, dy, dz);
                        } else {
                            branchStart = branchEnd;
                            dy = hauteur;
                            dx = Math.round(MathHelper.cos(angle) * length);
                            dz = Math.round(MathHelper.sin(angle) * length);
                            branchEnd = branchIni.add(dx, dy, dz);
                        }
                        placeBranch(world, rand, branchStart, branchEnd, trunkBlocks, bbox, config, foliagePlacerList);
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

                    BlockPos branchIni = getClosestTrunkBlock(new BlockPos[]{pos.up(y+1), pos.up(y+1).add(1,0,0), pos.up(y+1).add(1,0,1), pos.up(y+1).add(0,0,1)}, new BlockPos(Math.round(MathHelper.cos(angle) * length), hauteur, Math.round(MathHelper.sin(angle) * length)));
                    BlockPos branchStart;
                    BlockPos branchEnd = null;

                    for (int tkt = 0; tkt < 3; tkt += 1) {
                        if (tkt == 0) {
                            branchStart = branchIni;
                            dy = (int) (hauteur / 5.5);
                            dx = (int) Math.round(MathHelper.cos(angle) * (length / 2.5));
                            dz = (int) Math.round(MathHelper.sin(angle) * (length / 2.5));
                            branchEnd = branchIni.add(dx, dy, dz);
                        } else if (tkt == 1) {
                            branchStart = branchEnd;
                            dy = ((hauteur / 3) * 2);
                            dx = (int) Math.round(MathHelper.cos(angle) * ((length / 7.0)) * 5);
                            dz = (int) Math.round(MathHelper.sin(angle) * ((length / 7.0)) * 5);
                            branchEnd = branchIni.add(dx, dy, dz);
                        } else {
                            branchStart = branchEnd;
                            dy = hauteur;
                            dx = Math.round(MathHelper.cos(angle) * length);
                            dz = Math.round(MathHelper.sin(angle) * length);
                            branchEnd = branchIni.add(dx, dy, dz);
                        }
                        placeBranch(world, rand, branchStart, branchEnd, trunkBlocks, bbox, config, foliagePlacerList);
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
                                    pos.up(y),
                                    pos.up(y).add(1,0,0),
                                    pos.up(y).add(1,0,1),
                                    pos.up(y).add(0,0,1)
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
                            branchEnd = branchIni.add(dx, dy, dz);
                        } else if (tkt == 1) {
                            branchStart = branchEnd;
                            dy = ((hauteur / 3) * 2);
                            dx = (int) Math.round(MathHelper.cos(angle) * ((length / 7.0)) * 5);
                            dz = (int) Math.round(MathHelper.sin(angle) * ((length / 7.0)) * 5);
//                        Calculator.sendMsg("dx et dz et dy " + dx + "; " + dz + "; " + dy);
                            branchEnd = branchIni.add(dx, dy, dz);
                        } else {
                            branchStart = branchEnd;
                            dy = hauteur;
                            dx = Math.round(MathHelper.cos(angle) * length);
                            dz = Math.round(MathHelper.sin(angle) * length);
//                        Calculator.sendMsg("dx et dz et dy " + dx + "; " + dz + "; " + dy);
                            branchEnd = branchIni.add(dx, dy, dz);
                        }
//                    Calculator.sendMsg("start= " + branchStart + "\nend= " + branchEnd + "\n");
                        placeBranch(world, rand, branchStart, branchEnd, trunkBlocks, bbox, config, foliagePlacerList);
                    }

                    // feuillage au bout
                    foliagePlacerList.add(new FoliagePlacer.Foliage(branchEnd, -1, false));
//                // dessine une petite branche qui monte
//                for (int i = 0; i < length; i++) {
//                    BlockPos branchPos = pos.add(dx, y + i / 2, dz);
//                    func_236911_a_(world, rand, branchPos, trunkBlocks, bbox, config);
//                }

                    // ajoute du feuillage au bout
//                foliageList.add(new FoliagePlacer.Foliage(pos.add(dx, y + length, dz), -2, false));
                }
            }
            
        }
//         3️⃣ Couronne de feuillage au sommet
        placeCanopy(world, rand, pos.up(height - 2),9, 4, bbox, config);
//        foliagePlacerList.add(new FoliagePlacer.Foliage(pos.up(height), 2 + (height / 7), false));
        Calculator.sendMsg("number of branches = " + nbBranch);
        return foliagePlacerList;
    }

    private void placeBranch(IWorldGenerationReader world, Random rand,
                                              BlockPos start, BlockPos end,
                                              Set<BlockPos> trunkBlocks, MutableBoundingBox bbox,
                                              BaseTreeFeatureConfig config, List<FoliagePlacer.Foliage> foliagePlacerList) {
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
            placeLog(world, logState, pos, trunkBlocks, bbox);
            
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
     * Retourne le bloc parmi "trunkBlocks" le plus proche de "target"
     */
    public static BlockPos getClosestTrunkBlock(BlockPos[] trunkBlocks, BlockPos target) {
        if (trunkBlocks == null || trunkBlocks.length == 0) return null;

        BlockPos closest = trunkBlocks[0];
        double minDistance = closest.distanceSq(target);

        for (int i = 1; i < trunkBlocks.length; i++) {
            double dist = trunkBlocks[i].distanceSq(target);
            if (dist < minDistance) {
                closest = trunkBlocks[i];
                minDistance = dist;
            }
        }
        return closest;
    }

    private void placeRoots(IWorldGenerationReader world, Random rand,
                           BlockPos pos, Set<BlockPos> placedBlocks,
                           MutableBoundingBox bbox, BaseTreeFeatureConfig config) {

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
                            pos.add(1,0,0),
                            pos.add(1,0,1),
                            pos.add(0,0,1)
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
//                BlockPos nextPos = branchIni.add(dx, dy, dz);
//                Calculator.sendMsg("nextpos = " + nextPos.toString());
            
            int dy = 0;
            int dx = (int) Math.round(MathHelper.cos(angle) * (length / 3.0) * 2);
            int dz = (int) Math.round(MathHelper.sin(angle) * (length / 3.0) * 2);
            BlockPos branchEnd = branchIni.add(dx, dy, dz);
            
//            Calculator.sendMsg("start= " + branchIni + "\nend= " + branchEnd + "\n");
            // Placer les blocs de la racine entre currentPos et nextPos
            BlockPos branchStart = placeRootSegment(world, rand, branchIni, branchEnd, placedBlocks, bbox, config, false);

            dy = -3;
            dx = (int) Math.round(MathHelper.cos(angle) * (length / 3.0));
            dz = (int) Math.round(MathHelper.sin(angle) * (length / 3.0));
            branchEnd = branchStart.add(dx, dy, dz);
            
//            Calculator.sendMsg("start= " + branchIni + "\nend= " + branchEnd + "\n");
            placeRootSegment(world, rand, branchStart, branchEnd, placedBlocks, bbox, config, true);
        }
    }

    private BlockPos placeRootSegment(IWorldGenerationReader world, Random rand,
                                    BlockPos start, BlockPos end,
                                    Set<BlockPos> trunkBlocks, MutableBoundingBox bbox,
                                    BaseTreeFeatureConfig config, boolean canGoUnderground) {
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
                pos = pos.down();
                rootGoDownNumber--;
//                Calculator.sendMsg("OMG DOWN = " + pos);
            }
            
            BlockState logState = getLogState(config, rand, start, end);
            placeLog(world, logState, pos, trunkBlocks, bbox);
            
            x += xStep;
            y += yStep;
            z += zStep;
        }
        return pos;
    }

    public static BlockPos moveUpToSurface(IWorldGenerationReader world, BlockPos pos) {
        BlockPos.Mutable mutable = pos.toMutable();

        // climb up until we find air
        while (!world.hasBlockState(mutable, BlockState::isAir)) {
            mutable.move(Direction.UP);
        }

        return mutable.toImmutable();
    }

    protected void placeLog(IWorldGenerationReader reader, BlockState state, BlockPos pos, Set<BlockPos> p_236911_3_, MutableBoundingBox p_236911_4_) {
        func_236913_a_(reader, pos, state, p_236911_4_);
        p_236911_3_.add(pos.toImmutable());
    }

    public static boolean isReplaceableAtCustom(IWorldGenerationBaseReader reader, BlockPos pos) {
        return isAirOrLeavesAt(reader, pos) || isTallPlantAt(reader, pos) || isWaterAt(reader, pos) || isLogAt(reader, pos);
    }

    private static boolean isLogAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> state.getBlock() == Blocks.JUNGLE_LOG);
    }

    private static boolean isTallPlantAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            Material material = state.getMaterial();
            return material == Material.TALL_PLANTS;
        });
    }

    private static boolean isWaterAt(IWorldGenerationBaseReader reader, BlockPos pos) {
        return reader.hasBlockState(pos, (state) -> {
            return state.matchesBlock(Blocks.WATER);
        });
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
                             MutableBoundingBox bbox,
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
                        BlockPos pos = start.add(dx, y, dz);
                        positions.add(pos);
                    }
                }
            }
        }
        for (BlockPos pos : positions) {
            // place une feuille si l'endroit est libre
            if (TreeFeature.isAirOrLeavesAt(world, pos)) {
                func_236913_a_(world, pos, config.leavesProvider.getBlockState(rand, pos), bbox);
            }
        }
    }

//    private FoliagePlacer.Foliage placeCurvedBranch(IWorldGenerationReader world, Random rand,
//                                                    BlockPos start, BlockPos end,
//                                                    Set<BlockPos> trunkBlocks, MutableBoundingBox bbox,
//                                                    BaseTreeFeatureConfig config) {
//        int dx = end.getX() - start.getX();
//        int dy = end.getY() - start.getY();
//        int dz = end.getZ() - start.getZ();
//
//        int steps = Math.max(Math.abs(dx), Math.abs(dz));
//        float xStep = dx / (float) steps;
//        float zStep = dz / (float) steps;
//
//        float x = start.getX();
//        float z = start.getZ();
//
//        BlockPos lastPos = start;
//
//        for (int i = 0; i <= steps; i++) {
//            float progress = i / (float) steps;
//            int bx = Math.round(x);
//            int bz = Math.round(z);
//            int by = start.getY() + Math.round(progress * progress * progress * dy);
////            int by = Math.toIntExact(start.getY() + Math.round((Math.pow(progress, 1.5)) * dy));
//
//            BlockPos nextPos = new BlockPos(bx, by, bz);
//
//            // relie lastPos → nextPos sans trou
//            bresenhamAlgo(world, rand, lastPos, nextPos, trunkBlocks, bbox, config);
//
//            lastPos = nextPos;
//            x += xStep;
//            z += zStep;
//        }
//
//        // feuillage au bout
//        return new FoliagePlacer.Foliage(end, -2, false);
//    }
//
//    private void bresenhamAlgo(IWorldGenerationReader world, Random rand,
//                          BlockPos start, BlockPos end,
//                          Set<BlockPos> trunkBlocks, MutableBoundingBox bbox,
//                          BaseTreeFeatureConfig config) {
//        int x1 = start.getX();
//        int y1 = start.getY();
//        int z1 = start.getZ();
//        int x2 = end.getX();
//        int y2 = end.getY();
//        int z2 = end.getZ();
//
//        int dx = Math.abs(x2 - x1);
//        int dy = Math.abs(y2 - y1);
//        int dz = Math.abs(z2 - z1);
//
//        int sx = x1 < x2 ? 1 : -1;
//        int sy = y1 < y2 ? 1 : -1;
//        int sz = z1 < z2 ? 1 : -1;
//
//        int err1, err2;
//        int dx2 = dx << 1;
//        int dy2 = dy << 1;
//        int dz2 = dz << 1;
//
//        if (dx >= dy && dx >= dz) {
//            err1 = dy2 - dx;
//            err2 = dz2 - dx;
//            for (int i = 0; i <= dx; i++) {
//                func_236911_a_(world, rand, new BlockPos(x1, y1, z1), trunkBlocks, bbox, config);
//                if (err1 > 0) { y1 += sy; err1 -= dx2; }
//                if (err2 > 0) { z1 += sz; err2 -= dx2; }
//                err1 += dy2;
//                err2 += dz2;
//                x1 += sx;
//            }
//        } else if (dy >= dx && dy >= dz) {
//            err1 = dx2 - dy;
//            err2 = dz2 - dy;
//            for (int i = 0; i <= dy; i++) {
//                func_236911_a_(world, rand, new BlockPos(x1, y1, z1), trunkBlocks, bbox, config);
//                if (err1 > 0) { x1 += sx; err1 -= dy2; }
//                if (err2 > 0) { z1 += sz; err2 -= dy2; }
//                err1 += dx2;
//                err2 += dz2;
//                y1 += sy;
//            }
//        } else {
//            err1 = dy2 - dz;
//            err2 = dx2 - dz;
//            for (int i = 0; i <= dz; i++) {
//                func_236911_a_(world, rand, new BlockPos(x1, y1, z1), trunkBlocks, bbox, config);
//                if (err1 > 0) { y1 += sy; err1 -= dz2; }
//                if (err2 > 0) { x1 += sx; err2 -= dz2; }
//                err1 += dy2;
//                err2 += dx2;
//                z1 += sz;
//            }
//        }
//    }
}