package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.zadezapper.vibral.item.ModItems;
import net.zadezapper.vibral.sound.ModSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 4096)
public abstract class PlayerEntityMixin {
    @Unique
    public PlayerEntity entity = ((PlayerEntity)(Object)this);

    @Inject(at = @At("HEAD"), method = "getSwimSound", cancellable = true)
    public void getSwimSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity)) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getSplashSound", cancellable = true)
    public void getSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity)) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getHighSpeedSplashSound", cancellable = true)
    public void getHighSpeedSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity)) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getMoveEffect", cancellable = true)
    public void getMoveEffect(CallbackInfoReturnable<Entity.MoveEffect> callbackInfoReturnable) {
        if (isWearingFullVibralArmorSet(entity)) {
            callbackInfoReturnable.setReturnValue(Entity.MoveEffect.NONE);
            callbackInfoReturnable.cancel();
        }
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        ),
        method = "eatFood"
    )
    private boolean skip(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !isWearingFullVibralArmorSet(entity);
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
            ),
            method = "attack"
    )
    private boolean skip2(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !isHoldingVibralTool(entity);
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V"
            ),
            method = "attack"
    )
    private boolean skip3(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category) {
        return !isHoldingVibralTool(entity);
    }

    @Unique
    private boolean isWearingFullVibralArmorSet(Entity entity) {
        if (entity instanceof LivingEntity) {
            return (
                    ((LivingEntity) entity).getEquippedStack(EquipmentSlot.HEAD).isOf(ModItems.VIBRAL_HELMET)
                    && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.CHEST).isOf(ModItems.VIBRAL_CHESTPLATE)
                    && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.LEGS).isOf(ModItems.VIBRAL_LEGGINGS)
                    && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).isOf(ModItems.VIBRAL_BOOTS)
            );
        } else {
            return false;
        }
    }

    @Unique
    private boolean isHoldingVibralTool(Entity entity) {
        if (entity instanceof LivingEntity) {
            return (
                    ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.VIBRAL_SWORD)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.VIBRAL_PICKAXE)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.VIBRAL_AXE)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.VIBRAL_SHOVEL)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(ModItems.VIBRAL_HOE)
            );
        } else {
            return false;
        }
    }
}
