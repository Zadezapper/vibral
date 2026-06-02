package net.zadezapper.vibral.entity.custom;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FollowingItemEntity extends ItemEntity {
    private PlayerEntity target;

    public FollowingItemEntity(World world, double x, double y, double z, ItemStack stack) {
        super(world, x, y, z, stack);
    }
    public FollowingItemEntity(EntityType<? extends FollowingItemEntity> entityType, World world) {
        super(entityType, world);
    }
}
