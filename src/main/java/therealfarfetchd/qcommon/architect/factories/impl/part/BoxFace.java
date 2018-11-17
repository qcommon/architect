package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.EnumFacing;

import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.util.Optional;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class BoxFace {

    public final boolean show;
    public final TextureRef texture;

    @Nullable public final Pair<Vec2, Vec2> uvs;

    public BoxFace(TextureRef texture) {
        this(true, texture);
    }

    public BoxFace(TextureRef texture, Vec2 uv1, Vec2 uv2) {
        this(true, texture, uv1, uv2);
    }

    public BoxFace(boolean show, TextureRef texture) {
        this.show = show;
        this.texture = texture;
        this.uvs = null;
    }

    public BoxFace(boolean show, TextureRef texture, Vec2 uv1, Vec2 uv2) {
        this.show = show;
        this.texture = texture;
        this.uvs = Pair.of(uv1, uv2);
    }

    private BoxFace(boolean show, TextureRef texture, @Nullable Pair<Vec2, Vec2> uvs) {
        this.show = show;
        this.texture = texture;
        this.uvs = uvs;
    }

    public Optional<Face> makeFace(EnumFacing face, Vec3 from, Vec3 to) {
        Vec3[] v = new Vec3[4];
        Vec2[] uv = new Vec2[4];
        getVec(face, from, to, v);

        if (uvs != null) {

            uv[0] = uvs.getLeft();
            uv[2] = uvs.getRight();
            uv[1] = new Vec2(uv[0].x, uv[2].y);
            uv[3] = new Vec2(uv[2].x, uv[0].y);
        } else {
            getUV(face, from, to, uv);
        }

        return Optional.of(new Quad(texture, new Vertex(v[0], uv[0]), new Vertex(v[1], uv[1]), new Vertex(v[2], uv[2]), new Vertex(v[3], uv[3]), Color.WHITE));
    }

    public BoxFace show(boolean show) {
        if (show == this.show) return this;
        else return new BoxFace(show, texture, uvs);
    }

    public BoxFace withUV(Vec2 from, Vec2 to) {
        return new BoxFace(show, texture, from, to);
    }

    public BoxFace withAutoUV() {
        return new BoxFace(show, texture, null);
    }

    public BoxFace withTexture(TextureRef texture) {
        return new BoxFace(show, texture, uvs);
    }

    @Override
    public String toString() {
        if (uvs != null) {
            return String.format("BoxFace(show = %s, texture = %s, uv1 = %s, uv2 = %s)", show, texture, uvs.getLeft(), uvs.getRight());
        } else {
            return String.format("BoxFace(show = %s, texture = %s, uv1 = <auto>, uv2 = <auto>)", show, texture);
        }
    }

    private static void getVec(EnumFacing face, Vec3 from, Vec3 to, Vec3[] v) {
        switch (face) {
            case DOWN:
                v[1] = from;
                v[2] = new Vec3(to.x, from.y, from.z);
                v[3] = new Vec3(to.x, from.y, to.z);
                v[0] = new Vec3(from.x, from.y, to.z);
                break;
            case UP:
                v[0] = new Vec3(from.x, to.y, from.z);
                v[1] = new Vec3(from.x, to.y, to.z);
                v[2] = to;
                v[3] = new Vec3(to.x, to.y, from.z);
                break;
            case NORTH:
                v[2] = from;
                v[1] = new Vec3(to.x, from.y, from.z);
                v[0] = new Vec3(to.x, to.y, from.z);
                v[3] = new Vec3(from.x, to.y, from.z);
                break;
            case SOUTH:
                v[1] = new Vec3(from.x, from.y, to.z);
                v[0] = new Vec3(from.x, to.y, to.z);
                v[3] = to;
                v[2] = new Vec3(to.x, from.y, to.z);
                break;
            case WEST:
                v[1] = from;
                v[0] = new Vec3(from.x, to.y, from.z);
                v[3] = new Vec3(from.x, to.y, to.z);
                v[2] = new Vec3(from.x, from.y, to.z);
                break;
            case EAST:
                v[2] = new Vec3(to.x, from.y, from.z);
                v[1] = new Vec3(to.x, from.y, to.z);
                v[0] = to;
                v[3] = new Vec3(to.x, to.y, from.z);
                break;
            default:
                throw new IllegalStateException("face is null");
        }
    }

    private static void getUV(EnumFacing face, Vec3 from, Vec3 to, Vec2[] uv) {
        switch (face) {
            case DOWN:
                uv[0] = new Vec2(from.x, 1 - to.z);
                uv[2] = new Vec2(to.x, 1 - from.z);
                break;
            case UP:
                uv[0] = new Vec2(from.x, from.z);
                uv[2] = new Vec2(to.x, to.z);
                break;
            case NORTH:
                uv[0] = new Vec2(1 - to.x, 1 - to.y);
                uv[2] = new Vec2(1 - from.x, 1 - from.y);
                break;
            case SOUTH:
                uv[0] = new Vec2(from.x, 1 - to.y);
                uv[2] = new Vec2(to.x, 1 - from.y);
                break;
            case WEST:
                uv[0] = new Vec2(from.z, 1 - to.y);
                uv[2] = new Vec2(to.z, 1 - from.y);
                break;
            case EAST:
                uv[0] = new Vec2(1 - to.z, 1 - to.y);
                uv[2] = new Vec2(1 - from.z, 1 - from.y);
                break;
            default:
                throw new IllegalStateException("face is null");
        }

        uv[1] = new Vec2(uv[0].x, uv[2].y);
        uv[3] = new Vec2(uv[2].x, uv[0].y);
    }

}
