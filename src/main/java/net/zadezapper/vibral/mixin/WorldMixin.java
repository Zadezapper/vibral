package net.zadezapper.vibral.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.item.ModItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = World.class, priority = 2048)
public abstract class WorldMixin {
    @Inject(at = @At("HEAD"), method = "playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;)V", cancellable = true)
    public void playSound(PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, CallbackInfo callbackInfo) {
        // Vibral.LOGGER.info(source + " Made sound1: " + sound.getId().toString());
        if (source != null) {
            if (isWearingFullVibralArmorSet(source)) {
                callbackInfo.cancel();
            }
        }
    }

    /*
    @Inject(at = @At("HEAD"), method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V", cancellable = true)
    public void playSound(double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, CallbackInfo callbackInfo) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity)) {
                callbackInfo.cancel();
            }
        }
    }
    */

    @Inject(at = @At("HEAD"), method = "playSound(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", cancellable = true)
    public void playSound(Entity source, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch, CallbackInfo callbackInfo) {
        // Vibral.LOGGER.info(source + " Made sound2: " + sound.getId().toString());
        if (source != null) {
            if (isWearingFullVibralArmorSet(source)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", cancellable = true)
    public void playSound(PlayerEntity source, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch, CallbackInfo callbackInfo) {
        // Vibral.LOGGER.info(source + " Made sound3: " + sound.getId().toString());
        if (source != null) {
            if (isWearingFullVibralArmorSet(source)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFJ)V", cancellable = true)
    public void playSound(PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, long seed, CallbackInfo callbackInfo) {
        // Vibral.LOGGER.info(source + " Made sound4: " + sound.getId().toString());
        if (source != null) {
            if (isWearingFullVibralArmorSet(source)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V", cancellable = true)
    public void playSound(PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, CallbackInfo callbackInfo) {
        // Vibral.LOGGER.info(source + " Made sound5: " + sound.getId().toString());
        if (source != null) {
            if (isWearingFullVibralArmorSet(source)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/registry/entry/RegistryEntry;Lnet/minecraft/sound/SoundCategory;FF)V", cancellable = true)
    public void playSound(@Nullable PlayerEntity source, double x, double y, double z, RegistryEntry<SoundEvent> sound, SoundCategory category, float volume, float pitch, CallbackInfo callbackInfo) {
        // Vibral.LOGGER.info(source + " Made sound6: " + sound.getIdAsString());
        if (source != null) {
            if (isWearingFullVibralArmorSet(source)) {
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