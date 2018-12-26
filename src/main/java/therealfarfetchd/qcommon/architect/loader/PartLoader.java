package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.part.Part;

public class PartLoader extends GenLoaderJSON<Part> {

    public static final PartLoader INSTANCE = new PartLoader();

    @Override
    public Part load(ParseMessageContainer log, SourceFileInfo info, JsonObject json) {
        ParseContext ctx = ParseContext.wrap(log);

        if (json.has("scale")) {
            ctx = ctx.withScale(ctx.dp.parseFloatStatic(ctx.log, json, "scale", ctx.posScale));
        }

        return ctx.dp.parsePart(ctx.log, json);
    }

    @Override
    protected Part getError() {
        return Part.EMPTY;
    }

    @Override
    protected String getTypeName() {
        return "part";
    }

}
