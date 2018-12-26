package therealfarfetchd.qcommon.architect.loader.obj.structs;

import java.awt.Color;

import javax.annotation.Nullable;

public interface OBJMaterial {

    OBJMaterial FALLBACK = new OBJMaterialImpl(Color.WHITE, 1.0f, null);

    OBJMaterial withDiffuse(Color diffuse);

    Color getDiffuse();

    OBJMaterial withTransparency(float transparency);

    float getTransparency();

    OBJMaterial withDiffuseTexture(@Nullable String diffuseTexture);

    OBJMaterial.Mutable copy();

    @Nullable
    String getDiffuseTexture();

    interface Mutable extends OBJMaterial {

        void setDiffuse(Color diffuse);

        void setTransparency(float transparency);

        void setDiffuseTexture(@Nullable String diffuseTexture);

    }

}
