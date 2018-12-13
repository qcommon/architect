package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public interface TextureRef {

    TextureRefAbsolute PLACEHOLDER = new TextureRefAbsolute(new Identifier(Architect.MODID, "pablo"));

    Identifier getTexture(TextureMapper tm);

    String toStringRepr();

    static TextureRef fromString(@Nullable String texture) {
        if (texture == null) return PLACEHOLDER;

        try {
            if (texture.startsWith("#")) return new TextureRefKey(texture.substring(1));
            else return new TextureRefAbsolute(new Identifier(texture));
        } catch (Exception e) {
            return PLACEHOLDER;
        }
    }

}
