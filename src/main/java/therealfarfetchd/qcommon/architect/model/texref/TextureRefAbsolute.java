package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.Identifier;

import java.util.Objects;

public class TextureRefAbsolute implements TextureRef {

    public final Identifier texture;

    public TextureRefAbsolute(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public Identifier getTexture(TextureMapper tm) {
        return texture;
    }

    @Override
    public String toStringRepr() {
        return texture.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextureRefAbsolute that = (TextureRefAbsolute) o;
        return Objects.equals(texture, that.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture);
    }

    @Override
    public String toString() {
        return texture.toString();
    }

}
