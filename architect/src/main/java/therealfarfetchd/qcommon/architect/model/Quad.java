package therealfarfetchd.qcommon.architect.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.math.Mat4;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;

public class Quad implements Face {

    public final TextureRef texture;
    public final Vertex v0;
    public final Vertex v1;
    public final Vertex v2;
    public final Vertex v3;

    private List<Quad> quads;
    private List<Tri> tris;

    public Quad(TextureRef texture, Vertex v0, Vertex v1, Vertex v2, Vertex v3) {
        this.texture = texture;
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
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
            l.add(new Tri(texture, v0, v1, v2));
            l.add(new Tri(texture, v2, v3, v0));
            tris = Collections.unmodifiableList(l);
        }

        return tris;
    }

    @Override
    public Quad transform(Mat4 mat) {
        return new Quad(texture, v0.transform(mat), v1.transform(mat), v2.transform(mat), v3.transform(mat));
    }

}
