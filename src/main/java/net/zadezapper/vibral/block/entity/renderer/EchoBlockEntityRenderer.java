package net.zadezapper.vibral.block.entity.renderer;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.zadezapper.vibral.block.entity.EchoBlockEntity;

public class EchoBlockEntityRenderer implements BlockEntityRenderer<EchoBlockEntity> {
    private final BlockRenderManager BLOCK_RENDER_MANAGER;

    public EchoBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.BLOCK_RENDER_MANAGER = MinecraftClient.getInstance().getBlockRenderManager();
    }

    @Override
    public boolean rendersOutsideBoundingBox(EchoBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(EchoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState storedBlock = blockEntity.getStoredBlock();

        if (storedBlock == null || storedBlock.isAir()) {
            return;
        }
        BakedModel blockModel = BLOCK_RENDER_MANAGER.getModel(storedBlock);

        int age = blockEntity.getAge();
        int restoreTicks = blockEntity.getRestoreTicks();
        float scale = 1;
        float alpha = 1;

        matrices.push();
        matrices.translate(0.0, 0.0, 0.0);

        BLOCK_RENDER_MANAGER.getModelRenderer().render(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getTranslucent()),
                storedBlock,
                blockModel,
                0.019f,
                0.164f,
                0.196f,
                LightmapTextureManager.MAX_LIGHT_COORDINATE,
                overlay
        );

        matrices.pop();
    }
}
