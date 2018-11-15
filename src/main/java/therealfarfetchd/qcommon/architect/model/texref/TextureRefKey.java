package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.ResourceLocation;

import java.util.Objects;

import therealfarfetchd.qcommon.architect.Architect;

public class TextureRefKey implements TextureRef {

    public final String key;

    public TextureRefKey(String key) {
        this.key = key;
    }

    @Override
    public ResourceLocation getTexture(TextureMapper tm) {
        ResourceLocation rl = tm.getTexture(key);
        // TODO warning message for missing key
        return rl != null ? rl : new ResourceLocation(Architect.MODID, "pablo");
    }

    @Override
    public String toStringRepr() {
        return String.format("#%s", key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureRefKey that = (TextureRefKey) o;
        return Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return toStringRepr();
    }

}
