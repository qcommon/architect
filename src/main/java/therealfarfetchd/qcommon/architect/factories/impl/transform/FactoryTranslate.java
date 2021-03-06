package therealfarfetchd.qcommon.architect.factories.impl.transform;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryTranslate implements TransformFactory {

    @Override
    public Value<Transform> parse(ParseContext ctx, JsonObject json) {
        Value<Vec3> offset = JsonParserUtils.parseVec3(ctx, json, "offset");

        return offset.map(offset1 -> f -> f.translate(offset1));
    }

}
