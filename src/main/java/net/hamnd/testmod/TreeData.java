package net.hamnd.testmod;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TreeData {
    public final Map<Integer, Integer> remainingPerLayer = new HashMap<>(); // layerIndex -> count restant
    public Set<Tuple<BlockPos, Block>> cutsceneBlocks;
    public boolean triggered = false;
    
    public void triggerCutscene(World world, BlockPos scenePosition) {
        if (triggered) return;
        triggered = true;
        CutsceneHandler.triggerCutscene(world, scenePosition, cutsceneBlocks);
    }
    
    public CompoundNBT save() {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putBoolean("Triggered", triggered);
        compoundNBT.put("Data", saveRemainingPerLayer());
        return compoundNBT;
    }

    private ListNBT saveRemainingPerLayer() {
        ListNBT listNBT = new ListNBT();
        remainingPerLayer.forEach((index, count) -> {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("LayerIndex", index);
            compoundNBT.putInt("Count", count);
            listNBT.add(compoundNBT);
        });
        return listNBT;
    }

    public void load(CompoundNBT nbt) {
        triggered = nbt.getBoolean("Triggered");
        loadRemainingPerLayer(nbt.getList("Data", Constants.NBT.TAG_COMPOUND));
    }
    
    private void loadRemainingPerLayer(ListNBT listNBT) {
        listNBT.forEach((el) -> {
            CompoundNBT compoundNBT = ((CompoundNBT) el);
            remainingPerLayer.put(compoundNBT.getInt("LayerIndex"), compoundNBT.getInt("Count"));
        });
    }
}