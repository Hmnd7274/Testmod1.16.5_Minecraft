package net.hamnd.testmod.item;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.ModBlocks;
import net.hamnd.testmod.item.custom.EightBallItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon",
            () -> new Item(new Item.Properties()
                    .group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> RAW_ZIRCON = ITEMS.register("raw_zircon",
            () -> new Item(new Item.Properties()
                    .group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> EIGHT_BALL = ITEMS.register("eight_ball",
            () -> new EightBallItem(new Item.Properties()
                    .group(ModItemGroup.TEST_GROUP)
                    .maxStackSize(1)));

    public static final RegistryObject<Item> MEJOBERRY_SEEDS = ITEMS.register("mejoberry_seeds",
            () -> new BlockNamedItem(ModBlocks.BLUEBERRY_CROP.get(),new Item.Properties()
                    .group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> MEJOBERRY = ITEMS.register("mejoberry",
            () -> new Item(new Item.Properties()
                    .food(new Food.Builder().hunger(2).saturation(2f).build())
                    .group(ModItemGroup.TEST_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
