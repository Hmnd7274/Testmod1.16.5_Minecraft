package net.hamnd.testmod;

import com.google.common.collect.ImmutableMap;
import net.hamnd.testmod.block.ModBlocks;
import net.hamnd.testmod.entity.ModEntityTypes;
import net.hamnd.testmod.item.ModItems;
import net.hamnd.testmod.item.util.ModItemModelProperties;
import net.hamnd.testmod.painting.ModPaintings;
import net.hamnd.testmod.entity.villager.ModVillagers;
import net.hamnd.testmod.entity.renderer.BuffZombieRenderer;
import net.hamnd.testmod.entity.renderer.PigeonRenderer;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.*;
import net.minecraft.item.AxeItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(TestMod.MOD_ID)
public class TestMod {
    public static final String MOD_ID = "testmod";
    private static final Logger LOGGER = LogManager.getLogger();
    // veryImportant
    public TestMod() {

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModVillagers.register(modEventBus);
        ModPaintings.register(modEventBus);

        ModEntityTypes.register(modEventBus);

        modEventBus.addListener(this::setup);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        modEventBus.addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModVillagers::registerPOIs);
        event.enqueueWork(() -> {
            AxeItem.BLOCK_STRIPPING_MAP = new ImmutableMap.Builder<Block, Block>().putAll(AxeItem.BLOCK_STRIPPING_MAP)
                    .put(ModBlocks.REDWOOD_LOG.get(), ModBlocks.STRIPPED_REDWOOD_LOG.get())
                    .put(ModBlocks.REDWOOD_WOOD.get(), ModBlocks.STRIPPED_REDWOOD_WOOD.get()).build();
        });
    }

    public void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(ModBlocks.BLUEBERRY_CROP.get(), RenderType.getCutout());

            RenderTypeLookup.setRenderLayer(ModBlocks.REDWOOD_LEAVES.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(ModBlocks.REDWOOD_SAPLING.get(), RenderType.getCutout());

            ModItemModelProperties.makeBow(ModItems.ZIRCON_BOW.get());
        });

        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.BUFF_ZOMBIE.get(), BuffZombieRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityTypes.PIGEON.get(), PigeonRenderer::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {

    }

    private void processIMC(final InterModProcessEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }
}