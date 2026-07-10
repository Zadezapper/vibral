package net.zadezapper.vibral.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.SculkSpreadManager;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class RawVibralBlock extends MultifaceGrowthBlock implements Waterloggable, SculkSpreadable {
    public static final MapCodec<RawVibralBlock> CODEC = createCodec(RawVibralBlock::new);
    private static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private final LichenGrower grower = new LichenGrower(this);

    public RawVibralBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(WATERLOGGED, false));
    }

    @Override
    public MapCodec<RawVibralBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
        BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return super.canReplace(state, context);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    @Override
    public LichenGrower getGrower() {
        return this.grower;
    }

    @Override
    public int spread(SculkSpreadManager.Cursor cursor, WorldAccess world, BlockPos catalystPos, Random random, SculkSpreadManager spreadManager, boolean shouldConvertToBlock) {
        BlockState blockState;
        for (Direction offsetDirection : Direction.values()) {
            BlockPos pos = cursor.getPos().offset(offsetDirection);
            BlockState originalState = world.getBlockState(pos);

            if (originalState.getBlock() == Blocks.SCULK_VEIN) {
                blockState = VibralBlocks.RAW_VIBRAL.getDefaultState();
                for (Direction checkFaceDirection : Direction.values()) {
                    blockState = blockState.with(SculkVeinBlock.getProperty(checkFaceDirection), originalState.get(SculkVeinBlock.getProperty(checkFaceDirection)));
                }
                if (world.setBlockState(pos, blockState, Block.NOTIFY_ALL)) {
                    world.setBlockState(cursor.getPos(), originalState.with(WATERLOGGED, !world.getBlockState(cursor.getPos()).getFluidState().isEmpty()), Block.NOTIFY_ALL);
                    return cursor.getCharge() -1;
                }
            }
        }
        return random.nextInt(spreadManager.getSpreadChance()) == 0 ? MathHelper.floor(cursor.getCharge() * 0.5F) : cursor.getCharge();
    }
}
