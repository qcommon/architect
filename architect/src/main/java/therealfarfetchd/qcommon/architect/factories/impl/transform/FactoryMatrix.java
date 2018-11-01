package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.math.Mat4;

public class FactoryMatrix implements TransformFactory {

    @Override
    public Transform parse(ParseContext ctx, JsonObject json) {
        Mat4 mat = JsonParserUtils.parseMat4(ctx, json, "mat");

        return f -> f.transform(mat);
    }

}
