package therealfarfetchd.qcommon.architect.model;

import java.util.Collections;
import java.util.List;

public class EmptyModel implements Model {

    public static final EmptyModel INSTANCE = new EmptyModel();

    @Override
    public List<Part> getParts() {
        return Collections.emptyList();
    }

}
