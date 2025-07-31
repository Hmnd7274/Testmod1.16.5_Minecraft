package net.hamnd.testmod.block.custom;

import mcp.MethodsReturnNonnullByDefault;
import net.hamnd.testmod.tileentity.DaisyStatueTile;
import net.hamnd.testmod.utils.Calculator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DaisyStatueBlock extends Block {
    public static final EnumProperty<TripleXTripleXTripleBlockPart> PART = EnumProperty.create("part", TripleXTripleXTripleBlockPart.class);
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;

    public DaisyStatueBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState()
                .with(PART, TripleXTripleXTripleBlockPart.BOTTOM_CENTER)
                .with(FACING, Direction.NORTH));
    }

    public enum TripleXTripleXTripleBlockPart implements IStringSerializable {
        BOTTOM_CENTER,
        BOTTOM_NORTH, BOTTOM_SOUTH, BOTTOM_EAST, BOTTOM_WEST,
        BOTTOM_NORTH_EAST, BOTTOM_NORTH_WEST, BOTTOM_SOUTH_EAST, BOTTOM_SOUTH_WEST,

        MIDDLE_CENTER,
        MIDDLE_NORTH, MIDDLE_SOUTH, MIDDLE_EAST, MIDDLE_WEST,
        MIDDLE_NORTH_EAST, MIDDLE_NORTH_WEST, MIDDLE_SOUTH_EAST, MIDDLE_SOUTH_WEST,

        TOP_CENTER,
        TOP_NORTH, TOP_SOUTH, TOP_EAST, TOP_WEST,
        TOP_NORTH_EAST, TOP_NORTH_WEST, TOP_SOUTH_EAST, TOP_SOUTH_WEST;

        @Override
        @MethodsReturnNonnullByDefault
        public String getString() {
            return name().toLowerCase();
        }
    }

    public List<BlockPos> getStructurePositions(BlockPos origin, Direction facing) {
        List<BlockPos> positions = new ArrayList<>();
        // Crée une cube 3x3x3 autour de `origin`, orienté selon `facing`
        for (int y = 0; y < 3; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos offset = origin.add(rotateOffset(dx, dz, facing)).up(y);
                    positions.add(offset);
                }
            }
        }
        return positions;
    }

    public List<BlockPos> getStructurePositionsFromOrigin(BlockPos origin) {
        List<BlockPos> positions = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos offset = origin.add(dx, y, dz);
                    positions.add(offset);
                }
            }
        }
        return positions;
    }

    private BlockPos rotateOffset(int dx, int dz, Direction facing) {
        switch (facing) {
            case SOUTH: return new BlockPos(-dx, 0, -dz);
            case WEST:  return new BlockPos(dz, 0, -dx);
            case EAST:  return new BlockPos(-dz, 0, dx);
            case NORTH:
            default:    return new BlockPos(dx, 0, dz);
        }
    }

    public TripleXTripleXTripleBlockPart getPartFromOffset(BlockPos relPos, Direction facing) {
        int dx = relPos.getX();
        int dy = relPos.getY();
        int dz = relPos.getZ();

        // Inverser la rotation pour revenir au repère "face au nord"
        BlockPos rotated = rotateOffset(dx, dz, facing.getOpposite());
        dx = rotated.getX();
        dz = rotated.getZ();

        StringBuilder name = new StringBuilder();

        // Y-axis
        if (dy == 0) name.append("BOTTOM_");
        else if (dy == 1) name.append("MIDDLE_");
        else if (dy == 2) name.append("TOP_");

        // Horizontal (XZ)
        if (dx == 0 && dz == 0) name.append("CENTER");
        else {
            if (dz == -1) name.append("NORTH_");
            else if (dz == 1) name.append("SOUTH_");

            if (dx == -1) name.append("WEST");
            else if (dx == 1) name.append("EAST");
        }

        // Enlever dernier "_" si nécessaire
        String finalName = name.toString().endsWith("_") ? name.substring(0, name.length() - 1) : name.toString();

        return TripleXTripleXTripleBlockPart.valueOf(finalName);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockPos pos = context.getPos();
        IWorld world = context.getWorld();
        List<BlockPos> positions = getStructurePositions(pos, context.getPlacementHorizontalFacing());
        for (BlockPos p : positions) {
            if (!world.getBlockState(p).isReplaceable(context)) return null;
        }
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing()).with(PART, TripleXTripleXTripleBlockPart.BOTTOM_CENTER);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
        TripleXTripleXTripleBlockPart part = state.get(PART);
        if (part == TripleXTripleXTripleBlockPart.BOTTOM_CENTER) {
            for (BlockPos blocksPos : getStructurePositionsFromOrigin(pos)) {
                if (!world.isAirBlock(blocksPos))return false;
            }
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (world.isRemote) return; // ⛔ ne rien faire côté client

        Direction facing = state.get(FACING);
        List<BlockPos> blocksPositions = getStructurePositions(pos, facing);

        for (BlockPos p : blocksPositions) {
            TripleXTripleXTripleBlockPart part = getPartFromOffset(p.subtract(pos), facing);

            world.setBlockState(p, this.getDefaultState().with(FACING, facing).with(PART, part), 3);

            TileEntity tile = world.getTileEntity(p);
            assert tile != null;

            ((DaisyStatueTile) tile).setPositionsList(blocksPositions);
            if (part == TripleXTripleXTripleBlockPart.BOTTOM_CENTER) ((DaisyStatueTile) tile).show = true;
        }
    }

    @Override
    public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() == newState.getBlock()) return;

        TileEntity tile = world.getTileEntity(pos);
        assert tile != null;
        List<BlockPos> positionsList = ((DaisyStatueTile) tile).getPositionsList();

        for (BlockPos p : positionsList) {
            BlockState s = world.getBlockState(p);
            if (s.getBlock() == this) {
                world.destroyBlock(p, false);
            }
        }

        super.onReplaced(state, world, pos, newState, isMoving);
    }


    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            DaisyStatueTile tile = (DaisyStatueTile) world.getTileEntity(pos);
            assert tile != null;

            tile.show = !tile.show;
            tile.markDirty();
            world.notifyBlockUpdate(pos, state, state, 2);
            Calculator.sendMsg(Boolean.toString(tile.show));

            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS; // ⛔ ne rien faire côté client
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(PART, FACING);
    }

    // Pour que le bloc ne soit pas remplaçable facilement (ex: herbe)
    @Override
    public boolean isReplaceable(BlockState state, BlockItemUseContext context) {
        return false;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new DaisyStatueTile();
    }

    @Override
    public PushReaction getPushReaction(BlockState state) {
        return PushReaction.BLOCK;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, IBlockReader reader, BlockPos pos) {
        return true;
    }
}