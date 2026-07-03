package net.zadezapper.vibral.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MobEntity.class, priority = 2048)
public abstract class MobEntityMixin {

    @Inject(at = @At("HEAD"), method = "setTarget", cancellable = true)
    public void setTarget(LivingEntity target, CallbackInfo callbackInfo) {
        if (target != null && StealthHelper.getFullArmorObscuringEnchantmentLevel(target) >= 2 && target.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            callbackInfo.cancel();
        }
    }
}