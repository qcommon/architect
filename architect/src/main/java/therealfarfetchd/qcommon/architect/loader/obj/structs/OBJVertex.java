package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.util.Objects;

import javax.annotation.Nullable;

public class OBJVertex {

    public final int xyz;
    @Nullable public final Integer tex;
    @Nullable public final Integer normal;

    public OBJVertex(int xyz, @Nullable Integer tex, @Nullable Integer normal) {
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
            Objects.equals(tex, objVertex.tex) &&
            Objects.equals(normal, objVertex.normal);
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
