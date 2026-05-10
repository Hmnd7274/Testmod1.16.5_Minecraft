package net.hamnd.testmod;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class ModStructures {

    public static final DeferredRegister<Structure<?>> STRUCTURES =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, TestMod.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> DIAMOND_TOWER =
            STRUCTURES.register("diamond_tower", DiamondTowerStructure::new);

    public static IStructurePieceType DIAMOND_TOWER_PIECE;

    public static void setupStructures() {
        DIAMOND_TOWER_PIECE = Registry.register(
                Registry.STRUCTURE_PIECE,
                new ResourceLocation(TestMod.MOD_ID, "diamond_tower_piece"),
                (type, nbt) -> new DiamondTowerPiece(DIAMOND_TOWER_PIECE, nbt)
        );
        
        // Min 160 blocs entre deux tours, environ tous les 320 blocs
        setupMapSpacingAndLand(
                DIAMOND_TOWER.get(),
                new StructureSeparationSettings(20, 10, 147258369),
                false
        );
    }

    private static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings separation,
            boolean transformSurroundingLand) {

        Structure.STRUCTURES_REGISTRY.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.NOISE_AFFECTING_FEATURES = ImmutableList.<Structure<?>>builder()
                    .addAll(Structure.NOISE_AFFECTING_FEATURES)
                    .add(structure)
                    .build();
        }

        DimensionStructuresSettings.DEFAULTS =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.DEFAULTS)
                        .put(structure, separation)
                        .build();

        WorldGenRegistries.NOISE_GENERATOR_SETTINGS.entrySet().forEach(settings -> {
            Map<Structure<?>, StructureSeparationSettings> structureMap =
                    settings.getValue().structureSettings().structureConfig;
            if (structureMap instanceof ImmutableMap) {
                Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(structureMap);
                tempMap.put(structure, separation);
                settings.getValue().structureSettings().structureConfig = ImmutableMap.copyOf(tempMap);
            } else {
                structureMap.put(structure, separation);
            }
        });
    }

    public static void register(IEventBus eventBus) {
        STRUCTURES.register(eventBus);
    }
}