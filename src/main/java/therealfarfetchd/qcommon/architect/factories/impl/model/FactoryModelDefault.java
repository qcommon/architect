package therealfarfetchd.qcommon.architect.factories.impl.model;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.EmptyModel;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.texref.TextureMapper;
import therealfarfetchd.qcommon.architect.model.texref.TextureRef;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefAbsolute;
import therealfarfetchd.qcommon.architect.model.texref.TextureRefKey;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryModelDefault implements ModelFactory {

    @Override
    public Model parse(ParseContext ctx, JsonObject json) {
        List<Value<Part>> parts = new ArrayList<>();

        Value<TextureMapper> texs = EmptyModel.EMPTY_MAPPER;

        if (json.has("parts")) {
            ctx.dp.parseGenObjectArrayStatic(ctx.log, json, "parts", "part", -1, $ -> true,
                l -> l.stream().map(jo -> ctx.dp.parsePart(ctx.log, jo)), Stream.<Value<Part>>empty()).forEach(parts::add);
        }

        if (json.has("textures")) {
            JsonObject jo = ctx.dp.parseGenObjectStatic(ctx.log, json, "textures", "a texture definition map", $ -> true, $ -> $, new JsonObject());

            Map<String, Value<TextureRef>> map = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : jo.entrySet()) {
                Value<TextureRef> vt = ctx.dp.parseTextureRef(ctx.log, jo, entry.getKey());
                map.put(entry.getKey(), vt);
            }

            Value<Map<String, TextureRef>> map1 = Value.extract(map);

            texs = map1.map(m -> {
                Map<String, Identifier> result = new HashMap<>();

                List<String> tried = new ArrayList<>();
                for (String s : m.keySet()) {
                    tried.clear();
                    tried.add(s);
                    TextureRef tr = m.get(s);
                    while (true) {
                        if (tr instanceof TextureRefAbsolute) {
                            result.put(s, ((TextureRefAbsolute) tr).texture);
                            break;
                        } else if (tr instanceof TextureRefKey) {
                            final String newTexKey = ((TextureRefKey) tr).key;

                            if (tried.contains(newTexKey)) {
                                ctx.log.error(String.format("Circular dependency for texture %s: %s -> %s", s, String.join(" -> ", tried), newTexKey));
                                break;
                            }

                            TextureRef tr1 = m.get(newTexKey);
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

                return result::get;
            });
        }

        return new DefaultModel(Value.extract(parts).pull(), texs.pull());
    }

}
