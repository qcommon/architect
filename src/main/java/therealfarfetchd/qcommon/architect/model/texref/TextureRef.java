package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;

public interface TextureRef {

    TextureRefAbsolute PLACEHOLDER = new TextureRefAbsolute(new ResourceLocation(Architect.MODID, "pablo"));

    ResourceLocation getTexture(TextureMapper tm);

    String toStringRepr();

    static TextureRef fromString(@Nullable String texture) {
        if (texture == null) return PLACEHOLDER;
        if (texture.startsWith("#")) return new TextureRefKey(texture.substring(1));
        else return new TextureRefAbsolute(new ResourceLocation(texture));
    }

}
