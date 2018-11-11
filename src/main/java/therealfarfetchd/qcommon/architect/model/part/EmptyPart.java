package therealfarfetchd.qcommon.architect.model.part;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.Face;

public class EmptyPart implements Part {

    @Override
    public List<Face> getFaces() {
        return Collections.emptyList();
    }

}
