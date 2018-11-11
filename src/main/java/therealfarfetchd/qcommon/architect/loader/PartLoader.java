package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class PartLoader extends GenLoaderJSON<Value<Part>> {

    public static final PartLoader INSTANCE = new PartLoader();

    @Override
    public Value<Part> load(ParseContext ctx, SourceFileInfo info, JsonObject json) {
        return JsonParserUtils.parsePart(ctx, json);
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
