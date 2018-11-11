package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import therealfarfetchd.qcommon.croco.Vec3;

public class OBJRoot {

    public static final OBJRoot EMPTY = new OBJRoot(Collections.emptyMap(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyMap());

    public final Map<String, OBJMaterial> materials;

    public final List<Vec3> vertPos;
    public final List<Vec3> vertTex;
    public final List<Vec3> vertNormal;

    // these don't belong to any object
    public final List<OBJFace> faces;
    public final Map<String, OBJObject> objects;

    public OBJRoot(Map<String, OBJMaterial> materials, List<Vec3> vertPos, List<Vec3> vertTex, List<Vec3> vertNormal, List<OBJFace> faces, Map<String, OBJObject> objects) {
        this.materials = materials;
        this.vertPos = vertPos;
        this.vertTex = vertTex;
        this.vertNormal = vertNormal;
        this.faces = faces;
        this.objects = objects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBJRoot objRoot = (OBJRoot) o;
        return Objects.equals(materials, objRoot.materials) &&
            Objects.equals(vertPos, objRoot.vertPos) &&
            Objects.equals(vertTex, objRoot.vertTex) &&
            Objects.equals(vertNormal, objRoot.vertNormal) &&
            Objects.equals(faces, objRoot.faces) &&
            Objects.equals(objects, objRoot.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(materials, vertPos, vertTex, vertNormal, faces, objects);
    }

    @Override
    public String toString() {
        return "OBJRoot{" +
            "materials=" + materials +
            ", vertPos=" + vertPos +
            ", vertTex=" + vertTex +
            ", vertNormal=" + vertNormal +
            ", faces=" + faces +
            ", objects=" + objects +
            '}';
    }

}
