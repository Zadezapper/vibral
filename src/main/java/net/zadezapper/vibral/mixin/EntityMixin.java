package net.zadezapper.vibral.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.zadezapper.vibral.effect.ModEffects;
import net.zadezapper.vibral.item.ModItems;
import net.zadezapper.vibral.sound.ModSoundEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 2048)
public abstract class EntityMixin {

    @Final
    @Shadow
    protected DataTracker dataTracker;

    @Unique
    public Entity entity = ((Entity)(Object)this);

    @Inject(at = @At("HEAD"), method = "getSwimSound", cancellable = true)
    public void getSwimSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getSplashSound", cancellable = true)
    public void getSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getHighSpeedSplashSound", cancellable = true)
    public void getHighSpeedSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    /*
    @Inject(at = @At("HEAD"), method = "getMoveEffect", cancellable = true)
    public void getMoveEffect(CallbackInfoReturnable<Entity.MoveEffect> callbackInfoReturnable) {
        if (isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
            callbackInfoReturnable.setReturnValue(Entity.MoveEffect.NONE);
            callbackInfoReturnable.cancel();
        }
    }
    */

    @Inject(at = @At("HEAD"), method = "isSilent", cancellable = true)
    public void isSilent(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(true);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound", cancellable = true)
    public void playSound(SoundEvent sound, float volume, float pitch, CallbackInfo callbackInfo) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
                callbackInfo.cancel();
            }
        }
    }

    @Unique
    private boolean isWearingFullVibralArmorSet(Entity entity) {
        if (entity instanceof LivingEntity) {
            ItemStack headItemStack = ((LivingEntity) entity).getEquippedStack(EquipmentSlot.HEAD);
            ItemStack chestItemStack = ((LivingEntity) entity).getEquippedStack(EquipmentSlot.CHEST);
            ItemStack legsItemStack = ((LivingEntity) entity).getEquippedStack(EquipmentSlot.LEGS);
            ItemStack feetItemStack = ((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET);


            return (headItemStack.isOf(ModItems.VIBRAL_HELMET)
                    && chestItemStack.isOf(ModItems.VIBRAL_CHESTPLATE)
                    && legsItemStack.isOf(ModItems.VIBRAL_LEGGINGS)
                    && feetItemStack.isOf(ModItems.VIBRAL_BOOTS));
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