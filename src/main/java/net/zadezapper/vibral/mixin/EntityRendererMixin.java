package net.zadezapper.vibral.mixin;


import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.zadezapper.vibral.block.ModBlocks.VIBRAL_PANEL;
import static net.zadezapper.vibral.block.ModBlocks.VIBRAL_BLOCK;

@Mixin(value = EntityRenderer.class, priority = 2048)
public abstract class EntityRendererMixin<entity extends Entity> {

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void render(entity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo callbackInfo) {
        if (entity instanceof PlayerEntity) {
            if (isWearingFullVibralArmorSet(entity)) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "renderLabelIfPresent", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaky()Z"), cancellable = true)
    private void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float tickDelta, CallbackInfo callbackInfo) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        // Goal: Raycast from the camera to the entity, and check for collisions with vibral blocks. If collision found, cancel nametag rendering.
        // Currently: Appears to raycast from camera to player with too large step size, and or cuts off at some point for some reason
        Vec3d cameraPosition = camera.getPos();
        Vec3d entityPosition = entity.getBoundingBox().getCenter(); // entity.getCameraPosVec(tickDelta);
        Vec3d rayVector = cameraPosition.subtract(entityPosition); // entityPosition.add(entityRotation.x * distance, entityRotation.y * distance, entityRotation.z * distance); // Vector starting at entity and pointing where entity is looking
        RaycastContext context = new RaycastContext(
                entityPosition, rayVector, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, entity
        );
        BlockHitResult hit = BlockView.raycast(context.getStart(), context.getEnd(), context, (innerContext, pos) -> {
            BlockState blockState = entity.getWorld().getBlockState(pos);

            if (blockState.getBlock() != VIBRAL_PANEL && blockState.getBlock() != VIBRAL_BLOCK) {
                return null;
            }

            Vec3d vec3d4 = innerContext.getStart();
            Vec3d vec3d5 = innerContext.getEnd();
            VoxelShape voxelShape = innerContext.getBlockShape(blockState, entity.getWorld(), pos);
            return entity.getWorld().raycastBlock(vec3d4, vec3d5, pos, voxelShape, blockState);
        }, innerContext -> null);

        if (hit != null) {
            callbackInfo.cancel();
        }
    }


    @Unique
    private boolean isWearingFullVibralArmorSet(Entity entity) {

        ItemStack headItemStack = ((PlayerEntity) entity).getEquippedStack(EquipmentSlot.HEAD);
        ItemStack chestItemStack = ((PlayerEntity) entity).getEquippedStack(EquipmentSlot.CHEST);
        ItemStack legsItemStack = ((PlayerEntity) entity).getEquippedStack(EquipmentSlot.LEGS);
        ItemStack feetItemStack = ((PlayerEntity) entity).getEquippedStack(EquipmentSlot.FEET);

        return (headItemStack.isOf(ModItems.VIBRAL_HELMET)
                && chestItemStack.isOf(ModItems.VIBRAL_CHESTPLATE)
                && legsItemStack.isOf(ModItems.VIBRAL_LEGGINGS)
                && feetItemStack.isOf(ModItems.VIBRAL_BOOTS));

    }
}

