package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.math.Vec3;

public class FactoryRotate implements TransformFactory {

    @Override
    public Transform parse(ParseContext ctx, JsonObject json) {
        Vec3 axis = JsonParserUtils.parseVec3(ctx, json, "axis").getNormalized();
        float angle = JsonParserUtils.parseFloat(ctx, json, "angle");

        return f -> f.rotate(axis, angle);
    }

}
