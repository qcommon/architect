package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.util.List;
import java.util.Objects;

public class OBJObject {

    public final List<String> groups;
    public final List<OBJFace> faces;

    public OBJObject(List<String> groups, List<OBJFace> faces) {
        this.groups = groups;
        this.faces = faces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBJObject objObject = (OBJObject) o;
        return Objects.equals(groups, objObject.groups) &&
            Objects.equals(faces, objObject.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, faces);
    }

    @Override
    public String toString() {
        return "OBJObject{" +
            "groups=" + groups +
            ", faces=" + faces +
            '}';
    }

}
