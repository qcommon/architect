package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.EmptyPart;
import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class FactoryText implements PartFactory {

    @Override
    public Value<Part> parse(ParseContext ctx, JsonObject json) {
        // TODO impl text part creation
        return Value.wrap(EmptyPart.INSTANCE);
    }

}
