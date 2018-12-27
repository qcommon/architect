package therealfarfetchd.qcommon.architect.factories.impl.model;

import com.google.gson.JsonObject;

import therealfarfetchd.qcommon.architect.factories.ModelFactory;
import therealfarfetchd.qcommon.architect.loader.ParseContext;
import therealfarfetchd.qcommon.architect.model.Model;

public class FactoryModelEmpty implements ModelFactory {

    @Override
    public Model parse(ParseContext ctx, JsonObject json) {
        return Model.EMPTY;
    }

    @Override
    public Model merge(ParseContext ctx, JsonObject json, Model parent) {
        return Model.EMPTY;
    }

}
