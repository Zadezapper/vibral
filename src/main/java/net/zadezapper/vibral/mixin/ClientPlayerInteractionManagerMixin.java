package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ClientPlayerInteractionManager.class, priority = 4096)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sound/SoundManager;play(Lnet/minecraft/client/sound/SoundInstance;)V"
        ),
        method = "updateBlockBreakingProgress"
    )
    private boolean shouldPlay(SoundManager instance, SoundInstance sound) {
        return !(StealthHelper.isHoldingVibralTool(client.player) || client.player.hasStatusEffect(VibralEffects.SILENCE));
    }
}
