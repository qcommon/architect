package therealfarfetchd.qcommon.architect.factories.impl.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.loader.JsonParserUtils;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryModelDefault implements ModelFactory {

    @Override
    public Model parse(ParseContext ctx, JsonObject json) {
        List<Value<Part>> parts = new ArrayList<>();

        if (json.has("parts")) {
            JsonParserUtils.parseGenObjectArrayStatic(ctx, json, "parts", "part", -1, $ -> true,
                l -> l.stream().map(jo -> JsonParserUtils.parsePart(ctx, jo)), Stream.<Value<Part>>empty()).forEach(parts::add);
        }

        return new DefaultModel(parts);
    }

}
