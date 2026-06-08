package net.zadezapper.vibral.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.event.listener.Vibration;
import net.minecraft.world.event.listener.VibrationSelector;
import net.zadezapper.vibral.effect.ModEffects;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = VibrationSelector.class, priority = 2048)
public abstract class VibrationSelectorMixin {
    @Inject(at = @At("HEAD"), method = "shouldSelect", cancellable = true)
    private void shouldSelect(Vibration vibration, long tick, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        Entity vibrationSource = vibration.entity();

        if (vibrationSource != null) {
            if (isWearingFullVibralArmorSet(vibrationSource) || (vibrationSource instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(ModEffects.SILENCE))) {
                callbackInfoReturnable.setReturnValue(false);
                callbackInfoReturnable.cancel();
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
}