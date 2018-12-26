package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;

public class FactoryMatrix implements TransformFactory {

    @Override
    public AffineTransform parse(ParseContext ctx, JsonObject json) {
        Value<Mat4> mat = ctx.dp.parseMat4(ctx.log, json, "mat");

        return mat::get;
    }

}
