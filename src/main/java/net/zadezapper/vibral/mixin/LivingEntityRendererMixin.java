package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.accessor.SneakTicker;
import net.zadezapper.vibral.enchantment.ModEnchantments;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = LivingEntityRenderer.class, priority = 4096)
public abstract class LivingEntityRendererMixin {
    @WrapOperation(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/model/EntityModel;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"
            ),
            method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
    )
    private <T extends LivingEntity> void render(EntityModel instance, MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color, Operation<Void> original, T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (livingEntity != null && getFullArmorObscuringEnchantmentLevel(livingEntity) >= 3) {
            int alpha = MathHelper.clamp(
                    (int)(255 * (1.0F - MathHelper.clamp(
                            (((SneakTicker)livingEntity).vibral$getSneakingTicks() - 50) / 150f,
                            0.0F,1.0F
                    ))), 0, 255
            );
            original.call(
                    instance,
                    matrices,
                    vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucentCull(Identifier.of(Vibral.MOD_ID, "textures/entity/noise-skin.png"))),
                    light,
                    overlay,
                    (color & 0x00FFFFFF) | (alpha << 24)
            );
        } else {
            original.call(instance, matrices, vertices, light, overlay, color);
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
