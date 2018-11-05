package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryRotate implements TransformFactory {

    @Override
    public Value<Transform> parse(ParseContext ctx, JsonObject json) {
        Value<Vec3> axis = JsonParserUtils.parseVec3(ctx, json, "axis").map(Vec3::getNormalized);
        Value<Float> angle = JsonParserUtils.parseFloat(ctx, json, "angle");

        return axis.flatMap(axis1 -> angle.map(angle1 -> f -> f.rotate(axis1, angle1)));
    }

}
