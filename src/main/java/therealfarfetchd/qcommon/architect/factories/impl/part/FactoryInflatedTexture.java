package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.Identifier;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartInflatedTexture;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;

public class FactoryInflatedTexture implements PartFactory {

    @Override
    public Part parse(ParseContext ctx, JsonObject json) {
        Identifier tex = ctx.dp.parseGenStringStatic(ctx.log, json, "texture", "identifier", $ -> true, Identifier::new, TextureRef.PLACEHOLDER.texture);

        return new PartInflatedTexture(tex);
    }

}
