package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import net.zadezapper.vibral.accessor.FollowingItem;
import net.zadezapper.vibral.accessor.SneakTicker;
import net.zadezapper.vibral.effect.ModEffects;
import net.zadezapper.vibral.enchantment.ModEnchantments;
import net.zadezapper.vibral.item.ModItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = LivingEntity.class, priority = 4096)
public abstract class LivingEntityMixin implements SneakTicker {
    @Shadow
    protected PlayerEntity attackingPlayer;
    @Final
    @Shadow
    private static TrackedData<List<ParticleEffect>> POTION_SWIRLS;
    @Final
    @Shadow
    private static TrackedData<Boolean> POTION_SWIRLS_AMBIENT;

    @Unique
    private int vibral$sneakingTicks;

    @Override
    public int vibral$getSneakingTicks() {
        return vibral$sneakingTicks;
    }

    @Override
    public void vibral$setSneakingTicks(int ticks) {
        vibral$sneakingTicks = ticks;
    }

    @Inject(at = @At("HEAD"), method = "updatePotionSwirls", cancellable = true)
    private void updatePotionSwirls(CallbackInfo callbackInfo) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (getFullArmorObscuringEnchantmentLevel(self) >= 2 && self.hasStatusEffect(StatusEffects.INVISIBILITY)) {
            callbackInfo.cancel();
            self.getDataTracker().set(POTION_SWIRLS, new ArrayList<>());
            self.getDataTracker().set(POTION_SWIRLS_AMBIENT, false);
        }
    }

    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo callbackInfo) {
        LivingEntity self = (LivingEntity)(Object)this;
        if (self.isSneaking()) {
            vibral$sneakingTicks++;
        } else {
            vibral$sneakingTicks = 0;
        }
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(Lnet/minecraft/entity/player/PlayerEntity;DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FF)V"
            ),
            method = "eatFood"
    )
    private boolean skip(World world, PlayerEntity source, double x, double y, double z, SoundEvent sound, SoundCategory category, float volume, float pitch) {
        LivingEntity self = (LivingEntity)(Object)this;
        return !(isWearingFullVibralArmorSet(self) || self.hasStatusEffect(ModEffects.SILENCE));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            ),
            method = "baseTick"
    )
    private boolean skip2(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        LivingEntity self = (LivingEntity)(Object)this;
        return !(getFullArmorObscuringEnchantmentLevel(self) >= 2 && self.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            ),
            method = "tickStatusEffects"
    )
    private boolean skip3(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        LivingEntity self = (LivingEntity)(Object)this;
        return !(getFullArmorObscuringEnchantmentLevel(self) >= 2 && self.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            ),
            method = "handleStatus"
    )
    private boolean skip4(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        LivingEntity self = (LivingEntity)(Object)this;
        return !(getFullArmorObscuringEnchantmentLevel(self) >= 2 && self.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            ),
            method = "addDeathParticles"
    )
    private boolean skip5(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        LivingEntity self = (LivingEntity)(Object)this;
        return !(getFullArmorObscuringEnchantmentLevel(self) >= 2 && self.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @WrapWithCondition(
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
            ),
            method = "spawnItemParticles"
    )
    private boolean skip6(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        LivingEntity self = (LivingEntity)(Object)this;
        return !(getFullArmorObscuringEnchantmentLevel(self) >= 2 && self.hasStatusEffect(StatusEffects.INVISIBILITY));
    }

    @Inject(at = @At(value = "HEAD"), method = "dropLoot", cancellable = true)
    private void drop(DamageSource damageSource, boolean causedByPlayer, CallbackInfo callbackInfo) {
        if (damageSource.getAttacker() instanceof LivingEntity attacker) {
            World world = attacker.getWorld();
            int collectingEnchantmentLevel = EnchantmentHelper.getLevel(
                    damageSource.getAttacker().getWorld().getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(ModEnchantments.COLLECTING)
                            .orElseThrow(),
                    attacker.getEquippedStack(EquipmentSlot.MAINHAND)
            );
            if (collectingEnchantmentLevel > 0 && world instanceof ServerWorld) {
                callbackInfo.cancel();
                LivingEntity thisEntity = (LivingEntity)(Object)this;
                RegistryKey<LootTable> registryKey = thisEntity.getLootTable();
                LootTable lootTable = thisEntity.getWorld().getServer().getReloadableRegistries().getLootTable(registryKey);
                LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)thisEntity.getWorld())
                        .add(LootContextParameters.THIS_ENTITY, thisEntity)
                        .add(LootContextParameters.ORIGIN, thisEntity.getPos())
                        .add(LootContextParameters.DAMAGE_SOURCE, damageSource)
                        .addOptional(LootContextParameters.ATTACKING_ENTITY, damageSource.getAttacker())
                        .addOptional(LootContextParameters.DIRECT_ATTACKING_ENTITY, damageSource.getSource());
                if (causedByPlayer && this.attackingPlayer != null) {
                    builder = builder.add(LootContextParameters.LAST_DAMAGE_PLAYER, this.attackingPlayer).luck(this.attackingPlayer.getLuck());
                }

                LootContextParameterSet lootContextParameterSet = builder.build(LootContextTypes.ENTITY);
                lootTable.generateLoot(lootContextParameterSet, thisEntity.getLootTableSeed(), stack -> {
                    if (!stack.isEmpty() && !thisEntity.getWorld().isClient) {
                        ItemEntity itemEntity = new ItemEntity(thisEntity.getWorld(), thisEntity.getX(), thisEntity.getY(), thisEntity.getZ(), stack);
                        itemEntity.setOwner(attacker.getUuid());
                        FollowingItem data = (FollowingItem)itemEntity;
                        data.vibral$setTargetEntity(attacker);
                        data.vibral$setFollowTicks(collectingEnchantmentLevel * 100);
                        data.vibral$setFollowStrength((collectingEnchantmentLevel == 1 ? 0.02f : 0) + collectingEnchantmentLevel / 20.0f);
                        data.vibral$setFollowDistance(collectingEnchantmentLevel * 20);
                        itemEntity.setToDefaultPickupDelay();
                        thisEntity.getWorld().spawnEntity(itemEntity);
                    }
                });
            }
        }
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

    @Unique
    private int getFullArmorObscuringEnchantmentLevel(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            int headLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.HEAD));
            int chestLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.CHEST));
            int legsLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.LEGS));
            int feetLevel = EnchantmentHelper.getLevel(livingEntity.getWorld().getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(ModEnchantments.OBSCURING).orElseThrow(),livingEntity.getEquippedStack(EquipmentSlot.FEET));

            return Math.min(Math.min(headLevel, chestLevel), Math.min(legsLevel, feetLevel));
        } else {
            return -1;
        }
    }
}
