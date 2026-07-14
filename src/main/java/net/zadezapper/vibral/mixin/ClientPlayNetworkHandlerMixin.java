package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.ItemPickupAnimationS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
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
    private boolean shouldPlaySound(ClientWorld instance, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch, boolean useDistance, ItemPickupAnimationS2CPacket packet) {
        ClientPlayNetworkHandler clientPlayNetworkHandler = ((ClientPlayNetworkHandler)(Object)this);
        LivingEntity livingEntity = (LivingEntity)clientPlayNetworkHandler.getWorld().getEntityById(packet.getCollectorEntityId());
        return livingEntity  == null || !(StealthHelper.isWearingFullVibralArmorSet(livingEntity) || livingEntity.hasStatusEffect(VibralEffects.SILENCE));
    }
}
