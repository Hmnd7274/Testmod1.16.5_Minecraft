package net.hamnd.testmod;

import net.hamnd.testmod.TestMod;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Direction;
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

    public static final RegistryObject<Block> SPIRIT_LOG = registerBlock("spirit_log",
            () -> log(MaterialColor.DIRT, MaterialColor.PODZOL),
            ItemGroup.TAB_MISC);

    public static final RegistryObject<Block> SPIRIT_LEAVES = registerBlock("spirit_leaves",
            () -> new LeavesBlock(AbstractBlock.Properties.of(Material.LEAVES).strength(0.2F)
                    .randomTicks().sound(SoundType.GRASS).noOcclusion()
                    .isValidSpawn((a, b, c, entity) -> entity == EntityType.OCELOT || entity == EntityType.PARROT)
                    .isSuffocating((a, b, c) -> false)
                    .isViewBlocking((a, b, c) -> false)),
            ItemGroup.TAB_MISC);

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block, ItemGroup tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static RotatedPillarBlock log(MaterialColor p_235430_0_, MaterialColor p_235430_1_) {
        return new RotatedPillarBlock(AbstractBlock.Properties.of(Material.WOOD,
                (p_235431_2_) ->
                        p_235431_2_.getValue(RotatedPillarBlock.AXIS) ==
                                Direction.Axis.Y ? p_235430_0_
                                : p_235430_1_).strength(2.0F).sound(SoundType.WOOD));
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, ItemGroup tab) {

        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
