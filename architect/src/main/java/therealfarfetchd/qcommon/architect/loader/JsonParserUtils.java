package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import therealfarfetchd.qcommon.architect.Architect;
import therealfarfetchd.qcommon.architect.factories.FactoryRegistry;
import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.factories.TransformFactory;
import therealfarfetchd.qcommon.architect.factories.impl.transform.FactoryIdentity;
import therealfarfetchd.qcommon.architect.math.Mat4;
import therealfarfetchd.qcommon.architect.math.Vec2;
import therealfarfetchd.qcommon.architect.math.Vec3;
import therealfarfetchd.qcommon.architect.model.EmptyPart;
import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.model.PartTransformWrapper;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefAbsolute;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefKey;

public class JsonParserUtils {

    public static Mat4 parseMat4(ParseContext ctx, JsonObject root, String tag) {
        return parseMat4(ctx, root, tag, Mat4.IDENTITY);
    }

    public static Mat4 parseMat4(ParseContext ctx, JsonObject root, String tag, Mat4 fallback) {
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

    public static Vec3 parseVec3(ParseContext ctx, JsonObject root, String tag) {
        return parseVec3(ctx, root, tag, Vec3.ORIGIN);
    }

    public static Vec3 parseVec3(ParseContext ctx, JsonObject root, String tag, Vec3 fallback) {
        return parseGenPrimitiveArray(ctx, root, tag, "number", 3, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec3(floats.get(0), floats.get(1), floats.get(2));
        }, fallback);
    }

    public static Vec2 parseVec2(ParseContext ctx, JsonObject root, String tag) {
        return parseVec2(ctx, root, tag, Vec2.ORIGIN);
    }

    public static Vec2 parseVec2(ParseContext ctx, JsonObject root, String tag, Vec2 fallback) {
        return parseGenPrimitiveArray(ctx, root, tag, "number", 2, JsonPrimitive::isNumber, l -> {
            List<Float> floats = l.stream().map(JsonPrimitive::getAsFloat).collect(Collectors.toList());
            return new Vec2(floats.get(0), floats.get(1));
        }, fallback);
    }

    public static float parseFloat(ParseContext ctx, JsonObject root, String tag) {
        return parseFloat(ctx, root, tag, 0.0f);
    }

    public static float parseFloat(ParseContext ctx, JsonObject root, String tag, float fallback) {
        return parseGenPrimitive(ctx, root, tag, "a number", JsonPrimitive::isNumber, JsonPrimitive::getAsFloat, fallback);
    }

    public static TextureRef parseTextureRef(ParseContext ctx, JsonObject root, String tag) {
        return parseTextureRef(ctx, root, tag, new TextureRefAbsolute(new ResourceLocation(Architect.MODID, "error")));
    }

    public static TextureRef parseTextureRef(ParseContext ctx, JsonObject root, String tag, TextureRef fallback) {
        return parseGenString(ctx, root, tag, "a texture name or #-key", $ -> true, tn -> {
            if (tn.startsWith("#")) return new TextureRefKey(tn.substring(1));
            else return new TextureRefAbsolute(new ResourceLocation(tn));
        }, fallback);
    }

    public static Part parsePart(ParseContext ctx, JsonObject root, String tag) {
        Optional<JsonObject> opt = parseGenObject(ctx, root, tag, "a part", $ -> true, Optional::of, Optional.empty());
        return opt.map(jo -> parsePart(ctx, jo)).orElse(EmptyPart.INSTANCE);
    }

    public static Part parsePart(ParseContext ctx, JsonObject root) {
        PartFactory pf = parseGenString(ctx, root, "type", "a part type",
            s -> FactoryRegistry.INSTANCE.getPartFactory(new ResourceLocation(s)) != null,
            s -> FactoryRegistry.INSTANCE.getPartFactory(new ResourceLocation(s)),
            (p1, p2) -> EmptyPart.INSTANCE);

        final Part part = pf.parse(ctx, root);

        if (root.has("transform")) {
            // TODO parse transforms
            List<Transform> trs = parseGenObjectArray(ctx, root, "transform", "transforms", -1,
                $ -> true,
                objs -> objs.stream().map(jo1 -> parseTransform(ctx, jo1)).collect(Collectors.toList()),
                Collections.emptyList());

            return new PartTransformWrapper(part, trs);
        } else {
            return part;
        }
    }

    public static Transform parseTransform(ParseContext ctx, JsonObject root) {
        TransformFactory tf = parseGenString(ctx, root, "type", "a transform type",
            s -> FactoryRegistry.INSTANCE.getTransformFactory(new ResourceLocation(s)) != null,
            s -> FactoryRegistry.INSTANCE.getTransformFactory(new ResourceLocation(s)),
            (p1, p2) -> FactoryIdentity.IDENTITY);

        return tf.parse(ctx, root);
    }

    public static <T> T parseGen(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback) {
        final String error1 = String.format("Missing '%s' tag!", tag);
        final String error2 = String.format("'%s' tag needs to be %s!", tag, tagType);

        if (!root.has(tag)) {
            ctx.error(error1);
            return fallback;
        }

        final JsonElement je = root.get(tag);

        if (!test.test(je)) {
            ctx.error(error2);
            return fallback;
        }

        return mapper.apply(je);
    }

    public static <T> T parseGenPrimitive(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, tagType, je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()), je -> mapper.apply(je.getAsJsonPrimitive()), fallback);
    }

    public static <T> T parseGenArray(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonArray> test, Function<JsonArray, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, tagType, je -> je.isJsonArray() && test.test(je.getAsJsonArray()), je -> mapper.apply(je.getAsJsonArray()), fallback);
    }

    public static <T> T parseGenArray(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, String.format("an array of %d %ss", size, tagType),
            je -> je.isJsonArray() && (size == -1 || je.getAsJsonArray().size() == size) && StreamSupport.stream(je.getAsJsonArray().spliterator(), false).allMatch(test),
            je -> mapper.apply(StreamSupport.stream(je.getAsJsonArray().spliterator(), false).collect(Collectors.toList())), fallback);
    }

    public static <T> T parseGenPrimitiveArray(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback) {
        return parseGenArray(ctx, root, tag, tagType, size,
            je -> je.isJsonPrimitive() && test.test(je.getAsJsonPrimitive()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonPrimitive).collect(Collectors.toList())), fallback);
    }

    public static <T> T parseGenObjectArray(ParseContext ctx, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback) {
        return parseGenArray(ctx, root, tag, tagType, size,
            je -> je.isJsonObject() && test.test(je.getAsJsonObject()),
            l -> mapper.apply(l.stream().map(JsonElement::getAsJsonObject).collect(Collectors.toList())), fallback);
    }

    public static <T> T parseGenString(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback) {
        return parseGenPrimitive(ctx, root, tag, tagType, jp -> jp.isString() && test.test(jp.getAsString()), jp -> mapper.apply(jp.getAsString()), fallback);
    }

    public static <T> T parseGenObject(ParseContext ctx, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback) {
        return parseGen(ctx, root, tag, tagType, je -> je.isJsonObject() && test.test(je.getAsJsonObject()), je -> mapper.apply(je.getAsJsonObject()), fallback);
    }

}
