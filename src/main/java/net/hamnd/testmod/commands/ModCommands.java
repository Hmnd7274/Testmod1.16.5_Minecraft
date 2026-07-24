package net.hamnd.testmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.hamnd.testmod.SpiritTreeSpawn;
import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.world.gen.feature.ModConfiguredFeatures;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("tree")
                .executes(ModCommands::spawnTree));
        dispatcher.register(
                Commands.literal("treeH")
                        .then(Commands.argument("treeHeight", IntegerArgumentType.integer())
                                .executes(ModCommands::spawnTreeHeight))
        );
        dispatcher.register(
                Commands.literal("treeB")
                        .then(Commands.argument("branch", IntegerArgumentType.integer())
                                .executes(ModCommands::spawnTreeBranch))
        );
        dispatcher.register(Commands.literal("ftree")
                .executes(ModCommands::spawnTreeFeature));
    }

    private static int spawnTree(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        BlockPos pos = new BlockPos(
                source.getPosition().x,
                source.getLevel().getChunkSource().getGenerator().getBaseHeight((int) source.getPosition().x, (int) source.getPosition().z, Heightmap.Type.WORLD_SURFACE_WG),
                source.getPosition().z
        );
        SpiritTreeSpawn.spawnTree(
                source.getLevel(),
                source.getLevel().getRandom(),
                new MutableBoundingBox(
                        pos.getX() - 50, 50, pos.getZ() - 50,
                        pos.getX() + 50, 120, pos.getZ() + 50),
                pos,
                14,
                4
        );
        return 1;
    }

    private static int spawnTreeHeight(CommandContext<CommandSource> context) {
        try {
            int treeHeight = IntegerArgumentType.getInteger(context, "treeHeight");
            CommandSource source = context.getSource();
            BlockPos pos = new BlockPos(
                    source.getPosition().x,
                    source.getLevel().getChunkSource().getGenerator().getBaseHeight((int) source.getPosition().x, (int) source.getPosition().z, Heightmap.Type.WORLD_SURFACE_WG),
                    source.getPosition().z
            );
            SpiritTreeSpawn.spawnTree(
                    source.getLevel(),
                    source.getLevel().getRandom(),
                    new MutableBoundingBox(
                            pos.getX() - 50, 50, pos.getZ() - 50,
                            pos.getX() + 50, 120, pos.getZ() + 50),
                    pos,
                    treeHeight,
                    4
            );
        } catch (Exception e) {
            TestMod.LOGGER.info(e);
        }
        return 1;
    }


    private static int spawnTreeBranch(CommandContext<CommandSource> context) {
        try {
        int branch = IntegerArgumentType.getInteger(context, "branch");
        CommandSource source = context.getSource();
        BlockPos pos = new BlockPos(
                source.getPosition().x,
                source.getLevel().getChunkSource().getGenerator().getBaseHeight((int) source.getPosition().x, (int) source.getPosition().z, Heightmap.Type.WORLD_SURFACE_WG),
                source.getPosition().z
        );
        SpiritTreeSpawn.spawnTree(
                source.getLevel(),
                source.getLevel().getRandom(),
                new MutableBoundingBox(
                        pos.getX() - 50, 50, pos.getZ() - 50,
                        pos.getX() + 50, 120, pos.getZ() + 50),
                pos,
                14,
                branch
        );
        } catch (Exception e) {
            TestMod.LOGGER.info(e);
        }
        return 1;
    }

    private static int spawnTreeFeature(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        BlockPos pos = new BlockPos(source.getPosition());

        ConfiguredFeature<?, ?> feature = ModConfiguredFeatures.TREE;

        feature.place(source.getLevel(),
                source.getLevel().getChunkSource().getGenerator(),
                source.getLevel().getRandom(),
                pos);
        return 1;
    }
}