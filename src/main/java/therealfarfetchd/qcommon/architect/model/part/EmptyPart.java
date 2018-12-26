package therealfarfetchd.qcommon.architect.model.part;

import java.util.Collections;
import java.util.List;

import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.Transform;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public class EmptyPart implements Part, RawPart {

    @Override
    public List<Face> getFaces(StateProvider sp) {
        return Collections.emptyList();
    }

    @Override
    public Part transform(List<Transform> trs) {
        return this;
    }

    @Override
    public List<Face> getFaces() {
        return Collections.emptyList();
    }

    @Override
    public AffineTransform getTransform(StateProvider sp) {
        return Transform.IDENTITY;
    }

    @Override
    public ValueVariance<AffineTransform> getTransformVariance() {
        return ValueVariance.Single.of(Transform.IDENTITY);
    }

    @Override
    public boolean showFace(StateProvider sp, int face) {
        return false;
    }

}
