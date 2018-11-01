package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.math.Vec3;

public class FactoryScale implements TransformFactory {

    @Override
    public Transform parse(ParseContext ctx, JsonObject json) {
        Vec3 scale = JsonParserUtils.parseVec3(ctx, json, "scale", new Vec3(1, 1, 1));

        return f -> f.scale(scale);
    }

}
