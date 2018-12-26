package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;

import javax.annotation.Nullable;

import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public interface Part {

    Part EMPTY = new EmptyPart();

    List<Face> getFaces(StateProvider sp);

    @Nullable
    default RawPart getRaw() {
        return this instanceof RawPart ? (RawPart) this : null;
    }

    default Part transform(List<Transform> trs) {
        return new PartTransformWrapper(this, trs);
    }

}
