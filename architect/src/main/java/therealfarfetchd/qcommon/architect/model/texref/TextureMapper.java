package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public interface TextureMapper {

    @Nullable
    ResourceLocation getTexture(String key);

}
