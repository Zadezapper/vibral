package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.sound.VibralSoundEvents;
import net.zadezapper.vibral.util.StealthHelper;
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
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(VibralEffects.SILENCE)) {
                callbackInfoReturnable.setReturnValue(VibralSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getSplashSound", cancellable = true)
    public void getSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(VibralEffects.SILENCE)) {
                callbackInfoReturnable.setReturnValue(VibralSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getHighSpeedSplashSound", cancellable = true)
    public void getHighSpeedSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (StealthHelper.isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(VibralEffects.SILENCE)) {
                callbackInfoReturnable.setReturnValue(VibralSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @WrapOperation(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
        ),
        method = "attack"
    )
    private boolean damage(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingEntity && instance instanceof LivingEntity && (StealthHelper.isHoldingVibralTool(livingEntity) || livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
            ((LivingEntity)instance).addStatusEffect(new StatusEffectInstance(
                VibralEffects.SILENCE,
                60,
                0,
                true,
                true
            ));
        }
        return original.call(instance, source, amount);
    }

    @WrapOperation(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
        ),
        method = "attack"
    )
    private boolean damage2(LivingEntity instance, DamageSource source, float amount, Operation<Boolean> original) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingEntity && (StealthHelper.isHoldingVibralTool(livingEntity) || livingEntity.hasStatusEffect(VibralEffects.SILENCE))) {
            instance.addStatusEffect(new StatusEffectInstance(
                VibralEffects.SILENCE,
                60,
                0,
                true,
                true
            ));
        }
        return original.call(instance, source, amount);
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        ),
        method = "eatFood"
    )
    private boolean shouldPlaySound(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !(StealthHelper.isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(VibralEffects.SILENCE));
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        ),
        method = "attack"
    )
    private boolean shouldPlaySound1(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !(StealthHelper.isHoldingVibralTool(entity) || entity.hasStatusEffect(VibralEffects.SILENCE));
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V"
        ),
        method = "attack"
    )
    private boolean shouldPlaySound2(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category) {
        return !(StealthHelper.isHoldingVibralTool(entity) || entity.hasStatusEffect(VibralEffects.SILENCE));
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"
        ),
        method = "attack"
    )
    private boolean shouldSpawnSweepAttackParticles(PlayerEntity instance) {
        return !(StealthHelper.getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"
        ),
        method = "attack"
    )
    private boolean shouldAddCritParticles(PlayerEntity instance, Entity target) {
        return !(StealthHelper.getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerEntity;addEnchantedHitParticles(Lnet/minecraft/entity/Entity;)V"
        ),
        method = "attack"
    )
    private boolean shouldAddEnchantedHitParticles(PlayerEntity instance, Entity target) {
        return !(StealthHelper.getFullArmorObscuringEnchantmentLevel(instance) >= 2 && instance.hasStatusEffect(StatusEffects.INVISIBILITY) || StealthHelper.getFullArmorObscuringEnchantmentLevel(target) >= 2 && target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"
        ),
        method = "attack"
    )
    private boolean shouldSpawnParticles(ServerWorld instance, ParticleEffect particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        return !(StealthHelper.getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY));
    }
}
