package net.hamnd.testmod.tileentity;

import net.hamnd.testmod.entity.ModEntityTypes;
import net.hamnd.testmod.entity.custom.DaisyStatueEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DaisyStatueTile extends TileEntity {

    private List<BlockPos> positionsList = new ArrayList<>();
    public DaisyStatueEntity entity = null;
    public boolean show = false;

    public DaisyStatueTile() {
        super(ModTileEntities.DAISY_STATUE_TILE.get());
    }

    public void setPositionsList(List<BlockPos> list) {
        this.positionsList = new ArrayList<>(list);
        markDirty(); // indique que les données ont changé
    }

    public List<BlockPos> getPositionsList() {
        return positionsList;
    }

    public void createEntity() {
        this.entity = new DaisyStatueEntity(ModEntityTypes.DAISY_STATUE_ENTITY.get(), this.getWorld());
        this.entity.setPositionAndRotation(this.getPos().getX() + 0.5f, this.getPos().getY(), this.getPos().getZ() + 0.5f, 0, 0f);
        this.entity.setSilent(true);
        this.entity.setBoundingBox(new AxisAlignedBB(0,0,0,0,0,0));
        markDirty(); // indique que les données ont changé
    }

    public DaisyStatueEntity getEntity() {
        return this.entity;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT tag = super.getUpdateTag();
        tag.putBoolean("Show", this.show);
        return tag;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        this.show = tag.getBoolean("Show");
    }

    // Sauvegarde dans le NBT
    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        ListNBT listNBT = new ListNBT();
        for (BlockPos pos : positionsList) {
            CompoundNBT posNBT = new CompoundNBT();
            posNBT.putInt("x", pos.getX());
            posNBT.putInt("y", pos.getY());
            posNBT.putInt("z", pos.getZ());
            listNBT.add(posNBT);
        }
        compound.put("structurePositions", listNBT);
        compound.putBoolean("Show", show);
        return compound;
    }

    // Lecture depuis le NBT
    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        positionsList.clear();
        if (compound.contains("structurePositions", 9)) { // 9 = list tag
            ListNBT listNBT = compound.getList("structurePositions", 10); // 10 = compound tag
            for (int i = 0; i < listNBT.size(); i++) {
                CompoundNBT posNBT = listNBT.getCompound(i);
                int x = posNBT.getInt("x");
                int y = posNBT.getInt("y");
                int z = posNBT.getInt("z");
                positionsList.add(new BlockPos(x, y, z));
            }
        }
        this.show = compound.getBoolean("Show");
    }

    // Envoie le paquet vers le client
    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT tag = new CompoundNBT();
        this.write(tag);
        return new SUpdateTileEntityPacket(this.pos, 0, tag);
    }

    // Reçoit le paquet côté client
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.read(this.getBlockState(), pkt.getNbtCompound());
        System.out.println("Client received show = " + this.show);
    }
}