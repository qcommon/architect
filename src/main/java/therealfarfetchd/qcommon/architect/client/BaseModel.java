package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
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

public abstract class BaseModel implements UnbakedModel {

    protected final StateProvider sp;
    protected final Model model;
    protected final TextureMapper tm;

    public BaseModel(StateProvider sp, Model model) {
        this.sp = sp;
        this.model = model;
        this.tm = model.getTextureMapper();
    }

    protected void addModelTextures(Collection<Identifier> textures) {
        textures.add(TextureRef.PLACEHOLDER.texture);

        model.getParts(sp).parallelStream()
            .flatMap($ -> $.getFaces(sp).parallelStream())
            .map(Face::getTexture)
            .map($ -> $.getTexture(tm))
            .forEach(textures::add);
    }

    protected Quad snapToGrid(Quad q) {
        return new Quad(q.texture,
            q.v0.withXYZ(snapToGrid(q.v0.xyz)),
            q.v1.withXYZ(snapToGrid(q.v1.xyz)),
            q.v2.withXYZ(snapToGrid(q.v2.xyz)),
            q.v3.withXYZ(snapToGrid(q.v3.xyz)),
            q.color);
    }

    protected Vec3 snapToGrid(Vec3 v) {
        float gridSize = 256;
        return new Vec3(
            round(v.x * gridSize) / gridSize,
            round(v.y * gridSize) / gridSize,
            round(v.z * gridSize) / gridSize
        );
    }

    protected Map<Direction, List<BakedQuad>> getQuads(VertexFormat format, Function<TextureRef, Sprite> mapper) {
        List<Quad> quadList = model.getParts(sp).parallelStream()
            .flatMap($ -> $.getFaces(sp).parallelStream())
            .flatMap($ -> $.toQuads().parallelStream())
            .collect(Collectors.toList());

        return categorizeAndBake(quadList, quad -> QuadFactory.INSTANCE.bake(format, mapper, quad).get(0));
    }

    protected Map<Direction, List<BakedQuad>> categorizeAndBake(List<Quad> quads, Function<Quad, BakedQuad> bake) {
        Map<Direction, List<BakedQuad>> l = new HashMap<>();

        for (Direction f : Direction.values()) l.put(f, new ArrayList<>());
        l.put(null, new ArrayList<>());

        for (Quad q : quads) {
            Quad q1 = snapToGrid(q);

            BakedQuad baked = bake.apply(q);

            boolean isCullable = true;
            Direction f = q1.getFacing();
            Vec3 direction = Vec3.from(f.getVector());
            Vec3 filter = direction.mul(direction);
            Vec3 cmp = direction.add(filter).div(2);

            // check if the normal is the same as the facing direction
            if (!direction.equals(q1.getNormal())) isCullable = false;

            // check if all vertices are inside the block
            if (isCullable && (q1.v0.xyz.x < 0 || q1.v0.xyz.x > 1 || q1.v0.xyz.y < 0 || q1.v0.xyz.y > 1 || q1.v0.xyz.z < 0 || q1.v0.xyz.z > 1)) isCullable = false;
            if (isCullable && (q1.v1.xyz.x < 0 || q1.v1.xyz.x > 1 || q1.v1.xyz.y < 0 || q1.v1.xyz.y > 1 || q1.v1.xyz.z < 0 || q1.v1.xyz.z > 1)) isCullable = false;
            if (isCullable && (q1.v2.xyz.x < 0 || q1.v2.xyz.x > 1 || q1.v2.xyz.y < 0 || q1.v2.xyz.y > 1 || q1.v2.xyz.z < 0 || q1.v2.xyz.z > 1)) isCullable = false;
            if (isCullable && (q1.v3.xyz.x < 0 || q1.v3.xyz.x > 1 || q1.v3.xyz.y < 0 || q1.v3.xyz.y > 1 || q1.v3.xyz.z < 0 || q1.v3.xyz.z > 1)) isCullable = false;

            // check if all vertices are on the right face of the block
            if (isCullable && !filter.mul(q1.v0.xyz).equals(cmp)) isCullable = false;
            if (isCullable && !filter.mul(q1.v1.xyz).equals(cmp)) isCullable = false;
            if (isCullable && !filter.mul(q1.v2.xyz).equals(cmp)) isCullable = false;
            if (isCullable && !filter.mul(q1.v3.xyz).equals(cmp)) isCullable = false;

            if (isCullable) l.get(f).add(baked);
            else l.get(null).add(baked);
        }

        return l;
    }

    protected static <T> T select(@Nullable T t, T fallback) {
        return t == null ? fallback : t;
    }

    protected static <T> T select(@Nullable T t, Predicate<T> filter, T fallback) {
        return t == null || !filter.test(t) ? fallback : t;
    }

    @Override
    public Collection<Identifier> getModelDependencies() {
        return Collections.emptySet();
    }

}
