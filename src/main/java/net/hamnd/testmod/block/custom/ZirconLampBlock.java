package net.hamnd.testmod.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class ZirconLampBlock extends Block {
    public static final BooleanProperty LIT = BooleanProperty.create(("lit"));

    public ZirconLampBlock(Properties properties) {
        super(properties);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResultType use(BlockState blockState, World world, BlockPos blockPos,
                                PlayerEntity playerEntity, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if(!world.isClientSide() && hand == Hand.MAIN_HAND) {
            world.setBlock(blockPos, blockState.cycle(LIT), 3);
        }

        return super.use(blockState, world, blockPos, playerEntity, hand, blockRayTraceResult);
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
