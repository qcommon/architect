package therealfarfetchd.qcommon.architect.model.part;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class EmptyPart implements Part {

    @Override
    public List<Face> getFaces(StateProvider sp) {
        return Collections.emptyList();
    }

    @Override
    public Part transform(List<Transform> trs) {
        return this;
    }

}
