package net.hamnd.testmod.villager;

import com.google.common.collect.ImmutableSet;
import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.ModBlocks;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.InvocationTargetException;

public class ModVillagers {
    public static final DeferredRegister<PointOfInterestType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, TestMod.MOD_ID);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.PROFESSIONS, TestMod.MOD_ID);

    public static final RegistryObject<PointOfInterestType> JUMPY_BLOCK_POI = POI_TYPES.register("jumpy_block_poi",
            () -> new PointOfInterestType("jumpy_block_poi", ImmutableSet.copyOf(ModBlocks.JUMPY_BLOCK.get().getStateContainer().getValidStates()),
                    1, 1));


    public static final RegistryObject<VillagerProfession> JUMP_MASTER = VILLAGER_PROFESSIONS.register("jumpy_master",
            () -> new VillagerProfession(
                    "jumpy-master", // Lambda using Supplier<T>
                    JUMPY_BLOCK_POI.get(),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.ENTITY_VILLAGER_WORK_ARMORER
            )
    );

public static void registerPOIs() {
    try {
        ObfuscationReflectionHelper.findMethod(PointOfInterestType.class,
                "registerBlockStates", PointOfInterestType.class).invoke(null, JUMPY_BLOCK_POI.get());
    } catch (InvocationTargetException | IllegalAccessException exception) {
        exception.printStackTrace();
    }
}

    public static void register(IEventBus eventBus) {
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
