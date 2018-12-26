package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryScale implements TransformFactory {

    @Override
    public AffineTransform parse(ParseContext ctx, JsonObject json) {
        Value<Vec3> scale = ctx.dp.parseVec3(ctx.log, json, "scale", new Vec3(1, 1, 1));

        return sp -> {
            Vec3 scale1 = scale.get(sp);
            return Mat4.IDENTITY.scale(scale1.x, scale1.y, scale1.z);
        };
    }

}
