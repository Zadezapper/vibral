package net.zadezapper.vibral;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.zadezapper.vibral.block.entity.VibralBlockEntities;
import net.zadezapper.vibral.block.entity.renderer.EchoBlockEntityRenderer;

public class VibralClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockEntityRendererFactories.register(VibralBlockEntities.ECHO_BE, EchoBlockEntityRenderer::new);
    }
}
