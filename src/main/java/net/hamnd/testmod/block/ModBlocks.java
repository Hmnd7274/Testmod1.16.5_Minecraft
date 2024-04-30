package net.hamnd.testmod.block;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.custom.BlueberryCropBlock;
import net.hamnd.testmod.block.custom.JumpyBlock;
import net.hamnd.testmod.block.custom.ZirconLampBlock;
import net.hamnd.testmod.block.custom.trees.RedWoodTree;
import net.hamnd.testmod.item.ModItemGroup;
import net.hamnd.testmod.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod.MOD_ID);

    public static final RegistryObject<Block> ZIRCON_BLOCK = registerBlock("zircon_block",
            () -> new Block(Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(6f)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool()),
            ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> ZIRCON_ORE = registerBlock("zircon_ore",
            () -> new OreBlock(AbstractBlock.Properties.create(Material.ROCK)
                    .hardnessAndResistance(6f)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool()),
            ModItemGroup.TEST_GROUP);
    public static final RegistryObject<Block> DEEPSLATE_ZIRCON_ORE = registerBlock("deepslate_zircon_ore",
            () -> new OreBlock(Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(7f)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).setRequiresTool()),
            ModItemGroup.TEST_GROUP);


    public static final RegistryObject<Block> JUMPY_BLOCK = registerBlock("jumpy_block",
            () -> new JumpyBlock(Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4f)
                    .harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()),
            ModItemGroup.TEST_GROUP);


    public static final RegistryObject<Block> ZIRCON_LAMP = registerBlock("zircon_lamp",
            () -> new ZirconLampBlock(Block.Properties.create(Material.ROCK)
                    .hardnessAndResistance(4f)
                    .harvestLevel(1).harvestTool(ToolType.PICKAXE).setRequiresTool()
                    .setLightLevel(state -> state.get(ZirconLampBlock.LIT) ? 15 : 0)),
            ModItemGroup.TEST_GROUP);


    public static final RegistryObject<Block> BLUEBERRY_CROP = BLOCKS.register("blueberry_crop",
            () -> new BlueberryCropBlock(Block.Properties.from(Blocks.WHEAT)));


    public static final RegistryObject<Block> REDWOOD_LOG = registerBlock("redwood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.OAK_LOG)), ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> REDWOOD_WOOD = registerBlock("redwood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.OAK_WOOD)), ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> STRIPPED_REDWOOD_LOG = registerBlock("stripped_redwood_log",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_LOG)), ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> STRIPPED_REDWOOD_WOOD = registerBlock("stripped_redwood_wood",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.STRIPPED_OAK_WOOD)), ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> REDWOOD_PLANKS = registerBlock("redwood_planks",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.from(Blocks.OAK_PLANKS)), ModItemGroup.TEST_GROUP);


    public static final RegistryObject<Block> REDWOOD_LEAVES = registerBlock("redwood_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.create(Material.LEAVES)
                    .hardnessAndResistance(0.2F).tickRandomly().sound(SoundType.PLANT).notSolid()), ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> REDWOOD_SAPLING = registerBlock("redwood_sapling",
            () -> new SaplingBlock(new RedWoodTree(), AbstractBlock.Properties.from(Blocks.OAK_SAPLING)), ModItemGroup.TEST_GROUP);


    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, ItemGroup tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, ItemGroup tab) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().group(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
