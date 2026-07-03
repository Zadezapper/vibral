package net.zadezapper.vibral.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.zadezapper.vibral.accessor.FollowingItem;
import net.zadezapper.vibral.block.entity.EchoBlockEntity;
import net.zadezapper.vibral.effect.VibralEffects;
import net.zadezapper.vibral.enchantment.VibralEnchantments;
import net.zadezapper.vibral.util.StealthHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(value = Block.class, priority = 4096)
public abstract class BlockMixin {

    @Inject(at = @At("HEAD"), method = "dropStacks(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/BlockEntity;Lnet/minecraft/entity/Entity;Lnet/minecraft/item/ItemStack;)V", cancellable = true)
    private static void dropStacks(BlockState state, World world, BlockPos pos, BlockEntity blockEntity, Entity entity, ItemStack tool, CallbackInfo callbackInfo) {
        if (world instanceof ServerWorld serverWorld) {
            int collectingEnchantmentLevel = EnchantmentHelper.getLevel(
                world.getRegistryManager()
                    .get(RegistryKeys.ENCHANTMENT)
                    .getEntry(VibralEnchantments.COLLECTING)
                    .orElseThrow(),
                tool
            );
            int echoMiningEnchantmentLevel = EnchantmentHelper.getLevel(
                world.getRegistryManager()
                    .get(RegistryKeys.ENCHANTMENT)
                    .getEntry(VibralEnchantments.ECHO_MINING)
                    .orElseThrow(),
                tool
            );
            if (echoMiningEnchantmentLevel > 0) {
                callbackInfo.cancel();
            }
            if (collectingEnchantmentLevel > 0) {
                callbackInfo.cancel();
                Block.getDroppedStacks(state, serverWorld, pos, blockEntity, entity, tool).forEach((stack) -> {
                    if (entity instanceof ServerPlayerEntity) {
                        double random = MathHelper.nextDouble(world.random, -0.25, 0.25);
                        publicDropStack(world, () -> {
                            ItemEntity item = new ItemEntity(world,
                                (double) pos.getX() + 0.5 + random,
                                (double) pos.getY() + 0.5 + random - (double) EntityType.ITEM.getHeight() / 2.0,
                                (double) pos.getZ() + 0.5 + random,
                                stack);
                            item.setOwner(entity.getUuid());
                            FollowingItem data = (FollowingItem) item;
                            data.vibral$setTargetEntity(entity);
                            data.vibral$setFollowTicks(collectingEnchantmentLevel * 100);
                            data.vibral$setFollowStrength((collectingEnchantmentLevel == 1 ? 0.02f : 0) + collectingEnchantmentLevel / 20.0f);
                            data.vibral$setFollowDistance(collectingEnchantmentLevel * 20);
                            return item;
                        }, stack);
                    }
                });
                state.onStacksDropped(serverWorld, pos, tool, true);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "afterBreak")
    private static void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState originalBlock, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
        if (world instanceof ServerWorld serverWorld) {
            int echoMiningEnchantmentLevel = EnchantmentHelper.getLevel(
                world.getRegistryManager()
                    .get(RegistryKeys.ENCHANTMENT)
                    .getEntry(VibralEnchantments.ECHO_MINING)
                    .orElseThrow(),
                tool
            );
            if (echoMiningEnchantmentLevel > 0) {
                EchoBlockEntity.create(serverWorld, pos, originalBlock,
                    blockEntity != null ? blockEntity.createNbtWithIdentifyingData(serverWorld.getRegistryManager()) : null,
                    Math.max(3, (int)(130 * Math.pow(0.5, echoMiningEnchantmentLevel - 1)))
                );
            }
        }
    }

    @WrapWithCondition(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;syncWorldEvent(Lnet/minecraft/entity/player/PlayerEntity;ILnet/minecraft/util/math/BlockPos;I)V"
        ),
        method = "spawnBreakParticles"
    )
    private boolean shouldSyncWorldEvent(World instance, PlayerEntity playerEntity, int eventId, BlockPos blockPos, int data) {
        return !(StealthHelper.isWearingFullVibralArmorSet(playerEntity) || playerEntity.hasStatusEffect(VibralEffects.SILENCE));
    }

    @Unique
    private static void publicDropStack(World world, Supplier<ItemEntity> itemEntitySupplier, ItemStack stack) {
        if (!world.isClient && !stack.isEmpty() && world.getGameRules().getBoolean(GameRules.DO_TILE_DROPS)) {
            ItemEntity itemEntity = itemEntitySupplier.get();
            itemEntity.setToDefaultPickupDelay();
            world.spawnEntity(itemEntity);
        }
    }
}
