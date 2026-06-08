package net.zadezapper.vibral.mixin;


import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.zadezapper.vibral.enchantment.ModEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.zadezapper.vibral.block.ModBlocks.VIBRAL_PANEL;

@Mixin(value = EntityRenderer.class, priority = 2048)
public abstract class EntityRendererMixin {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callbackInfo) {
        if (getFullArmorObscuringEnchantmentLevel(entity) >= 1) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaky()Z"), cancellable = true)
    private void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo callbackInfo) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d cameraPosition = camera.getPos();
        Vec3d entityPosition = entity.getBoundingBox().getCenter();
        RaycastContext context = new RaycastContext(
                cameraPosition, entityPosition, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity
        );
        Block hit = entity.getWorld().getBlockState(entity.getWorld().raycast(context).getBlockPos()).getBlock();

        if (hit == VIBRAL_PANEL) {
            callbackInfo.cancel();
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

