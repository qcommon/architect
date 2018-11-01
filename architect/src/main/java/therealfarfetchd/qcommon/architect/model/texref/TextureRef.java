package therealfarfetchd.qcommon.architect.model.texref;

import net.minecraft.util.ResourceLocation;

import therealfarfetchd.qcommon.architect.Architect;

public interface TextureRef {

    TextureRef PLACEHOLDER = new TextureRefAbsolute(new ResourceLocation(Architect.MODID, "pablo"));

    ResourceLocation getTexture(TextureMapper tm);

}
