package net.hamnd.testmod.item;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.ModBlocks;
import net.hamnd.testmod.item.custom.EightBallItem;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TestMod.MOD_ID);

    public static final RegistryObject<Item> ZIRCON = ITEMS.register("zircon",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> RAW_ZIRCON = ITEMS.register("raw_zircon",
            () -> new Item(new Item.Properties()
                    .tab(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> EIGHT_BALL = ITEMS.register("eight_ball",
            () -> new EightBallItem(new Item.Properties()
                    .tab(ModItemGroup.TEST_GROUP)
                    .stacksTo(1)));

    public static final RegistryObject<Item> BLUEBERRY_SEEDS = ITEMS.register("blueberry_seeds",
            () -> new BlockNamedItem(ModBlocks.BLUEBERRY_CROP.get(),new Item.Properties()
                    .tab(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> BLUEBERRY = ITEMS.register("blueberry",
            () -> new Item(new Item.Properties()
                    .food(new Food.Builder().nutrition(2).saturationMod(2f).build())
                    .tab(ModItemGroup.TEST_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
