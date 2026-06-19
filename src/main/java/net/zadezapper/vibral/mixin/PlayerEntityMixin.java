package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.zadezapper.vibral.effect.ModEffects;
import net.zadezapper.vibral.enchantment.ModEnchantments;
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
            if (isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(ModEffects.SILENCE)) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getSplashSound", cancellable = true)
    public void getSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(ModEffects.SILENCE)) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getHighSpeedSplashSound", cancellable = true)
    public void getHighSpeedSplashSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(ModEffects.SILENCE)) {
                callbackInfoReturnable.setReturnValue(ModSoundEvents.SILENT);
                callbackInfoReturnable.cancel();
            }
        }
    }

    /*
    @Inject(at = @At("HEAD"), method = "getMoveEffect", cancellable = true)
    public void getMoveEffect(CallbackInfoReturnable<Entity.MoveEffect> callbackInfoReturnable) {
        if (isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(ModEffects.SILENCE)) {
            callbackInfoReturnable.setReturnValue(Entity.MoveEffect.NONE);
            callbackInfoReturnable.cancel();
        }
    }
    */

    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            ),
            method = "attack"
    )
    private boolean damage(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        Entity attacker = source.getAttacker();
        if (attacker instanceof LivingEntity livingEntity && instance instanceof LivingEntity && (isHoldingVibralTool(livingEntity) || livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
            ((LivingEntity)instance).addStatusEffect(new StatusEffectInstance(
                    ModEffects.SILENCE,
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
        if (attacker instanceof LivingEntity livingEntity && (isHoldingVibralTool(livingEntity) || livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
            instance.addStatusEffect(new StatusEffectInstance(
                    ModEffects.SILENCE,
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
    private boolean skip(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !(isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(ModEffects.SILENCE));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
            ),
            method = "attack"
    )
    private boolean skip2(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        return !(isHoldingVibralTool(entity) || entity.hasStatusEffect(ModEffects.SILENCE));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V"
            ),
            method = "attack"
    )
    private boolean skip3(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category) {
        return !(isHoldingVibralTool(entity) || entity.hasStatusEffect(ModEffects.SILENCE));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;spawnSweepAttackParticles()V"
            ),
            method = "attack"
    )
    private boolean skip3(PlayerEntity instance) {
        return !(getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;addCritParticles(Lnet/minecraft/entity/Entity;)V"
            ),
            method = "attack"
    )
    private boolean skip4(PlayerEntity instance, Entity target) {
        return !(getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;addEnchantedHitParticles(Lnet/minecraft/entity/Entity;)V"
            ),
            method = "attack"
    )
    private boolean skip5(PlayerEntity instance, Entity target) {
        return !(getFullArmorObscuringEnchantmentLevel(instance) >= 2 && instance.hasStatusEffect(StatusEffects.INVISIBILITY) || getFullArmorObscuringEnchantmentLevel(target) >= 2 && target instanceof LivingEntity livingTarget && livingTarget.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;spawnParticles(Lnet/minecraft/particle/ParticleEffect;DDDIDDDD)I"
            ),
            method = "attack"
    )
    private boolean skip6(ServerWorld instance, ParticleEffect particle, double x, double y, double z, int count, double deltaX, double deltaY, double deltaZ, double speed) {
        return !(getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity.hasStatusEffect(StatusEffects.INVISIBILITY));
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

    @Unique
    private int getFullArmorObscuringEnchantmentLevel(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            int headLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.HEAD));
            int chestLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.CHEST));
            int legsLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.LEGS));
            int feetLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.FEET));

            return Math.min(Math.min(headLevel, chestLevel), Math.min(legsLevel, feetLevel));
        } else {
            return -1;
        }
    }
}
