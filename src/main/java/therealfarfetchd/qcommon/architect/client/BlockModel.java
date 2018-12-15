package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.BasicBakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.ModelRotationContainer;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformations;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.croco.Vec3;

import static java.lang.Math.round;

public class BlockModel implements UnbakedModel {

    private final StateProvider sp;
    private final Model model;

    public BlockModel(StateProvider sp, Model model) {
        this.sp = sp;
        this.model = model;
    }

    @Override
    public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> var1, Set<String> var2) {
        Set<Identifier> requiredTextures = new HashSet<>();

        TextureMapper tm = model.getTextureMapper().get(sp);
        Function<String, Identifier> tmapper = s -> select(tm.getTexture(s), TextureRef.PLACEHOLDER.texture);

        model.getParts().getPossibleValues().parallelStream()
            .flatMap(Collection::parallelStream)
            .flatMap($ -> $.getFaces().parallelStream())
            .map(Face::getTexture)
            .map($ -> $.getTexture(unused -> TextureRef.PLACEHOLDER.texture))
            .forEach(requiredTextures::add);

        requiredTextures.add(tmapper.apply("particle"));

        return requiredTextures;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    public BakedModel bake(ModelLoader loader, Function<Identifier, Sprite> getTexture, ModelRotationContainer rc) {
        TextureMapper tm = model.getTextureMapper().get(sp);

        Function<TextureRef, Sprite> mapper = tr -> getTexture.apply(tr.getTexture(tm));
        Sprite particle = getTexture.apply(select(tm.getTexture("particle"), TextureRef.PLACEHOLDER.texture));

        List<Quad> quadList = model.getParts().get(sp).parallelStream()
            .flatMap($ -> $.getFaces().parallelStream())
            .flatMap($ -> $.toQuads().parallelStream())
            .collect(Collectors.toList());

        Map<Direction, List<BakedQuad>> quadsMap = categorizeAndBake(quadList, quad -> QuadFactory.INSTANCE.bake(VertexFormats.field_1590, mapper, quad).get(0));

        return new BasicBakedModel(quadsMap.get(null), quadsMap, true, true, particle, ModelTransformations.ORIGIN, ModelItemPropertyOverrideList.ORIGIN);
    }

    private Quad snapToGrid(Quad q) {
        return new Quad(q.texture,
            q.v0.withXYZ(snapToGrid(q.v0.xyz)),
            q.v1.withXYZ(snapToGrid(q.v1.xyz)),
            q.v2.withXYZ(snapToGrid(q.v2.xyz)),
            q.v3.withXYZ(snapToGrid(q.v3.xyz)),
            q.color);
    }

    private Vec3 snapToGrid(Vec3 v) {
        float gridSize = 256;
        return new Vec3(
            round(v.x * gridSize) / gridSize,
            round(v.y * gridSize) / gridSize,
            round(v.z * gridSize) / gridSize
        );
    }

    private Map<Direction, List<BakedQuad>> categorizeAndBake(List<Quad> quads, Function<Quad, BakedQuad> bake) {
        Map<Direction, List<BakedQuad>> l = new HashMap<>();

        for (Direction f : Direction.values()) l.put(f, new ArrayList<>());
        l.put(null, new ArrayList<>());

        for (Quad q : quads) {
            q = snapToGrid(q);

            BakedQuad baked = bake.apply(q);

            boolean isCullable = true;
            Direction f = q.getFacing();
            Vec3 direction = Vec3.from(f.getVector());
            Vec3 filter = direction.mul(direction);
            Vec3 cmp = direction.add(filter).div(2);

            // check if the normal is the same as the facing direction
            if (!direction.equals(q.getNormal())) isCullable = false;

            // check if all vertices are inside the block
            if (isCullable && (q.v0.xyz.x < 0 || q.v0.xyz.x > 1 || q.v0.xyz.y < 0 || q.v0.xyz.y > 1 || q.v0.xyz.z < 0 || q.v0.xyz.z > 1)) isCullable = false;
            if (isCullable && (q.v1.xyz.x < 0 || q.v1.xyz.x > 1 || q.v1.xyz.y < 0 || q.v1.xyz.y > 1 || q.v1.xyz.z < 0 || q.v1.xyz.z > 1)) isCullable = false;
            if (isCullable && (q.v2.xyz.x < 0 || q.v2.xyz.x > 1 || q.v2.xyz.y < 0 || q.v2.xyz.y > 1 || q.v2.xyz.z < 0 || q.v2.xyz.z > 1)) isCullable = false;
            if (isCullable && (q.v3.xyz.x < 0 || q.v3.xyz.x > 1 || q.v3.xyz.y < 0 || q.v3.xyz.y > 1 || q.v3.xyz.z < 0 || q.v3.xyz.z > 1)) isCullable = false;

            // check if all vertices are on the right face of the block
            if (isCullable && !filter.mul(q.v0.xyz).equals(cmp)) isCullable = false;
            if (isCullable && !filter.mul(q.v1.xyz).equals(cmp)) isCullable = false;
            if (isCullable && !filter.mul(q.v2.xyz).equals(cmp)) isCullable = false;
            if (isCullable && !filter.mul(q.v3.xyz).equals(cmp)) isCullable = false;

            if (isCullable) l.get(f).add(baked);
            else l.get(null).add(baked);
        }

        return l;
    }

    private static <T> T select(@Nullable T t, T fallback) {
        return t == null ? fallback : t;
    }

}
