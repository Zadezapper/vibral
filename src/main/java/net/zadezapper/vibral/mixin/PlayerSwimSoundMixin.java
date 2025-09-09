package net.zadezapper.vibral.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerEntity.class, priority = 32767)
public abstract class PlayerSwimSoundMixin {

    @Inject(at = @At("HEAD"), method = "getSwimSound")
    protected void getSwimSound(CallbackInfoReturnable<SoundEvent> callbackInfoReturnable) {
        // return SoundEvents.ENTITY_PLAYER_BURP;
        callbackInfoReturnable.getReturnValue();
    }
}
/* @Override
protected SoundEvent getSwimSound() {
	return SoundEvents.ENTITY_PLAYER_SWIM;
}

@Override
protected SoundEvent getSplashSound() {
	return SoundEvents.ENTITY_PLAYER_SPLASH;
}

@Override
protected SoundEvent getHighSpeedSplashSound() {
	return SoundEvents.ENTITY_PLAYER_SPLASH_HIGH_SPEED;
}

@Override
public int getDefaultPortalCooldown() {
	return 10;
}

@Override
public void playSound(SoundEvent sound, float volume, float pitch) {
	this.getWorld().playSound(this, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
} */