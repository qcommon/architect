package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;

public class FactoryIdentity implements TransformFactory {

    public static final AffineTransform IDENTITY = () -> Mat4.IDENTITY;
    public static final Value<Transform> IDENTITY_V = Value.wrap(IDENTITY);

    @Override
    public Value<Transform> parse(ParseContext ctx, JsonObject json) {
        return IDENTITY_V;
    }

}
