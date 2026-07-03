package net.zadezapper.vibral.mixin;


import net.minecraft.block.Block;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.zadezapper.vibral.block.VibralBlocks.VIBRAL_PANEL;

@Mixin(value = EntityRenderer.class, priority = 2048)
public abstract class EntityRendererMixin {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(Entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callbackInfo) {
        if (StealthHelper.getFullArmorObscuringEnchantmentLevel(entity) >= 1) {
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
}

