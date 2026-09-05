package net.hamnd.testmod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;

public class SpiritualLogBlock extends RotatedPillarBlock implements ITileEntityProvider {
    
    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader p_196283_1_) {
        return new SpiritualLogTileEntity();
    }

    public enum TreeParts implements IStringSerializable {
        UK("unknown"),
        TRUNK("trunk"),
        BRANCH("branch"),
        ROOT("root");

        private final String name;

        TreeParts(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    public static final EnumProperty<TreeParts> PART = EnumProperty.create("part", TreeParts.class);
    public SpiritualLogBlock(Properties p_i48339_1_) {
        super(p_i48339_1_);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .setValue(PART, TreeParts.UK));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART);
    }

    @Override
    public void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        super.playerWillDestroy(world, pos, state, player);
        if (world.isClientSide) return;
        if (state.getValue(PART) != TreeParts.TRUNK) return;
        TestMod.sendMessage("Spiritual Log Broken");
        SpiritualLogTileEntity tileEntity = (SpiritualLogTileEntity) world.getBlockEntity(pos);
        if (tileEntity == null || tileEntity.treeId == -1) return;
        
        TreeData treeData = TreeSaveData.get((ServerWorld) world).getTreeData(tileEntity.treeId);
        int remaining = treeData.remainingPerLayer.get(pos.getY());
        TestMod.sendMessage("from " + remaining + " to " + (remaining - 1));
        if (remaining != 1)
            treeData.remainingPerLayer.put(pos.getY(), remaining - 1);
        else 
            treeData.triggerCutscene(world, pos);
    }

    public static class SpiritualLogTileEntity extends TileEntity {

        private int treeId = -1;

        public SpiritualLogTileEntity() {
            super(ModTileEntities.SPIRITUAL_LOG_TILE.get());
        }
        
        public void setTreeId(int id) {
            this.treeId = id;
            setChanged();
        }

        @Override
        public CompoundNBT save(CompoundNBT nbt) {
            super.save(nbt);
            nbt.putInt("treeId", treeId);
            return nbt;
        }
        @Override
        public void load(BlockState state, CompoundNBT nbt) {
            super.load(state, nbt);
            treeId = nbt.getInt("treeId");
        }
    }
}
