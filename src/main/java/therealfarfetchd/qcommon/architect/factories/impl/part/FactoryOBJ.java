package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

import java.util.Map;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.obj.OBJLoader;
import therealfarfetchd.qcommon.architect.loader.obj.PreparedOBJ;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJMaterial;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.part.PartOBJ;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryOBJ implements PartFactory {

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        ResourceLocation rl = JsonParserUtils.parseGenStringStatic(ctx, json, "model", "an OBJ model location", $ -> true, ResourceLocation::new, new ResourceLocation("qcommon-architect:empty"));
        rl = new ResourceLocation(rl.getNamespace(), String.format("render/obj/%s.obj", rl.getPath()));

        OBJRoot objIn = OBJLoader.INSTANCE.load(rl);

        if (objIn == null) {
            ctx.error("Failed to load OBJ model.");
            return Value.wrap(Part.EMPTY);
        }

        Value<OBJRoot> obj = Value.wrap(objIn);

        if (json.has("materials")) {
            JsonObject jo = JsonParserUtils.parseGenObjectStatic(ctx, json, "materials", "an object", $ -> true, $ -> $, new JsonObject());
            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                String key = entry.getKey();
                if (objIn.materials.containsKey(key)) {
                    Value<OBJMaterial> mat = JsonParserUtils.parseGenObjectStatic(ctx, jo, key, "a material override", $ -> true, jo1 -> applyOverrides(ctx, jo1, objIn.materials.get(key)), Value.wrap(objIn.materials.get(key)));
                    obj = obj.flatMap(o -> mat.map(m -> o.withMaterial(key, m)));
                } else {
                    ctx.warn(String.format("Override for non-existing material '%s'. Ignoring", key));
                }
            }
        }

        // this is bad memory wise because it will create a fuckton of PreparedOBJ objects which all contain a copy of the OBJ's faces.
        // Better have 1 PreparedOBJ and pass the overrides to PartOBJ's constructor.
        // This way was easier to implement right now though :D
        // TODO optimize
        return obj.<Part>map(o -> new PartOBJ(PreparedOBJ.get(o))).pull();
    }

    private Value<OBJMaterial> applyOverrides(ParseContext ctx, JsonObject json, OBJMaterial orig) {
        Value<OBJMaterial> mat = null;

        // TODO color, transparency
        if (json.has("texture")) {
            Value<String> tex = JsonParserUtils.parseTextureRef(ctx, json, "texture", TextureRef.fromString(orig.diffuseTexture)).map(TextureRef::toStringRepr);
            mat = tex.map(orig::withDiffuseTexture);
        }

        return mat == null ? Value.wrap(orig) : mat;
    }

}