package net.hamnd.testmod.event;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.entity.ModEntityTypes;
import net.hamnd.testmod.entity.custom.*;
import net.hamnd.testmod.item.custom.ModSpawnEggItem;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.BUFF_ZOMBIE.get(), BuffZombieEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.PIGEON.get(), PigeonEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.DAISY.get(), DaisyEntity.setCustomAttributes().create());
        event.put(ModEntityTypes.DAISY_STATUE_ENTITY.get(), DaisyStatueEntity.setCustomAttributes().create());
    }

    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEggItem.initSpawnEggs();
    }
}