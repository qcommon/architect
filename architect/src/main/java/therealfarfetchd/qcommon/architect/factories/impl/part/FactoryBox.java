package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class FactoryBox implements PartFactory {

    private static final EnumMap<EnumFacing, Value<BoxFace>> INITIAL_TEXTURES = new EnumMap<>(EnumFacing.class);
    private static final Vec3 FROM = Vec3.ORIGIN;
    private static final Vec3 TO = new Vec3(1, 1, 1);

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        EnumMap<EnumFacing, Value<BoxFace>> em = INITIAL_TEXTURES.clone();
        Vec3 from = FROM;
        Vec3 to = TO;

        if (json.has("from")) from = JsonParserUtils.parseVec3Static(ctx, json, "from", from);
        if (json.has("to")) to = JsonParserUtils.parseVec3Static(ctx, json, "to", to);

        if (json.has("faces")) {
            JsonObject jo = JsonParserUtils.parseGenObjectStatic(ctx, json, "faces", "an object", $ -> true, $ -> $, new JsonObject());

            for (EnumFacing f : EnumFacing.VALUES) {
                em.put(f, apply(ctx, em.get(f), jo, "all"));
                em.put(f, apply(ctx, em.get(f), jo, f.getName()));
            }
        }

        return Value.wrap(new PartBox(em, from, to));
    }

    private Value<BoxFace> apply(ParseContext ctx, Value<BoxFace> current, JsonObject json, String key) {
        if (json.has(key)) {
            return JsonParserUtils.parseGenObjectStatic(ctx, json, key, "a face definition", $ -> true, jo -> parse(ctx, current, jo), current);
        } else return current;
    }

    private Value<BoxFace> parse(ParseContext ctx, Value<BoxFace> current, JsonObject json) {
        if (json.has("show")) {
            current = current.flatMap(face -> JsonParserUtils.parseBoolean(ctx, json, "show").map(face::show));
        }

        if (json.has("texture")) {
            current = current.flatMap(face -> JsonParserUtils.parseTextureRef(ctx, json, "texture").map(face::withTexture));
        }

        if (json.has("uv")) {
            Value<Vec2[]> uvsv = JsonParserUtils.parseGenPrimitiveArray(ctx, json, "uv", "number", 4, JsonPrimitive::isNumber,
                l -> new Vec2[]{new Vec2(l.get(0).getAsFloat(), l.get(1).getAsFloat()), new Vec2(l.get(2).getAsFloat(), l.get(3).getAsFloat())},
                new Vec2[]{Vec2.ORIGIN, new Vec2(1, 1)});
            current = current.flatMap($ -> uvsv.map(uvs -> $.withUV(uvs[0], uvs[1])));
        }

        return current;
    }

    static {
        for (EnumFacing f : EnumFacing.VALUES) {
            INITIAL_TEXTURES.put(f, Value.wrap(new BoxFace(TextureRef.PLACEHOLDER)));
        }
    }

}
