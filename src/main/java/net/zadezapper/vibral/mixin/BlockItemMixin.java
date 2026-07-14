package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = BlockItem.class, priority = 4096)
public abstract class BlockItemMixin {

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
        ),
        method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;"
    )
    private boolean shouldPlaySound(World world, PlayerEntity source, BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch, ItemPlacementContext context) {
        LivingEntity entity = ((BlockItem)(Object)this).getPlacementContext(context).getPlayer();
        return entity == null || !(StealthHelper.isWearingFullVibralArmorSet(entity) || entity.hasStatusEffect(VibralEffects.SILENCE));
    }
}
