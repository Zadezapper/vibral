package net.zadezapper.vibral.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public class VibralPanelBlock extends Block implements Waterloggable {
    public static final MapCodec<VibralPanelBlock> CODEC = createCodec(VibralPanelBlock::new);
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
    private static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES;
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final VoxelShape UP_SHAPE = Block.createCuboidShape(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape WEST_SHAPE = Block.createCuboidShape(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private static final Map<Direction, VoxelShape> SHAPES_FOR_DIRECTIONS =
            Util.make(Maps.newEnumMap(Direction.class), shapes -> {
                shapes.put(Direction.NORTH, SOUTH_SHAPE);
                shapes.put(Direction.EAST, WEST_SHAPE);
                shapes.put(Direction.SOUTH, NORTH_SHAPE);
                shapes.put(Direction.WEST, EAST_SHAPE);
                shapes.put(Direction.UP, UP_SHAPE);
                shapes.put(Direction.DOWN, DOWN_SHAPE);
            });
    private final ImmutableMap<BlockState, VoxelShape> shapes;
    private final boolean hasAllHorizontalDirections;
    private final boolean canMirrorX;
    private final boolean canMirrorZ;

    public VibralPanelBlock(Settings settings) {
        super(settings);
        this.setDefaultState(withAllDirections(this.stateManager).with(WATERLOGGED, false));
        this.shapes = this.getShapesForStates(VibralPanelBlock::getShapeForState);
        this.hasAllHorizontalDirections = Direction.Type.HORIZONTAL.stream().allMatch(this::canHaveDirection);
        this.canMirrorX = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.X).filter(this::canHaveDirection).count() % 2L == 0L;
        this.canMirrorZ = Direction.Type.HORIZONTAL.stream().filter(Direction.Axis.Z).filter(this::canHaveDirection).count() % 2L == 0L;
    }

    @Override
    public MapCodec<VibralPanelBlock> getCodec() {
        return CODEC;
    }

    protected boolean canHaveDirection(Direction direction) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        for (Direction direction : DIRECTIONS) {
            if (canHaveDirection(direction)) {
                builder.add(getProperty(direction));
            }
        }
        builder.add(WATERLOGGED);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(WATERLOGGED)) {
            world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }
        if (!hasAnyDirection(state)) {
            return Blocks.AIR.getDefaultState();
        }
        if (hasDirection(state, direction)
                && !canAttachTo(world, direction, neighborPos, neighborState)) {
            BlockState newState = state.with(getProperty(direction), false);
            return hasAnyDirection(newState) ? newState : Blocks.AIR.getDefaultState();
        }
        return state;
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        boolean foundFace = false;
        for (Direction direction : DIRECTIONS) {
            if (hasDirection(state, direction)) {
                BlockPos offset = pos.offset(direction);
                if (!canAttachTo(world, direction, offset, world.getBlockState(offset))) {
                    return false;
                }
                foundFace = true;
            }
        }
        return foundFace;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return shapes.get(state);
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        return context.getStack().isOf(this.asItem()) && Arrays.stream(DIRECTIONS).anyMatch(direction -> !hasDirection(state, direction));
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        BlockState existing = world.getBlockState(pos);
        return Arrays.stream(context.getPlacementDirections()).map(direction -> withDirection(existing, world, pos, direction)).filter(java.util.Objects::nonNull).findFirst().orElse(null);
    }

    @Nullable
    public BlockState withDirection(BlockState state, BlockView world, BlockPos pos, Direction direction) {
        if (!canAttachWithDirection(world, state, pos, direction)) {
            return null;
        }
        BlockState result;
        if (state.isOf(this)) {
            result = state;
        } else if (state.getFluidState().isEqualAndStill(Fluids.WATER)) {
            result = getDefaultState().with(WATERLOGGED, true);
        } else {
            result = getDefaultState();
        }
        return result.with(getProperty(direction), true);
    }

    public boolean canAttachWithDirection(BlockView world, BlockState state, BlockPos pos, Direction direction) {
        if (!canHaveDirection(direction)) {
            return false;
        }
        if (state.isOf(this) && hasDirection(state, direction)) {
            return false;
        }
        BlockPos offset = pos.offset(direction);
        return canAttachTo(world, direction, offset, world.getBlockState(offset));
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return hasAllHorizontalDirections ? mirror(state, rotation::rotate) : state;
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        if (mirror == BlockMirror.FRONT_BACK && !canMirrorX) {
            return state;
        }
        if (mirror == BlockMirror.LEFT_RIGHT && !canMirrorZ) {
            return state;
        }
        return mirror(state, mirror::apply);
    }

    private BlockState mirror(BlockState state, Function<Direction, Direction> transform) {
        BlockState result = state;
        for (Direction direction : DIRECTIONS) {
            if (canHaveDirection(direction)) {
                result = result.with(
                        getProperty(transform.apply(direction)),
                        state.get(getProperty(direction))
                );
            }
        }
        return result;
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    protected boolean isTransparent(BlockState state, BlockView world, BlockPos pos) {
        return state.getFluidState().isEmpty();
    }

    public static BooleanProperty getProperty(Direction direction) {
        return FACING_PROPERTIES.get(direction);
    }

    public static boolean hasDirection(BlockState state, Direction direction) {
        BooleanProperty property = getProperty(direction);
        return state.contains(property) && state.get(property);
    }

    private static boolean hasAnyDirection(BlockState state) {
        return Arrays.stream(DIRECTIONS).anyMatch(direction -> hasDirection(state, direction));
    }

    private static BlockState withAllDirections(StateManager<Block, BlockState> stateManager) {
        BlockState state = stateManager.getDefaultState();
        for (BooleanProperty property : FACING_PROPERTIES.values()) {
            if (state.contains(property)) {
                state = state.with(property, false);
            }
        }
        return state;
    }

    private static VoxelShape getShapeForState(BlockState state) {
        VoxelShape shape = VoxelShapes.empty();
        for (Direction direction : DIRECTIONS) {
            if (hasDirection(state, direction)) {
                shape = VoxelShapes.union(shape, SHAPES_FOR_DIRECTIONS.get(direction));
            }
        }
        return shape.isEmpty() ? VoxelShapes.fullCube() : shape;
    }

    public static boolean canAttachTo(BlockView world, Direction direction, BlockPos pos, BlockState state) {
        return Block.isFaceFullSquare(state.getSidesShape(world, pos), direction.getOpposite()) || Block.isFaceFullSquare(state.getCollisionShape(world, pos), direction.getOpposite());
    }
}
