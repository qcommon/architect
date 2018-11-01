package therealfarfetchd.qcommon.architect.factories;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.loader.ParseContext;

public interface PartFactory {
    Part parse(ParseContext ctx, JsonObject json);
}
