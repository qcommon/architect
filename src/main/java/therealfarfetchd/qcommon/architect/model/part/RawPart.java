package therealfarfetchd.qcommon.architect.model.part;

import java.util.List;

import therealfarfetchd.qcommon.architect.model.AffineTransform;
import therealfarfetchd.qcommon.architect.model.Face;
import therealfarfetchd.qcommon.architect.model.value.StateProvider;

public interface RawPart {

    List<Face> getFaces();

    AffineTransform getTransform(StateProvider sp);

    ValueVariance<AffineTransform> getTransformVariance();

    boolean showFace(StateProvider sp, int face);

}
