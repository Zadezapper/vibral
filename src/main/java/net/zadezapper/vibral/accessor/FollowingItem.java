package net.zadezapper.vibral.accessor;

import net.minecraft.entity.Entity;

public interface FollowingItem {
    void vibral$setTargetEntity(Entity target);
    Entity vibral$getTargetEntity();
    void vibral$setFollowTicks(int ticks);
    int vibral$getFollowTicks();
    void vibral$setFollowStrength(float strength);
    float vibral$getFollowStrength();
    void vibral$setFollowDistance(float distance);
    float vibral$getFollowDistance();
}
