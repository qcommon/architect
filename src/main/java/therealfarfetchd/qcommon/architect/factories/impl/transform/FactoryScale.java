package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryScale implements TransformFactory {

    @Override
    public Value<Transform> parse(ParseContext ctx, JsonObject json) {
        Value<Vec3> scale = JsonParserUtils.parseVec3(ctx, json, "scale", new Vec3(1, 1, 1));

        return scale.map(scale1 -> AffineTransform.of(Mat4.IDENTITY.scale(scale1.x, scale1.y, scale1.z)));
    }

}
