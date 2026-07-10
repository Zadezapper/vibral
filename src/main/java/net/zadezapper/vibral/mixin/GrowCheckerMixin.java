package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.SculkVeinBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.zadezapper.vibral.block.VibralBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LichenGrower.GrowChecker.class, priority = 2048)
public interface GrowCheckerMixin {
    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/WorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"
            ),
            method = "place"
    )
    private boolean setBlockState(WorldAccess instance, BlockPos pos, BlockState state, int flags, Operation<Boolean> original) {
        if (Math.random() < 0.0025) {
            BlockState newState = VibralBlocks.RAW_VIBRAL.getDefaultState();
            for (Direction direction : Direction.values()) {
                newState = newState.with(SculkVeinBlock.getProperty(direction), state.get(SculkVeinBlock.getProperty(direction)));
            }
            state = newState;
        }
        return original.call(instance, pos, state, flags);
    }
}
