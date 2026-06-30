package net.zadezapper.vibral.mixin;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.zadezapper.vibral.accessor.SneakTicker;
import net.zadezapper.vibral.enchantment.ModEnchantments;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ArmorFeatureRenderer.class, priority = 4096)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
    @Shadow
    protected abstract void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, A model, int i, Identifier identifier);

    @Unique
    private LivingEntity vibral$currentEntity;

    @Inject(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/render/entity/model/BipedEntityModel;ILnet/minecraft/util/Identifier;)V"
            ),
            method = "renderArmor",
            cancellable = true
    )
    private void renderArmor(MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity, EquipmentSlot armorSlot, int light, A model, CallbackInfo callbackInfo) {
        vibral$currentEntity = entity;
        if (entity instanceof LivingEntity livingEntity && getFullArmorObscuringEnchantmentLevel(livingEntity) >= 3) {
            if (entity.getEquippedStack(armorSlot).getItem() instanceof ArmorItem armorItem) {
                callbackInfo.cancel();
                int alpha = MathHelper.clamp(
                        (int)(255 * (1.0F - MathHelper.clamp(
                                (((SneakTicker)entity).vibral$getSneakingTicks() - 50) / 150f,
                            0.0F,1.0F
                        ))), 0, 255
                );
                for (ArmorMaterial.Layer layer : armorItem.getMaterial().value().layers()) {
                    ItemStack itemStack = entity.getEquippedStack(armorSlot);
                    int color = layer.isDyeable() ? itemStack.isIn(ItemTags.DYEABLE) ? ColorHelper.Argb.fullAlpha(DyedColorComponent.getColor(itemStack, -6265536)) : -1 : -1;
                    this.renderArmorParts(matrices, vertexConsumers, light, model, (color & 0x00FFFFFF) | (alpha << 24), layer.getTexture(armorSlot == EquipmentSlot.LEGS));
                }
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "renderArmorParts", cancellable = true)
    private void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, A model, int color, Identifier identifier, CallbackInfo callbackInfo) {
        if (getFullArmorObscuringEnchantmentLevel(vibral$currentEntity) >= 3) {
            callbackInfo.cancel();
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(identifier));
            model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, color);
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
