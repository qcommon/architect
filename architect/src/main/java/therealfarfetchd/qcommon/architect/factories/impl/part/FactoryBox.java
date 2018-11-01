package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import net.minecraft.util.EnumFacing;

import java.util.EnumMap;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.math.Vec2;
import therealfarfetchd.qcommon.architect.math.Vec3;
import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;

public class FactoryBox implements PartFactory {

    private static final EnumMap<EnumFacing, BoxFace> INITIAL_TEXTURES = new EnumMap<>(EnumFacing.class);
    private static final Vec3 FROM = Vec3.ORIGIN;
    private static final Vec3 TO = new Vec3(1, 1, 1);

    @Override
    public Part parse(ParseContext ctx, JsonObject json) {
        EnumMap<EnumFacing, BoxFace> em = INITIAL_TEXTURES.clone();
        Vec3 from = FROM;
        Vec3 to = TO;

        if (json.has("from")) from = JsonParserUtils.parseVec3(ctx, json, "from", from);
        if (json.has("to")) to = JsonParserUtils.parseVec3(ctx, json, "to", to);

        return new PartBox(em, from, to);
    }

    static {
        for (EnumFacing f : EnumFacing.VALUES) {
            INITIAL_TEXTURES.put(f, new BoxFace.ManualUV(TextureRef.PLACEHOLDER, Vec2.ORIGIN, new Vec2(1, 1)));
        }
    }

}
