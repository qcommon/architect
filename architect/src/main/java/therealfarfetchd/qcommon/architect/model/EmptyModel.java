package therealfarfetchd.qcommon.architect.model;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class EmptyModel implements Model {

    public static final EmptyModel INSTANCE = new EmptyModel();

    @Override
    public Value<List<Part>> getParts() {
        return Value.wrap(Collections.emptyList());
    }

}
