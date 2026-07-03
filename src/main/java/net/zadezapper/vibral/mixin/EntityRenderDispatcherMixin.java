package net.zadezapper.vibral.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Shadow
    private Map<String, EntityRenderer<? extends PlayerEntity>> modelRenderers;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void hideEntity(Entity entity, double x, double y, double z, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callbackInfo) {
        if (StealthHelper.getFullArmorObscuringEnchantmentLevel(entity) >= 2 && entity instanceof LivingEntity livingEntity && livingEntity.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            callbackInfo.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "getRenderer", cancellable = true)
    private <T extends Entity> void getRenderer(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> callbackInfoReturnable) {
        if (entity instanceof AbstractClientPlayerEntity player) {
            if (StealthHelper.getFullArmorObscuringEnchantmentLevel(player) >= 2) {
                @SuppressWarnings("unchecked") EntityRenderer<? super T> renderer = (EntityRenderer<? super T>) modelRenderers.get(SkinTextures.Model.WIDE);
                callbackInfoReturnable.setReturnValue(renderer);
            }
        }
    }
}