package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryIdentity implements TransformFactory {

    public static final Transform IDENTITY = f -> f;
    public static final Value<Transform> IDENTITY_V = Value.wrap(IDENTITY);

    @Override
    public Value<Transform> parse(ParseContext ctx, JsonObject json) {
        return IDENTITY_V;
    }

}
