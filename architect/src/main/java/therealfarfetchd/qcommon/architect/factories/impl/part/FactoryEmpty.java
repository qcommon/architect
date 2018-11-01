package therealfarfetchd.qcommon.architect.factories.impl.part;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.PartFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.EmptyPart;
import therealfarfetchd.qcommon.architect.model.Part;

public class FactoryEmpty implements PartFactory {

    @Override
    public Part parse(ParseContext ctx, JsonObject json) {
        return EmptyPart.INSTANCE;
    }

}
