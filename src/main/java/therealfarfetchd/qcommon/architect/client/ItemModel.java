package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.ModelRotationContainer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.Transformation;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap.ModelTransform;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class ItemModel extends BaseModel {

    public ItemModel(StateProvider sp, Model model) {
        super(sp, model);
    }

    @Override
    public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> var1, Set<String> var2) {
        Set<Identifier> requiredTextures = new HashSet<>();
        addModelTextures(requiredTextures);
        return requiredTextures;
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader var1, Function<Identifier, Sprite> var2, ModelRotationContainer var3) {
        Sprite placeholder = var2.apply(TextureRef.PLACEHOLDER.texture);
        Function<Identifier, Sprite> getTextureFixed = id -> select(var2.apply(id), s -> s != MissingSprite.getMissingSprite(), placeholder);

        Function<TextureRef, Sprite> mapper = tr -> getTextureFixed.apply(tr.getTexture(tm));
        Sprite particle = getTextureFixed.apply(select(tm.getTexture("particle"), TextureRef.PLACEHOLDER.texture));

        Map<Direction, List<BakedQuad>> quadsMap = getQuads(VertexFormats.POSITION_COLOR_UV_NORMAL, mapper);

        return new BasicBakedModel(quadsMap.get(null), quadsMap, true, true, particle, from(model.getModelTransforms()), ModelItemPropertyOverrideList.ORIGIN);
    }

    public static ModelTransformation from(ModelTransformMap mtm) {
        if (mtm.equals(ModelTransformMap.IDENTITY)) return ModelTransformation.ORIGIN;

        return new ModelTransformation(
            from(mtm.thirdPersonLeftHand),
            from(mtm.thirdPersonRightHand),
            from(mtm.firstPersonLeftHand),
            from(mtm.firstPersonRightHand),
            from(mtm.head),
            from(mtm.gui),
            from(mtm.ground),
            from(mtm.fixed)
        );
    }

    public static Transformation from(ModelTransform mt) {
        return new Transformation(
            new Vector3f(mt.rotation.x, mt.rotation.y, mt.rotation.z),
            new Vector3f(mt.translation.x, mt.translation.y, mt.translation.z),
            new Vector3f(mt.scale.x, mt.scale.y, mt.scale.z)
        );
    }

}
