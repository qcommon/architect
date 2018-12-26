package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.math.Direction;

import java.util.Optional;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.tuple.Pair;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartBox;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryBox implements PartFactory {

    public static final FactoryBox INSTANCE = new FactoryBox();

    private static final BoxFace[] INITIAL_TEXTURES = new BoxFace[6];
    private static final Vec3 FROM = Vec3.ORIGIN;
    private static final Vec3 TO = new Vec3(1, 1, 1);

    @Override
    public Part parse(ParseContext ctx, JsonObject json) {
        BoxFace[] em = INITIAL_TEXTURES.clone();
        Value<Vec3> from = Value.wrap(FROM);
        Value<Vec3> to = Value.wrap(TO);

        if (ctx.dp.hasKey(json, "from")) from = ctx.dp.parseCoords3D(ctx.log, json, "from", FROM);
        if (ctx.dp.hasKey(json, "to")) to = ctx.dp.parseCoords3D(ctx.log, json, "to", TO);

        if (json.has("faces")) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, "faces", "an object", $ -> true, $ -> $, new JsonObject());

            for (Direction f : Direction.values()) {
                final int id = f.getId();
                apply(ctx, em[id], jo, "all");
                apply(ctx, em[id], jo, f.getName());
            }
        }

        return new PartBox(from, to, em);
    }

    private void apply(ParseContext ctx, BoxFace current, JsonObject json, String key) {
        if (json.has(key)) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, key, "a face definition", $ -> true, $ -> $, null);
            parse(ctx, current, jo);
        }
    }

    private void parse(ParseContext ctx, BoxFace current, JsonObject json) {
        if (ctx.dp.hasKey(json, "show")) {
            current.setShow(ctx.dp.parseBoolean(ctx.log, json, "show"));
        }

        if (ctx.dp.hasKey(json, "texture")) {
            current.setTexture(ctx.dp.parseTextureRef(ctx.log, json, "texture"));
        }

        if (ctx.dp.hasKey(json, "uv")) {
            Value<Vec2[]> uvsv = ctx.dp.parseGenPrimitiveArray(ctx.log, json, "uv", "number", 4, JsonPrimitive::isNumber,
                l -> new Vec2[]{new Vec2(l.get(0).getAsFloat() / ctx.posScale, l.get(1).getAsFloat() / ctx.posScale), new Vec2(l.get(2).getAsFloat() / ctx.posScale, l.get(3).getAsFloat() / ctx.posScale)},
                new Vec2[]{Vec2.ORIGIN, new Vec2(1, 1)});
            current.setUV(uvsv.map(arr -> Optional.of(Pair.of(arr[0], arr[1]))));
        }
    }

    static {
        for (int i = 0; i < INITIAL_TEXTURES.length; i++) {
            INITIAL_TEXTURES[i] = new BoxFace();
        }
    }

}
