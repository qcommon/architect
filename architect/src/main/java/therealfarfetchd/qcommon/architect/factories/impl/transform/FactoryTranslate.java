package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.math.Vec3;

public class FactoryTranslate implements TransformFactory {

    @Override
    public Transform parse(ParseContext ctx, JsonObject json) {
        Vec3 offset = JsonParserUtils.parseVec3(ctx, json, "offset");

        return f -> f.translate(offset);
    }

}
