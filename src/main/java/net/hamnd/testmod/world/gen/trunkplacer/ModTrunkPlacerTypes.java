package net.hamnd.testmod.world.gen.trunkplacer;


import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;

import java.lang.reflect.Constructor;

public class ModTrunkPlacerTypes {
    public static TrunkPlacerType<CustomTrunkPlacer> CUSTOM_TRUNK_PLACER;
//    public static TrunkPlacerType<CustomTrunkPlacer> CUSTOM_MEGA_TRUNK_PLACER;

    public static void registerTrunkPlacers() {
//        Registry.register(Registry.TRUNK_REPLACER,
//                new ResourceLocation("testmod", "custom_mega_trunk_placer"),
//                CUSTOM_MEGA_TRUNK_PLACER);
        try {
            Constructor<TrunkPlacerType> constructor =
                    TrunkPlacerType.class.getDeclaredConstructor(Codec.class);
            constructor.setAccessible(true);

            CUSTOM_TRUNK_PLACER = constructor.newInstance(CustomTrunkPlacer.CODEC);

            // Inscrire dans le registry vanilla
            Registry.register(Registry.TRUNK_REPLACER,
                    new ResourceLocation("testmod", "custom_trunk_placer"),
                    CUSTOM_TRUNK_PLACER);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register custom TrunkPlacerType", e);
        }
//        try {
//            Constructor<TrunkPlacerType> constructor =
//                    TrunkPlacerType.class.getDeclaredConstructor(Codec.class);
//            constructor.setAccessible(true);
//
//            CUSTOM_MEGA_TRUNK_PLACER = constructor.newInstance(CustomMegaTrunkPlacer.CODEC);
//
//            // Inscrire dans le registry vanilla
//            Registry.register(Registry.TRUNK_REPLACER,
//                    new ResourceLocation("testmod", "custom_mega_trunk_placer"),
//                    CUSTOM_MEGA_TRUNK_PLACER);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to register custom MegaTrunkPlacerType", e);
//        }
    }
}
