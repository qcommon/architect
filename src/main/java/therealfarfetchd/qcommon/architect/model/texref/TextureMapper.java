package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

public interface TextureMapper {

    @Nullable
    Identifier getTexture(String key);

}
