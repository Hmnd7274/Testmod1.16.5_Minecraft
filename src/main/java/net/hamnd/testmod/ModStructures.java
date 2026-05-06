package net.hamnd.testmod;

import com.google.common.collect.ImmutableMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ModStructures {

    // spacing=12, separation=4 → ~1 structure toutes les ~200 blocs dans les jungles
    public static final StructureSeparationSettings SEPARATION =
            new StructureSeparationSettings(12, 4, 147258369);

    public static final DeferredRegister<Structure<?>> STRUCTURES =
            DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, TestMod.MOD_ID);

    public static final RegistryObject<Structure<NoFeatureConfig>> DIAMOND_TOWER =
            STRUCTURES.register("diamond_tower",
                    () -> new DiamondTowerStructure(NoFeatureConfig.CODEC));

    public static IStructurePieceType DIAMOND_TOWER_PIECE;

    private static final Unsafe UNSAFE = getUnsafe();

    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unsafe inaccessible", e);
        }
    }

    public static void setup() {
        // 1. Enregistre le type de Piece
        DIAMOND_TOWER_PIECE = Registry.register(
                Registry.STRUCTURE_PIECE,
                new ResourceLocation(TestMod.MOD_ID, "diamond_tower_piece"),
                (type, nbt) -> new DiamondTowerPiece((IStructurePieceType) type, nbt)
        );

        // 2. Enregistre dans Structure.STEP (nom MCP de STRUCTURE_DECORATION_STAGE_MAP)
        try {
            Field stepField = Structure.class.getDeclaredField("STEP");
            stepField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Structure<?>, net.minecraft.world.gen.GenerationStage.Decoration> step =
                    (Map<Structure<?>, net.minecraft.world.gen.GenerationStage.Decoration>) stepField.get(null);
            step.put(DIAMOND_TOWER.get(), net.minecraft.world.gen.GenerationStage.Decoration.SURFACE_STRUCTURES);
            TestMod.LOGGER.info("[TestMod] STEP enregistré");
        } catch (Exception e) {
            TestMod.LOGGER.error("[TestMod] Erreur STEP: {}", e.toString());
        }

        // 3. Enregistre dans STRUCTURES_REGISTRY (BiMap pour /locate)
        try {
            String key = new ResourceLocation(TestMod.MOD_ID, "diamond_tower").toString();
            Structure.STRUCTURES_REGISTRY.put(key, DIAMOND_TOWER.get());
            TestMod.LOGGER.info("[TestMod] STRUCTURES_REGISTRY enregistré");
        } catch (Exception e) {
            TestMod.LOGGER.error("[TestMod] Erreur STRUCTURES_REGISTRY: {}", e.toString());
        }

        // 4. Patche DimensionStructuresSettings.DEFAULTS via Unsafe
        patchDefaults();
    }

    private static void patchDefaults() {
        for (String fieldName : new String[]{"DEFAULTS", "field_236191_b_"}) {
            try {
                Field f = DimensionStructuresSettings.class.getDeclaredField(fieldName);
                f.setAccessible(true);

                @SuppressWarnings("unchecked")
                Map<Structure<?>, StructureSeparationSettings> orig =
                        (Map<Structure<?>, StructureSeparationSettings>) f.get(null);

                Map<Structure<?>, StructureSeparationSettings> copy = new HashMap<>(orig);
                copy.put(DIAMOND_TOWER.get(), SEPARATION);

                Object base = UNSAFE.staticFieldBase(f);
                long offset = UNSAFE.staticFieldOffset(f);
                UNSAFE.putObject(base, offset, ImmutableMap.copyOf(copy));

                TestMod.LOGGER.info("[TestMod] DEFAULTS patché via '{}' ({} structures)", fieldName, copy.size());
                return;
            } catch (NoSuchFieldException ignored) {
            } catch (Exception e) {
                TestMod.LOGGER.error("[TestMod] Erreur DEFAULTS '{}': {}", fieldName, e.toString());
            }
        }
        TestMod.LOGGER.error("[TestMod] ÉCHEC patch DEFAULTS");
    }
}
