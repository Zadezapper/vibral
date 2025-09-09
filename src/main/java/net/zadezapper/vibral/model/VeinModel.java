package net.zadezapper.vibral.model;

import com.mojang.datafixers.util.Either;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.Baker;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class VeinModel implements UnbakedModel {
    private final Identifier parent;
    private final Identifier texture;

    public VeinModel(Identifier parent, Identifier texture) {
        this.parent = parent;
        this.texture = texture;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return List.of(parent);
    }

    @Override
    public void setParents(Function<Identifier, UnbakedModel> modelLoader) {

    }

    @Override
    public @Nullable BakedModel bake(Baker baker, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer) {
        throw new UnsupportedOperationException("This model is for data generation only.");
    }

    public UnbakedModel get() {
        // Define texture mapping using `Either.left` for SpriteIdentifier
        SpriteIdentifier spriteId = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, texture);
        Map<String, Either<SpriteIdentifier, String>> textures = new HashMap<>();
        textures.put("all", Either.left(spriteId)); // for all sides, use this texture

        // Return the JsonUnbakedModel wrapped in a Supplier
        return new JsonUnbakedModel(
                parent,
                List.of(),
                textures,
                null, // default ambient occlusion
                JsonUnbakedModel.GuiLight.ITEM,
                ModelTransformation.NONE,
                List.of() // no overrides
        );
    }
}
