package net.hamnd.testmod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.hamnd.testmod.world.gen.feature.ModConfiguredFeatures;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("tree")
                .executes(ModCommands::spawnTree));
        dispatcher.register(Commands.literal("treea")
                .executes(ModCommands::spawnTreeA));
        dispatcher.register(Commands.literal("treeb")
                .executes(ModCommands::spawnTreeB));
    }

    private static int spawnTree(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        BlockPos pos = new BlockPos(source.getPos());

        ConfiguredFeature<?, ?> feature = ModConfiguredFeatures.TREE;

        feature.generate(source.getWorld(),
                source.getWorld().getChunkProvider().getChunkGenerator(),
                source.getWorld().getRandom(),
                pos);
        return 1;
    }

    private static int spawnTreeA(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        BlockPos pos = new BlockPos(source.getPos());

        ConfiguredFeature<?, ?> feature = ModConfiguredFeatures.TREEA;

        feature.generate(source.getWorld(),
                source.getWorld().getChunkProvider().getChunkGenerator(),
                source.getWorld().getRandom(),
                pos);
        return 1;
    }

    private static int spawnTreeB(CommandContext<CommandSource> context) {
        CommandSource source = context.getSource();
        BlockPos pos = new BlockPos(source.getPos());

        ConfiguredFeature<?, ?> feature = ModConfiguredFeatures.TREEB;

        feature.generate(source.getWorld(),
                source.getWorld().getChunkProvider().getChunkGenerator(),
                source.getWorld().getRandom(),
                pos);
        return 1;
    }
}