package net.zadezapper.vibral.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.block.ModBlocks;

public class ModBlockEntities {
    public static final BlockEntityType<EchoBlockEntity> ECHO_BE = Registry.register(Registries.BLOCK_ENTITY_TYPE, Identifier.of(Vibral.MOD_ID, "echo_be"),
            BlockEntityType.Builder.create(EchoBlockEntity::new, ModBlocks.ECHO).build());

    public static void registerBlockEntities() {
        Vibral.LOGGER.info("Registering Block Entities for " + Vibral.MOD_ID);
    }
}
