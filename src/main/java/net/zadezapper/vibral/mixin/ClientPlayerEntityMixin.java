package net.zadezapper.vibral.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientPlayerEntity.class, priority = 4096)
public abstract class ClientPlayerEntityMixin {
    @Unique
    public PlayerEntity entity = ((PlayerEntity)(Object)this);

    @Inject(at = @At("HEAD"), method = "shouldSpawnSprintingParticles", cancellable = true)
    private void shouldSpawnSprintingParticles(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (StealthHelper.getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            callbackInfoReturnable.setReturnValue(false);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound", cancellable = true)
    private void playSound(SoundEvent sound, float volume, float pitch, CallbackInfo callbackInfo) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(VibralEffects.SILENCE)) {
                callbackInfo.cancel();
            }
        }
    }
}
