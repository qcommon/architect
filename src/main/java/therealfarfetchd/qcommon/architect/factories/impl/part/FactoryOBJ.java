package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.obj.OBJLoader;
import therealfarfetchd.qcommon.architect.loader.obj.PreparedOBJ;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartOBJ;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryOBJ implements PartFactory {

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        ResourceLocation rl = JsonParserUtils.parseGenStringStatic(ctx, json, "model", "an OBJ model location", $ -> true, ResourceLocation::new, new ResourceLocation("qcommon-architect:empty"));
        rl = new ResourceLocation(rl.getNamespace(), String.format("render/obj/%s.obj", rl.getPath()));

        OBJRoot obj = OBJLoader.INSTANCE.load(rl);

        if (obj == null) {
            ctx.error("Failed to load OBJ model.");
            return Value.wrap(Part.EMPTY);
        }

        return Value.wrap(new PartOBJ(PreparedOBJ.get(obj)));
    }

}