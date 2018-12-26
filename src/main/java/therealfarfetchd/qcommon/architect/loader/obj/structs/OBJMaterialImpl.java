package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.awt.Color;
import java.util.Objects;

import javax.annotation.Nullable;

public class OBJMaterialImpl implements OBJMaterial.Mutable {

    private Color diffuse;
    private float transparency;
    @Nullable private String diffuseTexture;

    public OBJMaterialImpl(Color diffuse, float transparency, @Nullable String diffuseTexture) {
        this.diffuse = diffuse;
        this.transparency = transparency;
        this.diffuseTexture = diffuseTexture;
    }

    @Override
    public OBJMaterialImpl withDiffuse(Color diffuse) {
        return new OBJMaterialImpl(diffuse, transparency, diffuseTexture);
    }

    @Override
    public Color getDiffuse() {
        return diffuse;
    }

    @Override
    public OBJMaterialImpl withTransparency(float transparency) {
        return new OBJMaterialImpl(diffuse, transparency, diffuseTexture);
    }

    @Override
    public float getTransparency() {
        return transparency;
    }

    @Override
    public OBJMaterialImpl withDiffuseTexture(@Nullable String diffuseTexture) {
        return new OBJMaterialImpl(diffuse, transparency, diffuseTexture);
    }

    @Nullable
    @Override
    public String getDiffuseTexture() {
        return diffuseTexture;
    }

    @Override
    public void setDiffuse(Color diffuse) {
        this.diffuse = diffuse;
    }

    @Override
    public void setTransparency(float transparency) {
        this.transparency = transparency;
    }

    @Override
    public void setDiffuseTexture(@Nullable String diffuseTexture) {
        this.diffuseTexture = diffuseTexture;
    }

    @Override
    public OBJMaterialImpl copy() {
        return new OBJMaterialImpl(diffuse, transparency, diffuseTexture);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OBJMaterialImpl material = (OBJMaterialImpl) o;
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
        return "OBJMaterialImpl{" +
            "diffuse=" + diffuse +
            ", transparency=" + transparency +
            ", diffuseTexture='" + diffuseTexture + '\'' +
            '}';
    }
}
