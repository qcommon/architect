package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Transform;

public class FactoryIdentity implements TransformFactory {

    @Override
    public Transform parse(ParseContext ctx, JsonObject json) {
        return Transform.IDENTITY;
    }

}
