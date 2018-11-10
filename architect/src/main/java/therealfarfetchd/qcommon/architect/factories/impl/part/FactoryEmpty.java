package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.part.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryEmpty implements PartFactory {

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        return Value.wrap(Part.EMPTY);
    }

}
