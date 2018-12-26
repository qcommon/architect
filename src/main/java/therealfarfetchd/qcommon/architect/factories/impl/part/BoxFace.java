package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.math.Direction;

import java.awt.Color;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Quad;
import therealfarfetchd.qcommon.architect.model.Vertex;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class BoxFace {

    private Value<Boolean> show;
    private Value<TextureRef> texture;
    private Value<Optional<Pair<Vec2, Vec2>>> uvs;

    public BoxFace() {
        this.show = Value.wrap(true);
        this.texture = Value.wrap(TextureRef.PLACEHOLDER);
        this.uvs = Value.wrap(Optional.empty());
    }

    public void setShow(Value<Boolean> show) {
        this.show = show;
    }

    public Value<Boolean> getShow() {
        return this.show;
    }

    public void setUV(Value<Optional<Pair<Vec2, Vec2>>> uvs) {
        this.uvs = uvs;
    }

    public Value<Optional<Pair<Vec2, Vec2>>> getUVs() {
        return this.uvs;
    }

    public void setTexture(Value<TextureRef> texture) {
        this.texture = texture;
    }

    public Value<TextureRef> getTexture() {
        return this.texture;
    }

    public static Face makeFace(Direction face, TextureRef texture, Vec3 from, Vec3 to, @Nullable Vec2 uv1, @Nullable Vec2 uv2) {
        Vec3[] v = new Vec3[4];
        Vec2[] uv = new Vec2[4];
        getVec(face, from, to, v);

        if (uv1 != null && uv2 != null) {
            uv[0] = uv1;
            uv[2] = uv2;
            uv[1] = new Vec2(uv[0].x, uv[2].y);
            uv[3] = new Vec2(uv[2].x, uv[0].y);
        } else {
            getUV(face, from, to, uv);
        }

        return new Quad(texture, new Vertex(v[0], uv[0]), new Vertex(v[1], uv[1]), new Vertex(v[2], uv[2]), new Vertex(v[3], uv[3]), Color.WHITE);
    }

    private static void getVec(Direction face, Vec3 from, Vec3 to, Vec3[] v) {
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

    private static void getUV(Direction face, Vec3 from, Vec3 to, Vec2[] uv) {
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
