package net.hamnd.testmod;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TestMod.MOD_ID);

    public static RegistryObject<TileEntityType<SpiritualLogBlock.SpiritualLogTileEntity>> SPIRITUAL_LOG_TILE =
            TILE_ENTITIES.register("spiritual_log_tile", () -> TileEntityType.Builder.of(
                    SpiritualLogBlock.SpiritualLogTileEntity::new, ModBlocks.SPIRIT_LOG.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}