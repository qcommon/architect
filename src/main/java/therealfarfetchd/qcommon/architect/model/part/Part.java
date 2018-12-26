package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public interface Part {

    Part EMPTY = new EmptyPart();

    List<Face> getFaces(StateProvider sp);

    default Part transform(List<Transform> trs) {
        return new PartTransformWrapper(this, trs);
    }

}
