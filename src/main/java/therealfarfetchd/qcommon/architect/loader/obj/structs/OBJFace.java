package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class OBJFace {

    @Nullable public final String material;
    public final List<OBJVertex> vertices;

    public OBJFace(@Nullable String material, List<OBJVertex> vertices) {
        this.material = material;
        this.vertices = vertices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBJFace objFace = (OBJFace) o;
        return Objects.equals(material, objFace.material) &&
            Objects.equals(vertices, objFace.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(material, vertices);
    }

    @Override
    public String toString() {
        return "OBJFace{" +
            "material='" + material + '\'' +
            ", vertices=" + vertices +
            '}';
    }

}
