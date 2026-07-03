package net.zadezapper.vibral.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.zadezapper.vibral.block.entity.EchoBlockEntity;
import net.zadezapper.vibral.block.entity.VibralBlockEntities;
import org.jetbrains.annotations.Nullable;

public class EchoBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final MapCodec<EchoBlock> CODEC = EchoBlock.createCodec(EchoBlock::new);
    public EchoBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EchoBlockEntity(pos, state);
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
        World world,
        BlockState state,
        BlockEntityType<T> type) {
        return validateTicker(
            type,
            VibralBlockEntities.ECHO_BE,
            EchoBlockEntity::tick
        );
    }

    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
}
