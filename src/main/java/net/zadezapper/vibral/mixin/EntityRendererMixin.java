package net.zadezapper.vibral.mixin;


import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

