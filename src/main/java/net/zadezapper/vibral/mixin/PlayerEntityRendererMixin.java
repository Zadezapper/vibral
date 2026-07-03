package net.zadezapper.vibral.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntityRenderer.class, priority = 4096)
public abstract class PlayerEntityRendererMixin {
    @Inject(at = @At("HEAD"), method = "getTexture(Lnet/minecraft/client/network/AbstractClientPlayerEntity;)Lnet/minecraft/util/Identifier;", cancellable = true)
    public void getTexture(AbstractClientPlayerEntity abstractClientPlayerEntity, CallbackInfoReturnable<Identifier> callbackInfoReturnable) {
        if (abstractClientPlayerEntity instanceof PlayerEntity playerEntity) {
            if (StealthHelper.getFullArmorObscuringEnchantmentLevel(playerEntity) >= 2) {
                callbackInfoReturnable.setReturnValue(Identifier.of(Vibral.MOD_ID, "textures/entity/noise-skin.png"));
                callbackInfoReturnable.cancel();
            }
        }
    }
}
