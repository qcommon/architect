package therealfarfetchd.qcommon.architect.model;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.value.StateProvider;
import therealfarfetchd.qcommon.architect.model.value.Value;

public class EmptyPart implements Part {

    public static final EmptyPart INSTANCE = new EmptyPart();

    @Override
    public Value<List<Face>> getFaces() {
        return Value.wrap(Collections.emptyList());
    }

}
