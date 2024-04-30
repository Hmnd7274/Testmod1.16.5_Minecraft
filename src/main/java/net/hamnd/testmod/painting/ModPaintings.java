package net.hamnd.testmod.painting;

import net.hamnd.testmod.TestMod;
import net.minecraft.entity.item.PaintingType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPaintings {
    public static final DeferredRegister<PaintingType> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, TestMod.MOD_ID);

    public static final RegistryObject<PaintingType> PLANT = PAINTING_VARIANTS.register("plant",
            () -> new PaintingType(16, 16));
    public static final RegistryObject<PaintingType> WANDERER = PAINTING_VARIANTS.register("wanderer",
            () -> new PaintingType(16, 32));
    public static final RegistryObject<PaintingType> SUNSET = PAINTING_VARIANTS.register("sunset",
            () -> new PaintingType(32, 16));

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
