package net.zadezapper.vibral.mixin;

import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EndermanEntity.class, priority = 2048)
public abstract class EndermanEntityMixin {
    @Inject(at = @At("HEAD"), method = "isPlayerStaring", cancellable = true)
    void isPlayerStaring(PlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (player != null) {
            if (StealthHelper.getFullArmorObscuringEnchantmentLevel(player) >= 2) {
                callbackInfoReturnable.setReturnValue(false);
                callbackInfoReturnable.cancel();
            }
        }
    }
}
