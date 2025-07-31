package net.hamnd.testmod.entity;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.entity.custom.DaisyEntity;
import net.hamnd.testmod.entity.custom.DaisyStatueEntity;
import net.hamnd.testmod.entity.custom.*;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEntityTypes {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES
            = DeferredRegister.create(ForgeRegistries.ENTITIES, TestMod.MOD_ID);

    public static final RegistryObject<EntityType<BuffZombieEntity>> BUFF_ZOMBIE =
            ENTITY_TYPES.register("buff_zombie",
                    () -> EntityType.Builder.create(BuffZombieEntity::new,
                                    EntityClassification.MONSTER).size(1.4f, 2.6f)
                            .build(new ResourceLocation(TestMod.MOD_ID, "buff_zombie").toString()));

    public static final RegistryObject<EntityType<PigeonEntity>> PIGEON =
            ENTITY_TYPES.register("pigeon",
                    () -> EntityType.Builder.create(PigeonEntity::new,
                                    EntityClassification.CREATURE).size(0.4f, 0.3f)
                            .build(new ResourceLocation(TestMod.MOD_ID, "pigeon").toString()));

    public static final RegistryObject<EntityType<DaisyEntity>> DAISY =
            ENTITY_TYPES.register("daisy",
                    () -> EntityType.Builder.create(DaisyEntity::new,
                                    EntityClassification.CREATURE).size(1.4F, 2.7F)
                            .build(new ResourceLocation(TestMod.MOD_ID, "daisy").toString()));

    public static final RegistryObject<EntityType<DaisyStatueEntity>> DAISY_STATUE_ENTITY =
            ENTITY_TYPES.register("daisy_statue_entity",
                    () -> EntityType.Builder.create(DaisyStatueEntity::new,
                                    EntityClassification.CREATURE).size(0.75F, 1F)
                            .build(new ResourceLocation(TestMod.MOD_ID, "daisy_statue_entity").toString()));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}