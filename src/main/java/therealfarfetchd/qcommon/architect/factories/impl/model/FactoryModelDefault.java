package therealfarfetchd.qcommon.architect.factories.impl.model;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.loader.ModelTransformLoader;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.ModelTransformMap;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefAbsolute;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefKey;

public class FactoryModelDefault implements ModelFactory {

    @Override
    public Model parse(ParseContext ctx, JsonObject json) {
        return merge(ctx, json, Model.EMPTY);
    }

    @Override
    public Model merge(ParseContext ctx, JsonObject json, Model parent) {
        @SuppressWarnings("ConstantConditions")
        List<Part> parts = new ArrayList<>(parent.getParts(null));

        TextureMapper texs = parent.getTextureMapper();

        if (json.has("parts")) {
            ctx.dp.parseGenObjectArrayStatic(ctx.log, json, "parts", "part", -1, $ -> true,
                l -> l.stream().map(jo -> ctx.dp.parsePart(ctx.log, jo)), Stream.<Part>empty()).forEach(parts::add);
        }

        if (json.has("textures")) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, "textures", "a texture definition map", $ -> true, $ -> $, new JsonObject());

            Map<String, TextureRef> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                TextureRef vt = ctx.dp.parseTextureRefStatic(ctx.log, jo, entry.getKey());
                map.put(entry.getKey(), vt);
            }

            Map<String, Identifier> resolved = new HashMap<>();

            {
                List<String> tried = new ArrayList<>();

                for (Entry<String, TextureRef> entry : map.entrySet()) {
                    String s = entry.getKey();
                    TextureRef tr = entry.getValue();

                    while (true) {
                        if (tr instanceof TextureRefAbsolute) {
                            resolved.put(s, ((TextureRefAbsolute) tr).texture);
                            break;
                        } else if (tr instanceof TextureRefKey) {
                            final String newTexKey = ((TextureRefKey) tr).key;

                            if (tried.contains(newTexKey)) {
                                ctx.log.error(String.format("Circular dependency for texture %s: %s -> %s", s, String.join(" -> ", tried), newTexKey));
                                break;
                            }

                            TextureRef tr1 = map.get(newTexKey);
                            tried.add(newTexKey);

                            if (tr1 == null) {
                                ctx.log.error(String.format("Texture %s resolves to non existing texture %s", s, newTexKey));
                                break;
                            }

                            tr = tr1;
                        } else {
                            ctx.log.error(String.format("Unhandled texture reference type %s for texture %s", tr.getClass().getName(), s));
                            break;
                        }
                    }
                }
            }

            texs = s -> resolved.containsKey(s) ? resolved.get(s) : parent.getTextureMapper().getTexture(s);
        }

        ModelTransformMap tr = null;

        if (json.has("model-transform")) {
            final Identifier fallback = new Identifier("minecraft", "identity");
            final Identifier rl = ctx.dp.parseGenStringStatic(ctx.log, json, "model-transform", "identifier", $ -> true, Identifier::new, fallback);

            if (rl != fallback) {
                tr = ModelTransformLoader.INSTANCE.load(new Identifier(rl.getNamespace(), String.format("render/model-transform/%s.json", rl.getPath())));

            }
        }

        if (tr == null) tr = ModelTransformMap.IDENTITY;

        return new DefaultModel(parts, texs, tr);
    }

}
