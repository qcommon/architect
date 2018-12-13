package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import net.minecraft.util.Identifier;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.PartLoader;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryExternal implements PartFactory {

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        Identifier id = JsonParserUtils.parseGenStringStatic(ctx, json, "part", "a part location", $ -> true, Identifier::new, new Identifier("qcommon-architect:empty"));
        id = new Identifier(id.getNamespace(), String.format("render/part/%s.json", id.getPath()));

        final Value<Part> part = PartLoader.INSTANCE.load(id);
        return part == null ? Value.wrap(Part.EMPTY) : part;
    }

}
