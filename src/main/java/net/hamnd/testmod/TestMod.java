package net.hamnd.testmod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TestMod.MOD_ID)
public class TestMod {
    public static final String MOD_ID = "testmod";
    public static final Logger LOGGER = LogManager.getLogger();

    public TestMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Enregistre la structure via DeferredRegister
        ModStructures.STRUCTURES.register(modBus);

        // commonSetup : enregistre le Piece + patches statiques
        modBus.addListener(this::commonSetup);

        // BiomeLoadingEvent : injecte la structure dans les biomes jungle
        // Ce listener est sur le FORGE bus, pas le MOD bus
        MinecraftForge.EVENT_BUS.register(new BiomeEvents());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(ModStructures::setup);
    }
}
