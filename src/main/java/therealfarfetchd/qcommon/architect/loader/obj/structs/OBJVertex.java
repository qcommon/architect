package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.util.Objects;

public class OBJVertex {

    public final int xyz;
    public final int tex;
    public final int normal;

    public OBJVertex(int xyz, int tex, int normal) {
        if (xyz == 0) throw new IllegalArgumentException("Vertex needs xyz coordinates!");

        this.xyz = xyz;
        this.tex = tex;
        this.normal = normal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBJVertex objVertex = (OBJVertex) o;
        return xyz == objVertex.xyz &&
            tex == objVertex.tex &&
            normal == objVertex.normal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xyz, tex, normal);
    }

    @Override
    public String toString() {
        return "OBJVertex{" +
            "xyz=" + xyz +
            ", tex=" + tex +
            ", normal=" + normal +
            '}';
    }

}
