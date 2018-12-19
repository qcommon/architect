package therealfarfetchd.qcommon.architect.loader;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.model.value.Value;
import therealfarfetchd.qcommon.croco.Vec2;
import therealfarfetchd.qcommon.croco.Vec3;

public class DataParserExt extends DataParserImpl {

    final ParseContext ctx;

    public DataParserExt(ParseContext ctx) {
        super();
        this.ctx = ctx;
    }

    @Override
    public Value<Vec3> parseCoords3D(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        return parseVec3(log, root, tag, fallback).map($ -> $.div(ctx.posScale)).pull();
    }

    @Override
    public Vec3 parseCoords3DStatic(ParseMessageContainer log, JsonObject root, String tag, Vec3 fallback) {
        return parseVec3Static(log, root, tag, fallback).div(ctx.posScale);
    }

    @Override
    public Value<Vec2> parseCoords2D(ParseMessageContainer log, JsonObject root, String tag, Vec2 fallback) {
        return parseVec2(log, root, tag, fallback).map($ -> $.div(ctx.posScale)).pull();
    }

    @Override
    public Vec2 parseCoords2DStatic(ParseMessageContainer log, JsonObject root, String tag) {
        return parseVec2Static(log, root, tag).div(ctx.posScale);
    }

}
