package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
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
        Identifier rl = ctx.dp.parseGenStringStatic(ctx.log, json, "model", "an OBJ model location", $ -> true, Identifier::new, new Identifier("qcommon-architect:empty"));
        rl = new Identifier(rl.getNamespace(), String.format("render/obj/%s.obj", rl.getPath()));

        OBJRoot objIn = OBJLoader.INSTANCE.load(rl);

        if (objIn == null) {
            ctx.log.error("Failed to load OBJ model.");
            return Value.wrap(Part.EMPTY);
        }

        Value<OBJRoot> obj = Value.wrap(objIn);

        if (json.has("materials")) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, "materials", "an object", $ -> true, $ -> $, new JsonObject());
            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                String key = entry.getKey();
                if (objIn.materials.containsKey(key)) {
                    Value<OBJMaterial> mat = ctx.dp.parseGenObjectStatic(ctx.log, jo, key, "a material override", $ -> true, jo1 -> applyOverrides(ctx, jo1, objIn.materials.get(key)), Value.wrap(objIn.materials.get(key)));
                    obj = obj.flatMap(o -> mat.map(m -> o.withMaterial(key, m)));
                } else {
                    ctx.log.warn(String.format("Override for non-existing material '%s'. Ignoring", key));
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
        Value<OBJMaterial> mat = Value.wrap(orig);

        if (ctx.dp.hasKey(json, "texture")) {
            Value<String> tex = ctx.dp.parseTextureRef(ctx.log, json, "texture", TextureRef.fromString(orig.diffuseTexture)).map(TextureRef::toStringRepr);
            mat = tex.map(orig::withDiffuseTexture);
        }

        if (ctx.dp.hasKey(json, "color")) {
            Value<Color> color = ctx.dp.parseGenPrimitiveArray(ctx.log, json, "color", "color component", 3,
                f -> f.isNumber() && f.getAsFloat() >= 0 && f.getAsFloat() <= 1,
                arr -> new Color(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat()),
                orig.diffuse);

            mat = mat.flatMap(m -> color.map(m::withDiffuse));
        }

        if (ctx.dp.hasKey(json, "colour")) ctx.log.warn("no u"); // haha

        if (ctx.dp.hasKey(json, "transparency")) {
            Value<Float> tr = ctx.dp.parseGenPrimitive(ctx.log, json, "transparency", "a number in [0,1]",
                jsonPrimitive -> jsonPrimitive.isNumber() && jsonPrimitive.getAsFloat() >= 0 && jsonPrimitive.getAsFloat() <= 1,
                JsonPrimitive::getAsFloat, orig.transparency);

            mat = mat.flatMap(m -> tr.map(m::withTransparency));
        }

        return mat;
    }

}