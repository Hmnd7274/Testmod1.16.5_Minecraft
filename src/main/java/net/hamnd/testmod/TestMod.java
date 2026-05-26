package net.hamnd.testmod;

import net.hamnd.testmod.commands.ModCommands;
import net.hamnd.testmod.world.gen.trunkplacer.ModTrunkPlacerTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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

        ModItems.register(modBus);
        ModBlocks.register(modBus);
        ModStructures.register(modBus);

        ModTrunkPlacerTypes.registerTrunkPlacers();
        
        modBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
        });

        RenderTypeLookup.setRenderLayer(ModBlocks.SPIRIT_LEAVES.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }
}
