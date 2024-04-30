package net.hamnd.testmod.item;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.ModBlocks;
import net.hamnd.testmod.item.custom.EightBallItem;
import net.minecraft.item.*;
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

    public static final RegistryObject<Item> ZIRCON_SWORD = ITEMS.register("zircon_sword",
            () -> new SwordItem(ModItemTier.ZIRCON, 3, -2.4f,
                    new Item.Properties().group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> ZIRCON_PICKAXE = ITEMS.register("zircon_pickaxe",
            () -> new PickaxeItem(ModItemTier.ZIRCON, 1, -2.8f,
                    new Item.Properties().group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> ZIRCON_SHOVEL = ITEMS.register("zircon_shovel",
            () -> new ShovelItem(ModItemTier.ZIRCON, 1.5f, -3.0f,
                    new Item.Properties().group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> ZIRCON_AXE = ITEMS.register("zircon_axe",
            () -> new AxeItem(ModItemTier.ZIRCON, 6.0f, -3.1f,
                    new Item.Properties().group(ModItemGroup.TEST_GROUP)));
    public static final RegistryObject<Item> ZIRCON_HOE = ITEMS.register("zircon_hoe",
            () -> new HoeItem(ModItemTier.ZIRCON, -2, -1.0f,
                    new Item.Properties().group(ModItemGroup.TEST_GROUP)));

    public static final RegistryObject<Item> ZIRCON_BOW = ITEMS.register("zircon_bow",
            () -> new BowItem(new Item.Properties().group(ModItemGroup.TEST_GROUP)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
