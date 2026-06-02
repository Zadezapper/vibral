package net.zadezapper.vibral.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.entity.custom.FollowingItemEntity;

public class ModEntities {
    public static final EntityType<FollowingItemEntity> FOLLOWING_ITEM = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(Vibral.MOD_ID, "following_item"),
            EntityType.Builder.<FollowingItemEntity>create(FollowingItemEntity::new, SpawnGroup.MISC).dimensions(0.5F, 0.5F).build());

    public static void registerModEntities() {
        Vibral.LOGGER.info("Registering Entities for " + Vibral.MOD_ID);
    }
}
