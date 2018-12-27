package therealfarfetchd.qcommon.architect.loader;

import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Matcher;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class DataParserImpl implements DataParser {

    public static final DataParserImpl INSTANCE = new DataParserImpl();

    private static final Pattern AXIS_VECTOR = Pattern.compile("^((|[+-])[xyzXYZ])+?$");

    protected DataParserImpl() {}

    public Value<Mat4> parseMat4(ParseMessageContainer log, JsonObject root, String tag, Mat4 fallback) {
        return parseGenPrimitiveArray(log, root, tag, "number", 16, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Mat4(
                floats.get(0), floats.get(1), floats.get(2), floats.get(3),
                floats.get(4), floats.get(5), floats.get(6), floats.get(7),
                floats.get(8), floats.get(9), floats.get(10), floats.get(11),
                floats.get(12), floats.get(13), floats.get(14), floats.get(15)
            );
        }, fallback);
    }

    public Mat4 parseMat4Static(ParseMessageContainer log, JsonObject root, String tag, Mat4 fallback) {
        return parseGenPrimitiveArrayStatic(log, root, tag, "number", 16, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Mat4(
                floats.get(0), floats.get(1), floats.get(2), floats.get(3),
                floats.get(4), floats.get(5), floats.get(6), floats.get(7),
                floats.get(8), floats.get(9), floats.get(10), floats.get(11),
                floats.get(12), floats.get(13), floats.get(14), floats.get(15)
            );
        }, fallback);
    }

    public Value<Vec3> parseVec3(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        return parseGenPrimitiveArray(log, root, tag, "number", 3, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec3(floats.get(0), floats.get(1), floats.get(2));
        }, fallback);
    }

    public Vec3 parseVec3Static(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        return parseGenPrimitiveArrayStatic(log, root, tag, "number", 3, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec3(floats.get(0), floats.get(1), floats.get(2));
        }, fallback);
    }

    public Value<Vec3> parseAxisVector(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        ParseMessageContainer log1 = new ParseMessageContainer("temp");
        ParseMessageContainer log2 = new ParseMessageContainer("temp");
        Value<Vec3> v = parseVec3(log1, root, tag, fallback);
        if (log1.isResultValid()) return v;
        log1.into(log2);
        log1 = new ParseMessageContainer("temp");
        v = parseGenString(log1, root, tag, "an axis vector", s -> AXIS_VECTOR.matcher(s).matches(), s -> {
            Vec3 vec = Vec3.ORIGIN;
            int i = 1;
            for (char c : s.toCharArray()) {
                switch (c) {
                    case '+':
                        break;
                    case '-':
                        i = -1;
                        break;
                    case 'x':
                    case 'X':
                        vec = vec.add(new Vec3(i, 0, 0));
                        i = 1;
                        break;
                    case 'y':
                    case 'Y':
                        vec = vec.add(new Vec3(0, i, 0));
                        i = 1;
                        break;
                    case 'z':
                    case 'Z':
                        vec = vec.add(new Vec3(0, 0, i));
                        i = 1;
                        break;
                }
            }
            return vec.getNormalized();
        }, fallback);
        if (log1.isResultValid()) return v;
        log1.into(log2);
        log2.into(log);
        return Value.wrap(fallback);
    }

    public Value<Vec2> parseVec2(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback) {
        return parseGenPrimitiveArray(log, root, tag, "number", 2, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec2(floats.get(0), floats.get(1));
        }, fallback);
    }

    public Vec2 parseVec2Static(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback) {
        return parseGenPrimitiveArrayStatic(log, root, tag, "number", 2, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec2(floats.get(0), floats.get(1));
        }, fallback);
    }

    public Value<Float> parseFloat(ParseMessageContainer log, JsonObject root, String tag, float fallback) {
        return parseGenPrimitive(log, root, tag, "a number", JsonPrimitive::isNumber, JsonPrimitive::getAsFloat, fallback);
    }

    public float parseFloatStatic(ParseMessageContainer log, JsonObject root, String tag, float fallback) {
        return parseGenPrimitiveStatic(log, root, tag, "a number", JsonPrimitive::isNumber, JsonPrimitive::getAsFloat, fallback);
    }

    public Value<Boolean> parseBoolean(ParseMessageContainer log, JsonObject root, String tag, boolean fallback) {
        return parseGenPrimitive(log, root, tag, "a boolean", JsonPrimitive::isBoolean, JsonPrimitive::getAsBoolean, fallback);
    }

    public Value<TextureRef> parseTextureRef(ParseMessageContainer log, JsonObject root, String tag, TextureRef fallback) {
        return parseGenString(log, root, tag, "a texture name or #-key", $ -> true, TextureRef::fromString, fallback);
    }

    public TextureRef parseTextureRefStatic(ParseMessageContainer log, JsonObject root, String tag, TextureRef fallback) {
        return parseGenStringStatic(log, root, tag, "a texture name or #-key", $ -> true, TextureRef::fromString, fallback);
    }

    public Part parsePart(ParseMessageContainer log, JsonObject root, String tag) {
        return parseGenObjectStatic(log, root, tag, "a part", $ -> true, obj -> parsePart(log, obj), Part.EMPTY);
    }

    public Part parsePart(ParseMessageContainer log, JsonObject root) {
        PartFactory pf = parseGenStringStatic(log, root, "type", "a part type",
            s -> FactoryRegistry.INSTANCE.getPartFactory(new Identifier(s)) != null,
            s -> FactoryRegistry.INSTANCE.getPartFactory(new Identifier(s)),
            (p1, p2) -> Part.EMPTY);

        final Part part = pf.parse(ParseContext.wrap(this, log), root);

        if (root.has("transform")) {
            List<Transform> trs = parseGenObjectArrayStatic(log, root, "transform", "transforms", -1,
                $ -> true,
                objs -> objs.stream().map(jo1 -> parseTransform(log, jo1)).collect(Collectors.toList()),
                Collections.emptyList());

            return part.transform(trs);
        } else {
            return part;
        }
    }

    public Transform parseTransform(ParseMessageContainer log, JsonObject root) {
        TransformFactory tf = parseGenStringStatic(log, root, "type", "a transform type",
            s -> FactoryRegistry.INSTANCE.getTransformFactory(new Identifier(s)) != null,
            s -> FactoryRegistry.INSTANCE.getTransformFactory(new Identifier(s)),
            (p1, p2) -> Transform.IDENTITY);

        return tf.parse(ParseContext.wrap(this, log), root);
    }

    public <T> Value<T> parseGen(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback) {
        final String errNoValue = String.format("Missing '%s' tag!", tag);
        final String errWrongType = String.format("'%s' tag needs to be %s!", tag, tagType);
        final String errDupTag = String.format("Ambiguous tag: '%s' has both simple value and matcher!", tag);
        final String errWrongMatchType = String.format("'%s: match' tag needs to be an object!", tag);
        final String errNoDefault = String.format("'%s: match' has no default branch!", tag);

        boolean hasConstant = root.has(tag);
        boolean hasMatch = root.has(tag + ": match");

        if (!hasConstant && !hasMatch) {
            log.error(errNoValue);
            return Value.wrap(fallback);
        }

        if (hasConstant && hasMatch) {
            log.error(errDupTag);
        }

        if (hasConstant) {
            JsonElement je = root.get(tag);

            if (!test.test(je)) {
                log.error(errWrongType);
                return Value.wrap(fallback);
            }

            return Value.wrap(mapper.apply(je));
        } else {
            JsonElement je = root.get(tag + ": match");

            if (!je.isJsonObject()) {
                log.error(errWrongMatchType);
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
                    Map<String, String> k = parseMatchDef(log, entry.getKey());
                    if (k == null) continue;
                    c = v -> map.put(new Matcher.MatchTest(k), v);
                } else {
                    c = v -> _default[0] = v;
                }

                if (!test.test(entry.getValue())) {
                    log.error(String.format("'%s' match arm '%s' needs to be %s!", tag, entry.getKey(), tagType));
                    c.accept(fallback);
                    continue;
                }

                c.accept(mapper.apply(entry.getValue()));
            }

            if (_default[0] == null) {
                log.error(errNoDefault);
                _default[0] = fallback;
            }

            return new Matcher<>(map, _default[0]);
        }
    }

    public <T> T parseGenStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback) {
        final String errNoValue = String.format("Missing '%s' tag!", tag);
        final String errWrongType = String.format("'%s' tag needs to be %s!", tag, tagType);

        if (!root.has(tag)) {
            log.error(errNoValue);
            return fallback;
        }

        final JsonElement je = root.get(tag);

        if (!test.test(je)) {
            log.error(errWrongType);
            return fallback;
        }

        return mapper.apply(je);
    }

    @Nullable
    private static Map<String, String> parseMatchDef(ParseMessageContainer log, String s) {
        Map<String, String> map = new HashMap<>();
        String[] strings = s.split(",");
        for (String entry : strings) {
            int i = entry.indexOf("=");
            String k, v;

            if (i < 1) {
                log.error(String.format("Malformed constraints: %s", entry));
                return null;
            }

            k = entry.substring(0, i);
            v = entry.substring(i + 1);
            map.put(k, v);
        }
        return map;
    }

    public <T> Value<T> parseGenPrimitive(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback) {
        return parseGen(log, root, tag, tagType, je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()), je -> mapper.apply(je.getAsJsonPrimitive()), fallback);
    }

    public <T> T parseGenPrimitiveStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback) {
        return parseGenStatic(log, root, tag, tagType, je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()), je -> mapper.apply(je.getAsJsonPrimitive()), fallback);
    }

    public <T> Value<T> parseGenArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonArray> test, Function<JsonArray, T> mapper, T fallback) {
        return parseGen(log, root, tag, tagType, je -> je.isJsonArray() && test.test(je.getAsJsonArray()), je -> mapper.apply(je.getAsJsonArray()), fallback);
    }

    public <T> Value<T> parseGenArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback) {
        return parseGen(log, root, tag, String.format("an array of %d %ss", size, tagType),
            je -> je.isJsonArray() && (size == -1 || je.getAsJsonArray().size() == size) && StreamSupport.stream(je.getAsJsonArray().spliterator(), false).allMatch(test),
            je -> mapper.apply(StreamSupport.stream(je.getAsJsonArray().spliterator(), false).collect(Collectors.toList())), fallback);
    }

    public <T> T parseGenArrayStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback) {
        return parseGenStatic(log, root, tag, String.format("an array of %d %ss", size, tagType),
            je -> je.isJsonArray() && (size == -1 || je.getAsJsonArray().size() == size) && StreamSupport.stream(je.getAsJsonArray().spliterator(), false).allMatch(test),
            je -> mapper.apply(StreamSupport.stream(je.getAsJsonArray().spliterator(), false).collect(Collectors.toList())), fallback);
    }

    public <T> Value<T> parseGenPrimitiveArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback) {
        return parseGenArray(log, root, tag, tagType, size,
            je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonPrimitive).collect(Collectors.toList())), fallback);
    }

    public <T> T parseGenPrimitiveArrayStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback) {
        return parseGenArrayStatic(log, root, tag, tagType, size,
            je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonPrimitive).collect(Collectors.toList())), fallback);
    }

    public <T> Value<T> parseGenObjectArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback) {
        return parseGenArray(log, root, tag, tagType, size,
            je -> je.isJsonObject() && test.test(je.getAsJsonObject()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())), fallback);
    }

    public <T> T parseGenObjectArrayStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback) {
        return parseGenArrayStatic(log, root, tag, tagType, size,
            je -> je.isJsonObject() && test.test(je.getAsJsonObject()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())), fallback);
    }

    public <T> Value<T> parseGenString(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback) {
        return parseGenPrimitive(log, root, tag, tagType, jp -> jp.isString() && test.test(jp.getAsString()), jp -> mapper.apply(jp.getAsString()), fallback);
    }

    public <T> T parseGenStringStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback) {
        return parseGenPrimitiveStatic(log, root, tag, tagType, jp -> jp.isString() && test.test(jp.getAsString()), jp -> mapper.apply(jp.getAsString()), fallback);
    }

    public <T> Value<T> parseGenObject(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback) {
        return parseGen(log, root, tag, tagType, je -> je.isJsonObject() && test.test(je.getAsJsonObject()), je -> mapper.apply(je.getAsJsonObject()), fallback);
    }

    public <T> T parseGenObjectStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback) {
        return parseGenStatic(log, root, tag, tagType, je -> je.isJsonObject() && test.test(je.getAsJsonObject()), je -> mapper.apply(je.getAsJsonObject()), fallback);
    }

    public boolean hasKey(JsonObject root, String tag) {
        return root.has(tag) || root.has(String.format("%s: match", tag));
    }

}
