package therealfarfetchd.qcommon.architect.model;

import net.minecraft.util.EnumFacing;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public class Quad implements Face {

    public final TextureRef texture;
    public final Vertex v0;
    public final Vertex v1;
    public final Vertex v2;
    public final Vertex v3;
    public final Color color;

    private List<Quad> quads;
    private List<Tri> tris;
    private Vec3 normal;
    private EnumFacing facing;

    public Quad(TextureRef texture, Vertex v0, Vertex v1, Vertex v2, Vertex v3, Color color) {
        this.texture = texture;
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.color = color;
    }

    @Override
    public TextureRef getTexture() {
        return texture;
    }

    @Override
    public Vec3 getNormal() {
        if (normal == null) {
            normal = toTris().get(0).getNormal();
        }

        return normal;
    }

    @Override
    public EnumFacing getFacing() {
        if (facing == null) {
            facing = toTris().get(0).getFacing();
        }

        return facing;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public List<Quad> toQuads() {
        if (quads == null) {
            quads = Collections.singletonList(this);
        }

        return quads;
    }

    @Override
    public List<Tri> toTris() {
        if (tris == null) {
            ArrayList<Tri> l = new ArrayList<>();
            l.add(new Tri(texture, v0, v1, v2, color));
            l.add(new Tri(texture, v2, v3, v0, color));
            tris = Collections.unmodifiableList(l);
        }

        return tris;
    }

    @Override
    public Quad transform(Mat4 mat) {
        return new Quad(texture, v0.transform(mat), v1.transform(mat), v2.transform(mat), v3.transform(mat), color);
    }

}
