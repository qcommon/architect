package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.factories.impl.transform.FactoryIdentity;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartTransformWrapper;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefAbsolute;
import therealfarfetchd.qcommon.architect.model.value.Matcher;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class JsonParserUtils {

    private JsonParserUtils() {}

    public static Value<Mat4> parseMat4(ParseContext ctx, JsonObject root, String tag) {
        return parseMat4(ctx, root, tag, Mat4.IDENTITY);
    }

    public static Value<Mat4> parseMat4(ParseContext ctx, JsonObject root, String tag, Mat4 fallback) {
        return parseGenPrimitiveArray(ctx, root, tag, "number", 16, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Mat4(
                floats.get(0), floats.get(1), floats.get(2), floats.get(3),
                floats.get(4), floats.get(5), floats.get(6), floats.get(7),
                floats.get(8), floats.get(9), floats.get(10), floats.get(11),
                floats.get(12), floats.get(13), floats.get(14), floats.get(15)
            );
        }, fallback);
    }

    public static Mat4 parseMat4Static(ParseContext ctx, JsonObject root, String tag) {
        return parseMat4Static(ctx, root, tag, Mat4.IDENTITY);
    }

    public static Mat4 parseMat4Static(ParseContext ctx, JsonObject root, String tag, Mat4 fallback) {
        return parseGenPrimitiveArrayStatic(ctx, root, tag, "number", 16, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Mat4(
                floats.get(0), floats.get(1), floats.get(2), floats.get(3),
                floats.get(4), floats.get(5), floats.get(6), floats.get(7),
                floats.get(8), floats.get(9), floats.get(10), floats.get(11),
                floats.get(12), floats.get(13), floats.get(14), floats.get(15)
            );
        }, fallback);
    }

    public static Value<Vec3> parseVec3(ParseContext ctx, JsonObject root, String tag) {
        return parseVec3(ctx, root, tag, Vec3.ORIGIN);
    }

    public static Value<Vec3> parseVec3(ParseContext ctx, JsonObject root, String tag, Vec3 fallback) {
        return parseGenPrimitiveArray(ctx, root, tag, "number", 3, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec3(floats.get(0), floats.get(1), floats.get(2));
        }, fallback);
    }

    public static Vec3 parseVec3Static(ParseContext ctx, JsonObject root, String tag) {
        return parseVec3Static(ctx, root, tag, Vec3.ORIGIN);
    }

    public static Vec3 parseVec3Static(ParseContext ctx, JsonObject root, String tag, Vec3 fallback) {
        return parseGenPrimitiveArrayStatic(ctx, root, tag, "number", 3, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec3(floats.get(0), floats.get(1), floats.get(2));
        }, fallback);
    }

    public static Value<Vec2> parseVec2(ParseContext ctx, JsonObject root, String tag) {
        return parseVec2(ctx, root, tag, Vec2.ORIGIN);
    }

    public static Value<Vec2> parseVec2(ParseContext ctx, JsonObject root, String tag, Vec2 fallback) {
        return parseGenPrimitiveArray(ctx, root, tag, "number", 2, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec2(floats.get(0), floats.get(1));
        }, fallback);
    }

    public static Vec2 parseVec2Static(ParseContext ctx, JsonObject root, String tag) {
        return parseVec2Static(ctx, root, tag, Vec2.ORIGIN);
    }

    public static Vec2 parseVec2Static(ParseContext ctx, JsonObject root, String tag, Vec2 fallback) {
        return parseGenPrimitiveArrayStatic(ctx, root, tag, "number", 2, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec2(floats.get(0), floats.get(1));
        }, fallback);
    }

    public static Value<Float> parseFloat(ParseContext ctx, JsonObject root, String tag) {
        return parseFloat(ctx, root, tag, 0.0f);
    }

    public static Value<Float> parseFloat(ParseContext ctx, JsonObject root, String tag, float fallback) {
        return parseGenPrimitive(ctx, root, tag, "a number", JsonPrimitive::isNumber, JsonPrimitive::getAsFloat, fallback);
    }

    public static float parseFloatStatic(ParseContext ctx, JsonObject root, String tag) {
        return parseFloatStatic(ctx, root, tag, 0.0f);
    }

    public static float parseFloatStatic(ParseContext ctx, JsonObject root, String tag, float fallback) {
        return parseGenPrimitiveStatic(ctx, root, tag, "a number", JsonPrimitive::isNumber, JsonPrimitive::getAsFloat, fallback);
    }

    public static Value<Boolean> parseBoolean(ParseContext ctx, JsonObject root, String tag) {
        return parseBoolean(ctx, root, tag, false);
    }

    public static Value<Boolean> parseBoolean(ParseContext ctx, JsonObject root, String tag, boolean fallback) {
        return parseGenPrimitive(ctx, root, tag, "a boolean", JsonPrimitive::isBoolean, JsonPrimitive::getAsBoolean, fallback);
    }

    public static Value<TextureRef> parseTextureRef(ParseContext ctx, JsonObject root, String tag) {
        return parseTextureRef(ctx, root, tag, new TextureRefAbsolute(new ResourceLocation(Architect.MODID, "error")));
    }

    public static Value<TextureRef> parseTextureRef(ParseContext ctx, JsonObject root, String tag, TextureRef fallback) {
        return parseGenString(ctx, root, tag, "a texture name or #-key", $ -> true, TextureRef::fromString, fallback);
    }

    public static Value<Part> parsePart(ParseContext ctx, JsonObject root, String tag) {
        return parseGenObjectStatic(ctx, root, tag, "a part", $ -> true, obj -> parsePart(ctx, obj), Value.wrap(Part.EMPTY));
    }

    public static Value<Part> parsePart(ParseContext ctx, JsonObject root) {
        PartFactory pf = parseGenStringStatic(ctx, root, "type", "a part type",
            s -> FactoryRegistry.INSTANCE.getPartFactory(new ResourceLocation(s)) != null,
            s -> FactoryRegistry.INSTANCE.getPartFactory(new ResourceLocation(s)),
            (p1, p2) -> Value.wrap(Part.EMPTY));

        final Value<Part> part = pf.parse(ctx, root);

        if (root.has("transform")) {
            Value<List<Transform>> trs = Value.extract(parseGenObjectArrayStatic(ctx, root, "transform", "transforms", -1,
                $ -> true,
                objs -> objs.stream().map(jo1 -> parseTransform(ctx, jo1)).collect(Collectors.toList()),
                Collections.emptyList()));

            return trs.flatMap(list -> part.map(p -> new PartTransformWrapper(p, list)));
        } else {
            return part;
        }
    }

    public static Value<Transform> parseTransform(ParseContext ctx, JsonObject root) {
        TransformFactory tf = parseGenStringStatic(ctx, root, "type", "a transform type",
            s -> FactoryRegistry.INSTANCE.getTransformFactory(new ResourceLocation(s)) != null,
            s -> FactoryRegistry.INSTANCE.getTransformFactory(new ResourceLocation(s)),
            (p1, p2) -> FactoryIdentity.IDENTITY_V);

        return tf.parse(ctx, root);
    }

    public static <T> Value<T> parseGen(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback) {
        final String errNoValue = String.format("Missing '%s' tag!", tag);
        final String errWrongType = String.format("'%s' tag needs to be %s!", tag, tagType);
        final String errDupTag = String.format("Ambiguous tag: '%s' has both simple value and matcher!", tag);
        final String errWrongMatchType = String.format("'%s: match' tag needs to be an object!", tag);
        final String errNoDefault = String.format("'%s: match' has no default branch!", tag);

        boolean hasConstant = root.has(tag);
        boolean hasMatch = root.has(tag + ": match");

        if (!hasConstant && !hasMatch) {
            ctx.error(errNoValue);
            return Value.wrap(fallback);
        }

        if (hasConstant && hasMatch) {
            ctx.error(errDupTag);
        }

        if (hasConstant) {
            JsonElement je = root.get(tag);

            if (!test.test(je)) {
                ctx.error(errWrongType);
                return Value.wrap(fallback);
            }

            return Value.wrap(mapper.apply(je));
        } else {
            JsonElement je = root.get(tag + ": match");

            if (!je.isJsonObject()) {
                ctx.error(errWrongMatchType);
                return Value.wrap(fallback);
            }

            JsonObject jo = je.getAsJsonObject();

            Map<Matcher.MatchTest, T> map = new HashMap<>();

            // I hate you Java, let me assign a fucking value in a lambda you stupid piece of shit >:(
            @SuppressWarnings("unchecked")
            T[] _default = (T[]) new Object[1];

            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                boolean isDefaultBranch = entry.getKey().equals("default");

                Consumer<T> c;

                if (!isDefaultBranch) {
                    Map<String, String> k = parseMatchDef(ctx, entry.getKey());
                    if (k == null) continue;
                    c = v -> map.put(new Matcher.MatchTest(k), v);
                } else {
                    c = v -> _default[0] = v;
                }

                if (!test.test(entry.getValue())) {
                    ctx.error(String.format("'%s' match arm '%s' needs to be %s!", tag, entry.getKey(), tagType));
                    c.accept(fallback);
                    continue;
                }

                c.accept(mapper.apply(entry.getValue()));
            }

            if (_default[0] == null) {
                ctx.error(errNoDefault);
                _default[0] = fallback;
            }

            return new Matcher<>(map, _default[0]);
        }
    }

    public static <T> T parseGenStatic(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback) {
        final String errNoValue = String.format("Missing '%s' tag!", tag);
        final String errWrongType = String.format("'%s' tag needs to be %s!", tag, tagType);

        if (!root.has(tag)) {
            ctx.error(errNoValue);
            return fallback;
        }

        final JsonElement je = root.get(tag);

        if (!test.test(je)) {
            ctx.error(errWrongType);
            return fallback;
        }

        return mapper.apply(je);
    }

    @Nullable
    private static Map<String, String> parseMatchDef(ParseContext ctx, String s) {
        Map<String, String> map = new HashMap<>();
        String[] strings = s.split(",");
        for (String entry : strings) {
            int i = entry.indexOf("=");
            String k, v;

            if (i < 1) {
                ctx.error(String.format("Malformed constraints: %s", entry));
                return null;
            }

            k = entry.substring(0, i);
            v = entry.substring(i + 1);
            map.put(k, v);
        }
        return map;
    }

    public static <T> Value<T> parseGenPrimitive(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, tagType, je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()), je -> mapper.apply(je.getAsJsonPrimitive()), fallback);
    }

    public static <T> T parseGenPrimitiveStatic(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback) {
        return parseGenStatic(ctx, root, tag, tagType, je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()), je -> mapper.apply(je.getAsJsonPrimitive()), fallback);
    }

    public static <T> Value<T> parseGenArray(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonArray> test, Function<JsonArray, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, tagType, je -> je.isJsonArray() && test.test(je.getAsJsonArray()), je -> mapper.apply(je.getAsJsonArray()), fallback);
    }

    public static <T> Value<T> parseGenArray(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, String.format("an array of %d %ss", size, tagType),
            je -> je.isJsonArray() && (size == -1 || je.getAsJsonArray().size() == size) && StreamSupport.stream(je.getAsJsonArray().spliterator(), false).allMatch(test),
            je -> mapper.apply(StreamSupport.stream(je.getAsJsonArray().spliterator(), false).collect(Collectors.toList())), fallback);
    }

    public static <T> T parseGenArrayStatic(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback) {
        return parseGenStatic(ctx, root, tag, String.format("an array of %d %ss", size, tagType),
            je -> je.isJsonArray() && (size == -1 || je.getAsJsonArray().size() == size) && StreamSupport.stream(je.getAsJsonArray().spliterator(), false).allMatch(test),
            je -> mapper.apply(StreamSupport.stream(je.getAsJsonArray().spliterator(), false).collect(Collectors.toList())), fallback);
    }

    public static <T> Value<T> parseGenPrimitiveArray(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback) {
        return parseGenArray(ctx, root, tag, tagType, size,
            je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonPrimitive).collect(Collectors.toList())), fallback);
    }

    public static <T> T parseGenPrimitiveArrayStatic(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback) {
        return parseGenArrayStatic(ctx, root, tag, tagType, size,
            je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonPrimitive).collect(Collectors.toList())), fallback);
    }

    public static <T> Value<T> parseGenObjectArray(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback) {
        return parseGenArray(ctx, root, tag, tagType, size,
            je -> je.isJsonObject() && test.test(je.getAsJsonObject()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())), fallback);
    }

    public static <T> T parseGenObjectArrayStatic(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback) {
        return parseGenArrayStatic(ctx, root, tag, tagType, size,
            je -> je.isJsonObject() && test.test(je.getAsJsonObject()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())), fallback);
    }

    public static <T> Value<T> parseGenString(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback) {
        return parseGenPrimitive(ctx, root, tag, tagType, jp -> jp.isString() && test.test(jp.getAsString()), jp -> mapper.apply(jp.getAsString()), fallback);
    }

    public static <T> T parseGenStringStatic(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback) {
        return parseGenPrimitiveStatic(ctx, root, tag, tagType, jp -> jp.isString() && test.test(jp.getAsString()), jp -> mapper.apply(jp.getAsString()), fallback);
    }

    public static <T> Value<T> parseGenObject(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, tagType, je -> je.isJsonObject() && test.test(je.getAsJsonObject()), je -> mapper.apply(je.getAsJsonObject()), fallback);
    }

    public static <T> T parseGenObjectStatic(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback) {
        return parseGenStatic(ctx, root, tag, tagType, je -> je.isJsonObject() && test.test(je.getAsJsonObject()), je -> mapper.apply(je.getAsJsonObject()), fallback);
    }

}
