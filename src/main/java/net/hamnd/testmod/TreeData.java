package net.hamnd.testmod;

import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class TreeData {
    public final Map<Integer, Integer> remainingPerLayer = new HashMap<>(); // layerIndex -> count restant
    public boolean triggered = false;
    
    public void triggerCutscene(BlockPos scenePosition) {
//        if (triggered) return;
        triggered = true;
        CutsceneHandler.triggerCutscene(scenePosition);
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