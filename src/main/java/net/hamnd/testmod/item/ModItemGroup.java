package net.hamnd.testmod.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModItemGroup {
    public static final ItemGroup TEST_GROUP = new ItemGroup("testModTab") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(ModItems.ZIRCON.get());
        }
    };
}