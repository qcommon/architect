package therealfarfetchd.qcommon.architect.client;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public abstract class BaseModel implements IModel {

    protected final StateProvider sp;
    protected final Model model;
    protected final TextureMapper tm;

    public BaseModel(StateProvider sp, Model model) {
        this.sp = sp;
        this.model = model;
        this.tm = model.getTextureMapper().get(sp);
    }

    protected void addModelTextures(Collection<ResourceLocation> textures) {
        model.getParts().getPossibleValues().parallelStream()
            .flatMap(Collection::parallelStream)
            .flatMap($ -> $.getFaces().parallelStream())
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

    protected Map<EnumFacing, List<BakedQuad>> getQuads(VertexFormat format, Function<TextureRef, TextureAtlasSprite> mapper) {
        List<Quad> quadList = model.getParts().get(sp).parallelStream()
            .flatMap($ -> $.getFaces().parallelStream())
            .flatMap($ -> $.toQuads().parallelStream())
            .collect(Collectors.toList());

        return categorizeAndBake(quadList, quad -> QuadFactory.INSTANCE.bake(format, mapper, quad).get(0));
    }

    protected Map<EnumFacing, List<BakedQuad>> categorizeAndBake(List<Quad> quads, Function<Quad, BakedQuad> bake) {
        Map<EnumFacing, List<BakedQuad>> l = new HashMap<>();

        for (EnumFacing f : EnumFacing.VALUES) l.put(f, new ArrayList<>());
        l.put(null, new ArrayList<>());

        for (Quad q : quads) {
            q = snapToGrid(q);

            BakedQuad baked = bake.apply(q);

            boolean isCullable = true;
            EnumFacing f = q.getFacing();
            Vec3 direction = Vec3.from(f.getDirectionVec());
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

    protected static <T> T select(@Nullable T t, T fallback) {
        return t == null ? fallback : t;
    }

}
