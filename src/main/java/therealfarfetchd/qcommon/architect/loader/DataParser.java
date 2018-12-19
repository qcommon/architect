package therealfarfetchd.qcommon.architect.loader;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Mat4;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public interface DataParser {

    default Value<Mat4> parseMat4(ParseMessageContainer log, JsonObject root, String tag) {
        return parseMat4(log, root, tag, Mat4.IDENTITY);
    }

    default Mat4 parseMat4Static(ParseMessageContainer log, JsonObject root, String tag) {
        return parseMat4Static(log, root, tag, Mat4.IDENTITY);
    }

    default Value<Vec3> parseVec3(ParseMessageContainer log, JsonObject root, String tag) {
        return parseVec3(log, root, tag, Vec3.ORIGIN);
    }

    default Vec3 parseVec3Static(ParseMessageContainer log, JsonObject root, String tag) {
        return parseVec3Static(log, root, tag, Vec3.ORIGIN);
    }

    default Value<Vec3> parseAxisVector(ParseMessageContainer log, JsonObject root, String tag) {
        return parseAxisVector(log, root, tag, new Vec3(0, 1, 0));
    }

    default Value<Vec2> parseVec2(ParseMessageContainer log, JsonObject root, String tag) {
        return parseVec2(log, root, tag, Vec2.ORIGIN);
    }

    default Vec2 parseVec2Static(ParseMessageContainer log, JsonObject root, String tag) {
        return parseVec2Static(log, root, tag, Vec2.ORIGIN);
    }

    default Value<Vec3> parseCoords3D(ParseMessageContainer log, JsonObject root, String tag) {
        return parseCoords3D(log, root, tag, Vec3.ORIGIN);
    }

    default Vec3 parseCoords3DStatic(ParseMessageContainer log, JsonObject root, String tag) {
        return parseCoords3DStatic(log, root, tag, Vec3.ORIGIN);
    }

    default Value<Vec2> parseCoords2D(ParseMessageContainer log, JsonObject root, String tag) {
        return parseCoords2D(log, root, tag, Vec2.ORIGIN);
    }

    default Vec2 parseCoords2DStatic(ParseMessageContainer log, JsonObject root, String tag) {
        return parseCoords2DStatic(log, root, tag, Vec2.ORIGIN);
    }

    default Value<Float> parseFloat(ParseMessageContainer log, JsonObject root, String tag) {
        return parseFloat(log, root, tag, 0f);
    }

    default float parseFloatStatic(ParseMessageContainer log, JsonObject root, String tag) {
        return parseFloatStatic(log, root, tag, 0f);
    }

    default Value<Boolean> parseBoolean(ParseMessageContainer log, JsonObject root, String tag) {
        return parseBoolean(log, root, tag, false);
    }

    default Value<TextureRef> parseTextureRef(ParseMessageContainer log, JsonObject root, String tag) {
        return parseTextureRef(log, root, tag, TextureRef.PLACEHOLDER);
    }

    Value<Mat4> parseMat4(ParseMessageContainer log, JsonObject root, String tag, Mat4 fallback);

    Mat4 parseMat4Static(ParseMessageContainer log, JsonObject root, String tag, Mat4 fallback);

    Value<Vec3> parseVec3(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback);

    Vec3 parseVec3Static(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback);

    Value<Vec3> parseAxisVector(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback);

    Value<Vec2> parseVec2(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback);

    Vec2 parseVec2Static(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback);

    default Value<Vec3> parseCoords3D(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        return parseVec3(log, root, tag, fallback);
    }

    default Vec3 parseCoords3DStatic(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        return parseVec3Static(log, root, tag, fallback);
    }

    default Value<Vec2> parseCoords2D(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback) {
        return parseVec2(log, root, tag, fallback);
    }

    default Vec2 parseCoords2DStatic(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback) {
        return parseVec2Static(log, root, tag, fallback);
    }

    Value<Float> parseFloat(ParseMessageContainer log, JsonObject root, String tag, float fallback);

    float parseFloatStatic(ParseMessageContainer log, JsonObject root, String tag, float fallback);

    Value<Boolean> parseBoolean(ParseMessageContainer log, JsonObject root, String tag, boolean fallback);

    Value<TextureRef> parseTextureRef(ParseMessageContainer log, JsonObject root, String tag, TextureRef fallback);

    Value<Part> parsePart(ParseMessageContainer log, JsonObject root, String tag);

    Value<Part> parsePart(ParseMessageContainer log, JsonObject root);

    Value<Transform> parseTransform(ParseMessageContainer log, JsonObject root);

    <T> Value<T> parseGen(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback);

    <T> T parseGenStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonElement> test, Function<JsonElement, T> mapper, T fallback);

    <T> Value<T> parseGenPrimitive(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback);

    <T> T parseGenPrimitiveStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonPrimitive> test, Function<JsonPrimitive, T> mapper, T fallback);

    <T> Value<T> parseGenArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonArray> test, Function<JsonArray, T> mapper, T fallback);

    <T> Value<T> parseGenArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback);

    <T> T parseGenArrayStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonElement> test, Function<List<JsonElement>, T> mapper, T fallback);

    <T> Value<T> parseGenPrimitiveArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback);

    <T> T parseGenPrimitiveArrayStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonPrimitive> test, Function<List<JsonPrimitive>, T> mapper, T fallback);

    <T> Value<T> parseGenObjectArray(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback);

    <T> T parseGenObjectArrayStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, int size, Predicate<JsonObject> test, Function<List<JsonObject>, T> mapper, T fallback);

    <T> Value<T> parseGenString(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback);

    <T> T parseGenStringStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<String> test, Function<String, T> mapper, T fallback);

    <T> Value<T> parseGenObject(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback);

    <T> T parseGenObjectStatic(ParseMessageContainer log, JsonObject root, String tag, String tagType, Predicate<JsonObject> test, Function<JsonObject, T> mapper, T fallback);

    boolean hasKey(JsonObject root, String tag);

}




