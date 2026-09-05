package net.hamnd.testmod;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;

public class TreeSaveData extends WorldSavedData {
    private static final String NAME = "testmod_trees";
    private final Map<Integer, TreeData> trees = new HashMap<>();
    private static int nextId = 0;

    public TreeSaveData() { super(NAME); }

    public static TreeSaveData get(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(TreeSaveData::new, NAME);
    }

    public static int getNewTreeDataIndex() {
        nextId++;
        TestMod.LOGGER.info("nextid = " + nextId);
        return nextId;
    }
    
    public void registerNewTree(int id, TreeData treeData) {
        trees.put(id, treeData);
        setDirty();
    }

    public TreeData getTreeData(int id) {
        return trees.get(id);
    }

    public void removeTreeData(int id) {
        trees.remove(id);
        setDirty();
    }

    @Override
    public void load(CompoundNBT nbt) {
        nextId = nbt.getInt("TreeId");
        ListNBT listNBT = nbt.getList("TreesList", Constants.NBT.TAG_COMPOUND);
        listNBT.forEach((el) -> {
            CompoundNBT compoundNBT = (CompoundNBT) el;
            TreeData treeData = new TreeData();
            treeData.load(compoundNBT.getCompound("TreeData"));
            trees.put(compoundNBT.getInt("TreeId"), treeData);
        });
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        ListNBT listNBT = new ListNBT();
        trees.forEach((treeID, treeData) -> {
            CompoundNBT compoundnbt = new CompoundNBT();
            compoundnbt.putInt("TreeId", treeID);
            compoundnbt.put("TreeData", treeData.save());
            listNBT.add(compoundnbt);
        });
        nbt.put("TreesList", listNBT);
        nbt.putInt("TreeId", nextId);
        return nbt;
    }
}