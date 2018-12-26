package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryRotate implements TransformFactory {

    @Override
    public AffineTransform parse(ParseContext ctx, JsonObject json) {
        Value<Vec3> axis = ctx.dp.parseAxisVector(ctx.log, json, "axis").map(Vec3::getNormalized);
        Value<Float> angle = ctx.dp.parseFloat(ctx.log, json, "angle");

        return sp -> {
            Vec3 axis1 = axis.get(sp);
            float angle1 = angle.get(sp);
            return Mat4.IDENTITY.rotate(axis1.x, axis1.y, axis1.z, angle1);
        };
    }

}
