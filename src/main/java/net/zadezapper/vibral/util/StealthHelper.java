package net.zadezapper.vibral.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.zadezapper.vibral.enchantment.VibralEnchantments;
import net.zadezapper.vibral.item.VibralItems;

public class StealthHelper {
    public static boolean isWearingFullVibralArmorSet(Entity entity) {
        if (entity instanceof LivingEntity) {
            return (
                ((LivingEntity) entity).getEquippedStack(EquipmentSlot.HEAD).isOf(VibralItems.VIBRAL_HELMET)
                    && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.CHEST).isOf(VibralItems.VIBRAL_CHESTPLATE)
                    && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.LEGS).isOf(VibralItems.VIBRAL_LEGGINGS)
                    && ((LivingEntity) entity).getEquippedStack(EquipmentSlot.FEET).isOf(VibralItems.VIBRAL_BOOTS)
            );
        } else {
            return false;
        }
    }

    public static boolean isHoldingVibralTool(Entity entity) {
        if (entity instanceof LivingEntity) {
            return (
                ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(VibralItems.VIBRAL_SWORD)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(VibralItems.VIBRAL_PICKAXE)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(VibralItems.VIBRAL_AXE)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(VibralItems.VIBRAL_SHOVEL)
                    || ((LivingEntity) entity).getEquippedStack(EquipmentSlot.MAINHAND).isOf(VibralItems.VIBRAL_HOE)
            );
        } else {
            return false;
        }
    }

    public static int getFullArmorObscuringEnchantmentLevel(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            int headLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(VibralEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.HEAD));
            int chestLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(VibralEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.CHEST));
            int legsLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(VibralEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.LEGS));
            int feetLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(VibralEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.FEET));

            return Math.min(Math.min(headLevel, chestLevel), Math.min(legsLevel, feetLevel));
        } else {
            return -1;
        }
    }
}
