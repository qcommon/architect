package therealfarfetchd.qcommon.architect.factories.impl.model;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Model;
import therealfarfetchd.qcommon.architect.model.Part;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class DefaultModel implements Model {

    private final Value<List<Part>> parts;

    public DefaultModel(List<Value<Part>> parts) {
        this.parts = Value.extract(parts);
    }

    @Override
    public Value<List<Part>> getParts() {
        return parts;
    }

}
