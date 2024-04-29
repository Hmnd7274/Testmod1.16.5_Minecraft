package net.hamnd.testmod.world.gen;

import net.hamnd.testmod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraftforge.common.util.Lazy;

public enum Oretype {

    ZIRCON(Lazy.of(ModBlocks.ZIRCON_ORE), 8, 25, 50);

    private final Lazy<Block> block;
    private final int maxVeinSize;
    private final int minHeight;
    private final int maxHeight;

    Oretype(Lazy<Block> block, int maxVeinSize, int minHeight, int maxHeight) {
        this.block = block;
        this.maxVeinSize = maxVeinSize;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
    }

    public Lazy<Block> getBlock() {
        return block;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getMaxVeinSize() {
        return maxVeinSize;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public static Oretype get(Block block) {
        for(Oretype ore : values()) {
            if(block == ore.block) {
                return ore;
            }
        }
        return null;
    }
}
