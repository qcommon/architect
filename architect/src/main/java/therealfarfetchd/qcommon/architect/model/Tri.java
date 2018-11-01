package therealfarfetchd.qcommon.architect.model;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.math.Mat4;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;

public class Tri implements Face {

    public final TextureRef texture;
    public final Vertex v0;
    public final Vertex v1;
    public final Vertex v2;

    private List<Quad> quads;
    private List<Tri> tris;

    public Tri(TextureRef texture, Vertex v0, Vertex v1, Vertex v2) {
        this.texture = texture;
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    @Override
    public List<Quad> toQuads() {
        if (quads == null) {
            quads = Collections.singletonList(new Quad(texture, v0, v1, v2, v2));
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
        return new Tri(texture, v0.transform(mat), v1.transform(mat), v2.transform(mat));
    }

}
