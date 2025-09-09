package net.zadezapper.vibral.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.item.ModArmorMaterials;

public class PlayerEntityOverride extends PlayerEntity {
    public PlayerEntityOverride(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    private boolean hasFullSuitOfArmorOn(PlayerEntity player, World world) {
        if (!world.isClient) {
            ItemStack boots = player.getInventory().getArmorStack(0);
            ItemStack leggings = player.getInventory().getArmorStack(1);
            ItemStack breastplate = player.getInventory().getArmorStack(2);
            ItemStack helmet = player.getInventory().getArmorStack(3);

            return !helmet.isEmpty() && !breastplate.isEmpty()
                    && !leggings.isEmpty() && !boots.isEmpty();
        }
        return false;
    }

    private boolean hasCorrectArmorOn(RegistryEntry<ArmorMaterial> material, PlayerEntity player, World world) {
        if (!world.isClient) {
            for (ItemStack armorStack : player.getInventory().armor) {
                if (!(armorStack.getItem() instanceof ArmorItem)) {
                    return false;
                }
            }

            ArmorItem boots = ((ArmorItem) player.getInventory().getArmorStack(0).getItem());
            ArmorItem leggings = ((ArmorItem) player.getInventory().getArmorStack(1).getItem());
            ArmorItem breastplate = ((ArmorItem) player.getInventory().getArmorStack(2).getItem());
            ArmorItem helmet = ((ArmorItem) player.getInventory().getArmorStack(3).getItem());

            return helmet.getMaterial() == material && breastplate.getMaterial() == material &&
                    leggings.getMaterial() == material && boots.getMaterial() == material;
        }
        return false;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return false;
    }

    @Override
    protected SoundEvent getSwimSound() {
        if (hasFullSuitOfArmorOn(this, getWorld())
                && hasCorrectArmorOn(ModArmorMaterials.VIBRAL_ARMOR_MATERIAL, this, getWorld())) {
            Vibral.LOGGER.info("Player Swimming with Vibral Armor");
            return SoundEvents.ENTITY_PLAYER_BURP;
        } else {
            // return new SoundEvent();
            Vibral.LOGGER.info("Player Swimming without Vibral Armor");
            return SoundEvents.ENTITY_PLAYER_SWIM;
        }
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
    public void playSound(SoundEvent sound, float volume, float pitch) {
        this.getWorld().playSound(this, this.getX(), this.getY(), this.getZ(), sound, this.getSoundCategory(), volume, pitch);
    }
}
