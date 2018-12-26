package therealfarfetchd.qcommon.architect.factories.impl.part;

import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.Map;
import java.util.Optional;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.loader.obj.OBJLoader;
import therealfarfetchd.qcommon.architect.loader.obj.OBJPartBuilder;
import therealfarfetchd.qcommon.architect.loader.obj.structs.OBJRoot;
import therealfarfetchd.qcommon.architect.model.part.Part;

public class FactoryOBJ implements PartFactory {

    @Override
    public Part parse(ParseContext ctx, JsonObject json) {
        Identifier rl = ctx.dp.parseGenStringStatic(ctx.log, json, "model", "an OBJ model location", $ -> true, Identifier::new, new Identifier("qcommon-architect:empty"));
        rl = new Identifier(rl.getNamespace(), String.format("render/obj/%s.obj", rl.getPath()));

        OBJRoot objIn = OBJLoader.INSTANCE.load(rl);

        if (objIn == null) {
            ctx.log.error("Failed to load OBJ model.");
            return Part.EMPTY;
        }

        OBJPartBuilder pb = OBJPartBuilder.from(objIn);

        if (json.has("materials")) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, "materials", "an object", $ -> true, $ -> $, new JsonObject());
            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                String key = entry.getKey();
                if (objIn.materials.containsKey(key)) {
                    applyOverrides(ctx, pb, jo, key);
                } else {
                    ctx.log.warn(String.format("Override for non-existing material '%s'. Ignoring", key));
                }
            }
        }

        return pb.build();
    }

    private void applyOverrides(ParseContext ctx, OBJPartBuilder pb, JsonObject json, String material) {

        if (ctx.dp.hasKey(json, "texture")) {
            pb.setMatTexture(material, old -> ctx.dp.parseTextureRef(ctx.log, json, "texture").map(tex -> Optional.of(tex.toStringRepr())));
        }

        if (ctx.dp.hasKey(json, "color")) {
            pb.setMatColor(material, old -> ctx.dp.parseGenPrimitiveArray(ctx.log, json, "color", "color component", 3,
                f -> f.isNumber() && f.getAsFloat() >= 0 && f.getAsFloat() <= 1,
                arr -> new Color(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat()),
                Color.WHITE));
        }

        if (ctx.dp.hasKey(json, "colour")) ctx.log.warn("no u"); // haha

        if (ctx.dp.hasKey(json, "transparency")) {
            pb.setMatTransparency(material, old -> ctx.dp.parseGenPrimitive(ctx.log, json, "transparency", "a number in [0,1]",
                jsonPrimitive -> jsonPrimitive.isNumber() && jsonPrimitive.getAsFloat() >= 0 && jsonPrimitive.getAsFloat() <= 1,
                JsonPrimitive::getAsFloat, 1f));
        }

    }

}