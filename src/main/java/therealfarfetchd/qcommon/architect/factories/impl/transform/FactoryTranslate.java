package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryTranslate implements TransformFactory {

    @Override
    public AffineTransform parse(ParseContext ctx, JsonObject json) {
        Value<Vec3> offset = ctx.dp.parseCoords3D(ctx.log, json, "offset");

        return sp -> {
            Vec3 offset1 = offset.get(sp);
            return Mat4.IDENTITY.translate(offset1.x, offset1.y, offset1.z);
        };
    }

}
