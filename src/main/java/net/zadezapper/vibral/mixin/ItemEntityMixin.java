package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.util.math.Vec3d;
import net.zadezapper.vibral.accessor.FollowingItem;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemEntity.class, priority = 4096)
public abstract class ItemEntityMixin implements FollowingItem {

    @Unique @Nullable private Entity vibral$targetEntity = null;
    @Unique private int vibral$followTicks;
    @Unique private float vibral$followStrength;
    @Unique private float vibral$followDistance;

    @Override
    public void vibral$setTargetEntity(Entity target) {
        vibral$targetEntity = target;
    }
    @Override
    public Entity vibral$getTargetEntity() {
        return vibral$targetEntity;
    }
    @Override
    public void vibral$setFollowTicks(int ticks) {
        vibral$followTicks = ticks;
    }
    @Override
    public int vibral$getFollowTicks() {
        return vibral$followTicks;
    }
    @Override
    public void vibral$setFollowStrength(float strength) {
        vibral$followStrength = strength;
    }
    @Override
    public float vibral$getFollowStrength() {
        return vibral$followStrength;
    }
    @Override
    public void vibral$setFollowDistance(float distance) {
        vibral$followDistance = distance;
    }
    @Override
    public float vibral$getFollowDistance() {
        return vibral$followDistance;
    }

    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo callbackInfo) {
        ItemEntity thisItem = ((ItemEntity)(Object)this);
        if (vibral$targetEntity != null) {
            if (vibral$followTicks-- > 0) {
                Vec3d movementDirection = new Vec3d(this.vibral$targetEntity.getX() - thisItem.getX(), this.vibral$targetEntity.getY() + (double) this.vibral$targetEntity.getStandingEyeHeight() / 2.0 - thisItem.getY(), this.vibral$targetEntity.getZ() - thisItem.getZ());
                double distance = movementDirection.length();
                if (distance < vibral$followDistance) {
                    double e = 1.0 - Math.sqrt(distance) / 8.0;
                    thisItem.setVelocity(thisItem.getVelocity().add(movementDirection.normalize().multiply(e * e * vibral$followStrength)));
                    ((EntityAccessor)thisItem).setVelocityDirty(true);
                }
            } else {
                vibral$targetEntity = null;
            }
        }
    }

    @WrapOperation(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/Block;getSlipperiness()F"
        ),
        method = "tick"
    )
    private float getSlipperiness(Block instance, Operation<Float> original) {
        if (vibral$targetEntity != null && vibral$followTicks-- > 0) {
            return Math.max(0.8f, original.call(instance));
        }
        return original.call(instance);
    }
}