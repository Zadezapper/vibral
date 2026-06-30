package net.zadezapper.vibral.block.entity.renderer;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.zadezapper.vibral.Vibral;
import net.zadezapper.vibral.block.entity.EchoBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EchoBlockEntityRenderer implements BlockEntityRenderer<EchoBlockEntity> {
    private static final BlockRenderManager BLOCK_RENDER_MANAGER = MinecraftClient.getInstance().getBlockRenderManager();

    public EchoBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
    }

    @Override
    public boolean rendersOutsideBoundingBox(EchoBlockEntity blockEntity) {
        return true;
    }

    @Override
    public void render(EchoBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState storedBlock = blockEntity.getStoredBlock();

        if (storedBlock == null || storedBlock.isAir() || blockEntity.getWorld() == null) {
            return;
        }
        BakedModel blockModel = BLOCK_RENDER_MANAGER.getModel(storedBlock);

        matrices.push();
        matrices.translate(0.0, 0.0, 0.0);

        renderWithAlpha(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getTranslucent()),
                storedBlock,
                blockModel,
                null,
                0.039f, 0.333f, 0.400f, 2/3f,
                LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV
        );

        matrices.pop();

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(1.001f, 1.001f, 1.001f);
        matrices.translate(-0.5, -0.5, -0.5);

        renderWithAlpha(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getTranslucent()),
                storedBlock,
                blockModel,
                MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).getSprite(Identifier.of(Vibral.MOD_ID, "block/raw_vibral")),
                0.039f, 0.333f, 0.400f, 0.35f,
                LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV
        );

        matrices.pop();

        long time = blockEntity.getWorld().getTime();
        int restoreTicks = blockEntity.getRestoreTicks();

        float scale = 1f + (((time * 5) % restoreTicks) / 4f) / restoreTicks;
        float alpha = 1f - ((time * 5f) % restoreTicks) / restoreTicks;

        matrices.push();
        matrices.translate(0.5, 0.5, 0.5);
        matrices.scale(scale, scale, scale);
        matrices.translate(-0.5, -0.5, -0.5);

        renderWithAlpha(
                matrices.peek(),
                vertexConsumers.getBuffer(RenderLayer.getTranslucent()),
                storedBlock,
                blockModel,
                MinecraftClient.getInstance().getBakedModelManager().getAtlas(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE).getSprite(Identifier.of(Vibral.MOD_ID, "block/echo")),
                0.039f, 0.333f, 0.400f, alpha,
                LightmapTextureManager.MAX_BLOCK_LIGHT_COORDINATE,
                OverlayTexture.DEFAULT_UV
        );

        matrices.pop();
    }

    public static void renderWithAlpha(MatrixStack.Entry entry, VertexConsumer vertexConsumer, @Nullable BlockState state, BakedModel model, @Nullable Sprite newSprite, float red, float green, float blue, float alpha, int light, int overlay) {
        Random random = Random.create();
        for (Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderQuadsWithAlpha(entry, vertexConsumer, red, green, blue, alpha, model.getQuads(state, direction, random), newSprite, light, overlay);
        }
        random.setSeed(42L);
        renderQuadsWithAlpha(entry, vertexConsumer, red, green, blue, alpha, model.getQuads(state, null, random), newSprite, light, overlay);
    }
    private static void renderQuadsWithAlpha(MatrixStack.Entry entry, VertexConsumer vertexConsumer, float r, float g, float b, float a, List<BakedQuad> quads, @Nullable Sprite newSprite, int light, int overlay) {
        for (BakedQuad quad : quads) {
            vertexConsumer.quad(
                    entry,
                    newSprite == null ? quad : editQuadSprite(quad, newSprite),
                    MathHelper.clamp(r, 0.0F, 1.0F),
                    MathHelper.clamp(g, 0.0F, 1.0F),
                    MathHelper.clamp(b, 0.0F, 1.0F),
                    a,
                    light,
                    overlay
            );
        }
    }
    
    public static BakedQuad editQuadSprite(BakedQuad originalQuad, Sprite newSprite) {
        Sprite oldSprite = originalQuad.getSprite();
        int[] newVertexData = originalQuad.getVertexData().clone();

        for (int vertex = 0; vertex < 4; vertex++) {
            int vertexOffset = vertex * 8;
            int uIndex = vertexOffset + 4;
            int vIndex = vertexOffset + 5;
            
            float oldU = Float.intBitsToFloat(newVertexData[uIndex]);
            float oldV = Float.intBitsToFloat(newVertexData[vIndex]);
            
            float relativeU = (oldU - oldSprite.getMinU()) / (oldSprite.getMaxU() - oldSprite.getMinU());
            float relativeV = (oldV - oldSprite.getMinV()) / (oldSprite.getMaxV() - oldSprite.getMinV());
            
            float newU = newSprite.getMinU() + relativeU * (newSprite.getMaxU() - newSprite.getMinU());
            float newV = newSprite.getMinV() + relativeV * (newSprite.getMaxV() - newSprite.getMinV());

            newVertexData[uIndex] = Float.floatToRawIntBits(newU);
            newVertexData[vIndex] = Float.floatToRawIntBits(newV);
        }
        return new BakedQuad(newVertexData, originalQuad.getColorIndex(), originalQuad.getFace(), newSprite, originalQuad.hasShade());
    }
}
