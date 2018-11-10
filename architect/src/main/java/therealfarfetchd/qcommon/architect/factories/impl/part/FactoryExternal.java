package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.PartLoader;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryExternal implements PartFactory {

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        ResourceLocation rl = JsonParserUtils.parseGenStringStatic(ctx, json, "part", "a part location", $ -> true, ResourceLocation::new, new ResourceLocation("qcommon-architect:empty"));
        rl = new ResourceLocation(rl.getNamespace(), String.format("render/part/%s.json", rl.getPath()));

        final Value<Part> part = PartLoader.INSTANCE.load(rl);
        return part == null ? Value.wrap(Part.EMPTY) : part;
    }

}
