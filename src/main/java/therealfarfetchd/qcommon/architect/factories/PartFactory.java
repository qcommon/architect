package therealfarfetchd.qcommon.architect.factories;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.part.Part;

public interface PartFactory {

    Part parse(ParseContext ctx, JsonObject json);

}
