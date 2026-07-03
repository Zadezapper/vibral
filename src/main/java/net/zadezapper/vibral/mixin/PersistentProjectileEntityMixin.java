package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = PersistentProjectileEntity.class, priority = 4096)
public abstract class PersistentProjectileEntityMixin {

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/projectile/PersistentProjectileEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"
        ),
        method = "onEntityHit"
    )
    private boolean shouldPlaySound(PersistentProjectileEntity instance, SoundEvent soundEvent, float volume, float pitch, EntityHitResult entityHitResult) {
        return !(StealthHelper.isWearingFullVibralArmorSet(entityHitResult.getEntity()) || (entityHitResult.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(VibralEffects.SILENCE)));
    }
}
