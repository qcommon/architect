package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.math.Direction;

import java.util.EnumMap;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartBox;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryBox implements PartFactory {

    private static final EnumMap<Direction, Value<BoxFace>> INITIAL_TEXTURES = new EnumMap<>(Direction.class);
    private static final Vec3 FROM = Vec3.ORIGIN;
    private static final Vec3 TO = new Vec3(1, 1, 1);

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        EnumMap<Direction, Value<BoxFace>> em = INITIAL_TEXTURES.clone();
        Value<Vec3> from = Value.wrap(FROM);
        Value<Vec3> to = Value.wrap(TO);

        if (ctx.dp.hasKey(json, "from")) from = ctx.dp.parseCoords3D(ctx.log, json, "from", FROM);
        if (ctx.dp.hasKey(json, "to")) to = ctx.dp.parseCoords3D(ctx.log, json, "to", TO);

        if (json.has("faces")) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, "faces", "an object", $ -> true, $ -> $, new JsonObject());

            for (Direction f : Direction.values()) {
                em.put(f, apply(ctx, em.get(f), jo, "all"));
                em.put(f, apply(ctx, em.get(f), jo, f.getName()));
            }
        }

        Value<Vec3> finalFrom = from;
        Value<Vec3> finalTo = to;

        return Value.extract(em).flatMap(em1 -> finalFrom.flatMap(from1 -> finalTo.map(to1 -> new PartBox(em1, from1, to1))));
    }

    private Value<BoxFace> apply(ParseContext ctx, Value<BoxFace> current, JsonObject json, String key) {
        if (json.has(key)) {
            return ctx.dp.parseGenObjectStatic(ctx.log, json, key, "a face definition", $ -> true, jo -> parse(ctx, current, jo), current);
        } else return current;
    }

    private Value<BoxFace> parse(ParseContext ctx, Value<BoxFace> current, JsonObject json) {
        if (ctx.dp.hasKey(json, "show")) {
            current = current.flatMap(face -> ctx.dp.parseBoolean(ctx.log, json, "show").map(face::show));
        }

        if (ctx.dp.hasKey(json, "texture")) {
            current = current.flatMap(face -> ctx.dp.parseTextureRef(ctx.log, json, "texture").map(face::withTexture));
        }

        if (ctx.dp.hasKey(json, "uv")) {
            Value<Vec2[]> uvsv = ctx.dp.parseGenPrimitiveArray(ctx.log, json, "uv", "number", 4, JsonPrimitive::isNumber,
                l -> new Vec2[]{new Vec2(l.get(0).getAsFloat() / ctx.posScale, l.get(1).getAsFloat() / ctx.posScale), new Vec2(l.get(2).getAsFloat() / ctx.posScale, l.get(3).getAsFloat() / ctx.posScale)},
                new Vec2[]{Vec2.ORIGIN, new Vec2(1, 1)});
            current = current.flatMap($ -> uvsv.map(uvs -> $.withUV(uvs[0], uvs[1])));
        }

        return current;
    }

    static {
        for (Direction f : Direction.values()) {
            INITIAL_TEXTURES.put(f, Value.wrap(new BoxFace(TextureRef.PLACEHOLDER)));
        }
    }

}
