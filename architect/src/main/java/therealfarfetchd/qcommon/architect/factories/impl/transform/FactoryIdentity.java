package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;

public class FactoryIdentity implements TransformFactory {

    public static final Transform IDENTITY = f -> f;

    @Override
    public Transform parse(ParseContext ctx, JsonObject json) {
        return IDENTITY;
    }

}
