package therealfarfetchd.qcommon.architect.factories;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Transform;

public interface TransformFactory {

    Transform parse(ParseContext ctx, JsonObject json);

}
