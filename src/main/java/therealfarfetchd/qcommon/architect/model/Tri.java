package therealfarfetchd.qcommon.architect.model;

import net.minecraft.util.math.Direction;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public class Tri implements Face {

    public final TextureRef texture;
    public final Vertex v0;
    public final Vertex v1;
    public final Vertex v2;
    public final Color color;

    private List<Quad> quads;
    private List<Tri> tris;

    private Vec3 normal;
    private Direction facing;

    public Tri(TextureRef texture, Vertex v0, Vertex v1, Vertex v2, Color color) {
        this.texture = texture;
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.color = color;
    }

    @Override
    public TextureRef getTexture() {
        return texture;
    }

    @Override
    public Tri withTexture(TextureRef texture) {
        return new Tri(texture, v0, v1, v2, color);
    }

    @Override
    public Vec3 getNormal() {
        if (normal == null) {
            normal = v1.xyz.sub(v0.xyz).cross(v2.xyz.sub(v0.xyz)).getNormalized();
        }

        return normal;
    }

    @Override
    public Direction getFacing() {
        if (facing == null) {
            Vec3 n = getNormal();
            facing = Direction.getFacing(n.x, n.y, n.z);
        }

        return facing;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Face withColor(Color color) {
        return new Tri(texture, v0, v1, v2, color);
    }

    @Override
    public List<Quad> toQuads() {
        if (quads == null) {
            quads = Collections.singletonList(new Quad(texture, v0, v1, v2, v2, color));
        }

        return quads;
    }

    @Override
    public List<Tri> toTris() {
        if (tris == null) {
            tris = Collections.singletonList(this);
        }

        return tris;
    }

    @Override
    public Tri transform(Mat4 mat) {
        return new Tri(texture, v0.transform(mat), v1.transform(mat), v2.transform(mat), color);
    }

}
