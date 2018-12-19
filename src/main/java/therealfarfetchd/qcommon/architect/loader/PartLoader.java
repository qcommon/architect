package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class PartLoader extends GenLoaderJSON<Value<Part>> {

    public static final PartLoader INSTANCE = new PartLoader();

    @Override
    public Value<Part> load(ParseMessageContainer log, SourceFileInfo info, JsonObject json) {
        ParseContext ctx = ParseContext.wrap(log);

        if (json.has("scale")) {
            ctx = ctx.withScale(ctx.dp.parseFloatStatic(ctx.log, json, "scale", ctx.posScale));
        }

        return ctx.dp.parsePart(ctx.log, json);
    }

    @Override
    protected Value<Part> getError() {
        return Value.wrap(Part.EMPTY);
    }

    @Override
    protected String getTypeName() {
        return "part";
    }

}
