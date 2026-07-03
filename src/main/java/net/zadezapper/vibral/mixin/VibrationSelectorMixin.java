package net.zadezapper.vibral.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.event.listener.Vibration;
import net.minecraft.world.event.listener.VibrationSelector;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = VibrationSelector.class, priority = 2048)
public abstract class VibrationSelectorMixin {
    @Inject(at = @At("HEAD"), method = "shouldSelect", cancellable = true)
    private void shouldSelect(Vibration vibration, long tick, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Entity vibrationSource = vibration.entity();
        if (vibrationSource != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(vibrationSource) || (vibrationSource instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(false);
                callbackInfoReturnable.cancel();
            }
        }
    }
}