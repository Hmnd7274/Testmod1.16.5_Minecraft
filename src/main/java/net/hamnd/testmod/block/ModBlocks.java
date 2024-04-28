package net.hamnd.testmod.block;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.custom.JumpyBlock;
import net.hamnd.testmod.item.ModItemGroup;
import net.hamnd.testmod.item.ModItems;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Random;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod.MOD_ID);

    public static final RegistryObject<Block> ZIRCON_BLOCK = registerBlock("zircon_block",
            () -> new Block(Block.Properties.of(Material.STONE)
                    .strength(6f)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()),
            ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> ZIRCON_ORE = registerBlock("zircon_ore",
            () -> new OreBlock(AbstractBlock.Properties.of(Material.STONE)
                    .strength(6f)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()),
            ModItemGroup.TEST_GROUP);
    public static final RegistryObject<Block> DEEPSLATE_ZIRCON_ORE = registerBlock("deepslate_zircon_ore",
            () -> new OreBlock(Block.Properties.of(Material.STONE)
                    .strength(7f)
                    .harvestLevel(2).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()),
            ModItemGroup.TEST_GROUP);

    public static final RegistryObject<Block> JUMPY_BLOCK = registerBlock("jumpy_block",
            () -> new JumpyBlock(Block.Properties.of(Material.STONE)
                    .strength(4f)
                    .harvestLevel(1).harvestTool(ToolType.PICKAXE).requiresCorrectToolForDrops()),
            ModItemGroup.TEST_GROUP);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, ItemGroup tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, ItemGroup tab) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
