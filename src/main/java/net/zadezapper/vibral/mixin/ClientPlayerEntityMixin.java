package net.zadezapper.vibral.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPlayerEntity.class, priority = 4096)
public abstract class ClientPlayerEntityMixin {
    @Unique
    public PlayerEntity entity = ((PlayerEntity)(Object)this);

    @Inject(at = @At("HEAD"), method = "playSound", cancellable = true)
    public void playSound(SoundEvent sound, float volume, float pitch, CallbackInfo callbackInfo) {
        if (entity != null) {
            if (isWearingFullVibralArmorSet(entity)) {
                callbackInfo.cancel();
                return;
            }
        }
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
}
