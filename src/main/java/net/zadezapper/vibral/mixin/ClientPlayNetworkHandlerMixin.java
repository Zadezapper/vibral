package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.*;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientPlayNetworkHandler.class, priority = 4096)
public abstract class ClientPlayNetworkHandlerMixin {

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/world/ClientWorld;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"
        ),
        method = "onItemPickupAnimation"
    )
    private boolean skip(ClientWorld instance, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, ItemPickupAnimationS2CPacket packet) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = ((ClientPlayNetworkHandler)(Object)this);
        LivingEntity livingEntity = (LivingEntity)clientPlayNetworkHandler.getWorld().getEntityById(packet.getCollectorEntityId());
        return !isWearingFullVibralArmorSet(livingEntity);
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
