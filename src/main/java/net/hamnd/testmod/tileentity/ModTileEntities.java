package net.hamnd.testmod.tileentity;

import net.hamnd.testmod.TestMod;
import net.hamnd.testmod.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTileEntities {

    public static DeferredRegister<TileEntityType<?>> TILE_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TestMod.MOD_ID);


    public static RegistryObject<TileEntityType<DaisyStatueTile>> DAISY_STATUE_TILE =
            TILE_ENTITIES.register("daisy_statue_tile", () -> TileEntityType.Builder.create(
                    DaisyStatueTile::new, ModBlocks.DAISY_STATUE.get()).build(null));

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
