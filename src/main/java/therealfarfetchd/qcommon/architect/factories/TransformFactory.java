package therealfarfetchd.qcommon.architect.factories;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.Value;

public interface TransformFactory {
    Value<Transform> parse(ParseContext ctx, JsonObject json);
}
