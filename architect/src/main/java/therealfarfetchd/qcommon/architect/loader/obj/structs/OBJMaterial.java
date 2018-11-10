package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.awt.Color;
import java.util.Objects;

import javax.annotation.Nullable;

public class OBJMaterial {

    public static final OBJMaterial FALLBACK = new OBJMaterial(Color.WHITE, 1.0f, null);

    public final Color diffuse;
    public final float transparency;
    @Nullable public final String diffuseTexture;

    public OBJMaterial(Color diffuse, float transparency, @Nullable String diffuseTexture) {
        this.diffuse = diffuse;
        this.transparency = transparency;
        this.diffuseTexture = diffuseTexture;
    }

    public OBJMaterial withDiffuse(Color diffuse) {
        return new OBJMaterial(diffuse, transparency, diffuseTexture);
    }

    public OBJMaterial withTransparency(float transparency) {
        return new OBJMaterial(diffuse, transparency, diffuseTexture);
    }

    public OBJMaterial withDiffuseTexture(@Nullable String diffuseTexture) {
        return new OBJMaterial(diffuse, transparency, diffuseTexture);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBJMaterial material = (OBJMaterial) o;
        return Float.compare(material.transparency, transparency) == 0 &&
            Objects.equals(diffuse, material.diffuse) &&
            Objects.equals(diffuseTexture, material.diffuseTexture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diffuse, transparency, diffuseTexture);
    }

    @Override
    public String toString() {
        return "OBJMaterial{" +
            "diffuse=" + diffuse +
            ", transparency=" + transparency +
            ", diffuseTexture='" + diffuseTexture + '\'' +
            '}';
    }

}
