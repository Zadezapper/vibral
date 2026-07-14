package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BucketItem.class, priority = 4096)
public abstract class BucketItemMixin {

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/WorldAccess;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        ),
        method = "playEmptyingSound"
    )
    private boolean shouldPlaySound(WorldAccess instance, PlayerEntity player, BlockPos blockPos, SoundEvent soundEvent, SoundCategory soundCategory, float volume, float pitch) {
        return player == null || !(StealthHelper.isWearingFullVibralArmorSet(player) || player.hasStatusEffect(VibralEffects.SILENCE));
    }
}
