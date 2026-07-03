package net.zadezapper.vibral.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.sound.VibralSoundEvents;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Entity.class, priority = 2048)
public abstract class EntityMixin {

    @Unique
    public Entity entity = ((Entity)(Object)this);

    @Inject(at = @At("HEAD"), method = "getSwimSound", cancellable = true)
    public void getSwimSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(VibralSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getSplashSound", cancellable = true)
    public void getSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(VibralSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getHighSpeedSplashSound", cancellable = true)
    public void getHighSpeedSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(VibralSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isSilent", cancellable = true)
    public void isSilent(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(true);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound", cancellable = true)
    public void playSound(SoundEvent sound, float volume, float pitch, CallbackInfo callbackInfo) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || (entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
                callbackInfo.cancel();
            }
        }
    }
}